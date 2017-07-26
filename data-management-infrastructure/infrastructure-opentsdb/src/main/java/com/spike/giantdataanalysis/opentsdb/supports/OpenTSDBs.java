package com.spike.giantdataanalysis.opentsdb.supports;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.opentsdb.core.TSDB;
import net.opentsdb.utils.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;

public class OpenTSDBs {

  private static final Logger LOG = LoggerFactory.getLogger(OpenTSDBs.class);

  public static TSDB CLIENT(String configFile) {
    Config config;
    try {
      config = new Config(configFile);
      TSDB tsdb = new TSDB(config);
      return tsdb;
    } catch (IOException e) {
      LOG.error("Invalid configFile", e);
    }
    return null;
  }

  public static void SHUTDOWN(TSDB tsdb) {
    LOG.info("Shutdown TSDN instance...");
    try {
      tsdb.shutdown().join();
    } catch (Exception e) {
      LOG.error("Shutdown TSDN instance fail", e);
    }
  }

  public static void SYNC_ADDPOINT(TSDB tsdb, String metric, long timestamp, Object value,
      Map<String, String> tags) {
    Preconditions.checkNotNull(metric);
    Preconditions.checkState(!"".equals(metric));
    Preconditions.checkNotNull(value);

    Deferred<Object> deferred = null;
    if (value instanceof Long || value instanceof Integer || value instanceof Short
        || value instanceof Byte) {
      deferred = tsdb.addPoint(metric, timestamp, ((long) value), tags);
    } else if (value instanceof Float) {
      deferred = tsdb.addPoint(metric, timestamp, ((float) value), tags);
    } else if (value instanceof Double) {
      deferred = tsdb.addPoint(metric, timestamp, ((double) value), tags);
    } else {
      LOG.error("Invalid value in metric point");
      return;
    }

    LOG.debug("Writing data to TSDB");
    deferred.addErrback(new errBack());
    deferred.addCallback(new succBack());

    LOG.debug("Writing data to TSDB, metric: {}, timestamp: {},value: {}, tags: {}",//
      metric, String.valueOf(timestamp), value, tags.toString());
    try {
      // Block the thread until the deferred returns it's result.
      deferred.join();
    } catch (Exception e) {
      LOG.error("Writing data to TSDB fail", e);
    }
  }

  public static <T> void SYNC_ADDPOINT(TSDB tsdb, List<AbstractMetricPoint> points) {
    if (points == null || points.size() == 0) return;

    ArrayList<Deferred<Object>> deferreds = new ArrayList<Deferred<Object>>();

    for (AbstractMetricPoint point : points) {
      Deferred<Object> deferred = null;
      if (point instanceof LongMetricPoint) {
        deferred =
            tsdb.addPoint(point.getMetric(), point.getTimestamp(),
              ((LongMetricPoint) point).getValue(), point.getTags());
      } else if (point instanceof FloatMetricPoint) {
        deferred =
            tsdb.addPoint(point.getMetric(), point.getTimestamp(),
              ((FloatMetricPoint) point).getValue(), point.getTags());
      } else if (point instanceof DoubleMetricPoint) {
        deferred =
            tsdb.addPoint(point.getMetric(), point.getTimestamp(),
              ((DoubleMetricPoint) point).getValue(), point.getTags());
      } else {
        LOG.error("Invalid value in metric point");
        continue;
      }
      deferreds.add(deferred);
    }

    try {
      // Block the thread until the deferred returns it's result.
      Deferred.groupInOrder(deferreds)//
          .addErrback(new errBack())//
          .addCallback(new succBatchBack())//
          .join();
      LOG.info("Writing data to TSDB, size: {}", String.valueOf(deferreds.size()));
    } catch (Exception e) {
      LOG.error("Writing data to TSDB fail", e);
    }

  }

  public static class MetricPointBuilder {
    private AbstractMetricPoint mp;

    public MetricPointBuilder(long value) {
      mp = new LongMetricPoint(value);
    }

    public MetricPointBuilder(float value) {
      mp = new FloatMetricPoint(value);
    }

    public MetricPointBuilder(double value) {
      mp = new DoubleMetricPoint(value);
    }

    public MetricPointBuilder metric(String metric) {
      mp.setMetric(metric);
      return this;
    }

    public MetricPointBuilder timestamp(long timestamp) {
      mp.setTimestamp(timestamp);
      return this;
    }

    public MetricPointBuilder tags(Map<String, String> tags) {
      mp.setTags(tags);
      return this;
    }

    public AbstractMetricPoint build() {
      return mp;
    }

  }

  public static abstract class AbstractMetricPoint {
    protected String metric;
    protected long timestamp;
    protected Map<String, String> tags;

    public String getMetric() {
      return metric;
    }

    public void setMetric(String metric) {
      this.metric = metric;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public Map<String, String> getTags() {
      return tags;
    }

    public void setTags(Map<String, String> tags) {
      this.tags = tags;
    }
  }

  public static class DoubleMetricPoint extends AbstractMetricPoint {
    private double value;

    public DoubleMetricPoint(double value) {
      this.value = value;
    }

    public double getValue() {
      return value;
    }

    public void setValue(double value) {
      this.value = value;
    }
  }

  public static class LongMetricPoint extends AbstractMetricPoint {
    private long value;

    public LongMetricPoint(long value) {
      this.value = value;
    }

    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }

  }

  public static class FloatMetricPoint extends AbstractMetricPoint {
    private float value;

    public FloatMetricPoint(float value) {
      this.value = value;
    }

    public float getValue() {
      return value;
    }

    public void setValue(float value) {
      this.value = value;
    }
  }

  public static class MetricPoint {

    private String metric;
    private Class<?> valueClz;
    private Object value;
    private long timestamp;
    private Map<String, String> tags;

    private MetricPoint() {
    }

    public Class<?> getValueClz() {
      return valueClz;
    }

    public void setValueClz(Class<?> valueClz) {
      this.valueClz = valueClz;
    }

    public String getMetric() {
      return metric;
    }

    public void setMetric(String metric) {
      this.metric = metric;
    }

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public Map<String, String> getTags() {
      return tags;
    }

    public void setTags(Map<String, String> tags) {
      this.tags = tags;
    }
  }

  /**
   * <pre>
   * TSV文件内容
   * "Date"   "Daily minimum temperatures in Melbourne, Australia, 1981-1990"
   * "1981-01-01"    20.7
   * "1981-01-02"    17.9
   * </pre>
   * @param args
   * @throws IOException
   * @throws ParseException
   */
  public static void TSV() {

    try (BufferedReader reader =
        new BufferedReader(
            new FileReader("src/main/resources/daily-minimum-temperatures-in-me.tsv"));) {
      String line = reader.readLine(); // header
      System.out.println("Header: " + line);
      List<String> list = Lists.newArrayList();
      long timestamp = System.currentTimeMillis();
      double temperature = 0d;
      while ((line = reader.readLine()) != null) {

        list = Splitter.on("\t").splitToList(line.replaceAll("\"", ""));
        timestamp = new SimpleDateFormat("yyyy-mm-dd").parse(list.get(0)).getTime();
        temperature = Double.valueOf(list.get(1));
        System.out.println("timestamp=" + timestamp + ", temperature=" + temperature);
      }
    } catch (Exception e) {
      LOG.error("fail", e);
    }

  }

  public static void main(String[] args) {
  }

  // This is an optional errorback to handle when there is a failure.
  public static class errBack implements Callback<String, Exception> {
    public String call(final Exception e) throws Exception {
      String message = ">>>>>>>>>>>Failure!>>>>>>>>>>>";
      LOG.error(message + " " + e.getMessage(), e);
      return message;
    }
  };

  // This is an optional `batch` success callback to handle when there is a success.
  public static class succBatchBack implements Callback<Object, ArrayList<Object>> {
    public Object call(final ArrayList<Object> results) {
      LOG.debug("Successfully wrote " + results.size() + " data points");
      return null;
    }
  };

  // This is an optional success callback to handle when there is a success.
  public static class succBack implements Callback<Object, Object> {
    public Object call(final Object results) {
      LOG.debug("Successfully wrote data points");
      return null;
    }
  };
}
