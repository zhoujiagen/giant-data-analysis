package com.spike.giantdataanalysis.opentsdb;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import net.opentsdb.core.TSDB;
import net.opentsdb.meta.Annotation;
import net.opentsdb.stats.StatsCollector;
import net.opentsdb.tsd.RTPublisher;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.google.common.collect.Maps;
import com.stumbleupon.async.Deferred;

/**
 * 使用Kafka的实时发布器.
 * 
 * <pre>
 * Kafka Topic: metric name, and read from property 'tsd.extension.plugin.rt.kakfa.topic.annotation'(default is opentsdb.annotation).
 * </pre>
 * @author zhoujiagen
 */
public class KafkaRTPublisher extends RTPublisher {

  private Properties kafkaProperties;
  private KafkaProducer<String, KafkaProducerRecord> kafkaProducer;

  private AtomicLong dataPointCount = new AtomicLong(0);
  private AtomicLong dataPointSuccessCount = new AtomicLong(0);
  private AtomicLong dataPointFailureCount = new AtomicLong(0);
  private AtomicLong annotationCount = new AtomicLong(0);
  private AtomicLong annotationSuccessCount = new AtomicLong(0);
  private AtomicLong annotationFailureCount = new AtomicLong(0);

  @Override
  public void initialize(TSDB tsdb) {
    this.kafkaProperties = tsdb.getConfig().kafkaProperties();
    if (kafkaProperties.isEmpty()) {
      throw new IllegalStateException(
          "Kafka properties 'tsd.extension.plugin.kakfa.file' not set!");
    }
    kafkaProducer = new KafkaProducer<>(kafkaProperties);
  }

  @Override
  public Deferred<Object> shutdown() {
    try {
      kafkaProducer.close();
    } catch (Exception e) {
      return Deferred.fromError(e);
    }
    return Deferred.<Object> fromResult(Boolean.TRUE);
  }

  @Override
  public String version() {
    return "1.0.0";
  }

  @Override
  public void collectStats(StatsCollector collector) {
    collector.record("rt.kafka.dp.all", dataPointCount);
    collector.record("rt.kafka.dp.success", dataPointSuccessCount);
    collector.record("rt.kafka.dp.fail", dataPointFailureCount);
    collector.record("rt.kafka.anno.all", annotationCount);
    collector.record("rt.kafka.anno.success", annotationSuccessCount);
    collector.record("rt.kafka.anno.fail", annotationFailureCount);
  }

  @Override
  public Deferred<Object> publishDataPoint(String metric, long timestamp, long value,
      Map<String, String> tags, byte[] tsuid) {
    dataPointCount.incrementAndGet();

    KafkaProducerRecord record = KafkaProducerRecordBuilder.builder()//
        .metric(metric)//
        .timestamp(timestamp)//
        .isFloatValue(false)//
        .longValue(value)//
        .tags(tags)//
        .tsuid(tsuid)//
        .build();
    try {
      RecordMetadata metadata =
          kafkaProducer.send(new ProducerRecord<String, KafkaProducerRecord>(metric, record)).get();
      dataPointSuccessCount.incrementAndGet();
      return Deferred.<Object> fromResult(metadata);
    } catch (InterruptedException e) {
      dataPointFailureCount.incrementAndGet();
      return Deferred.fromError(e);
    } catch (ExecutionException e) {
      dataPointFailureCount.incrementAndGet();
      return Deferred.fromError(e);
    }
  }

  @Override
  public Deferred<Object> publishDataPoint(String metric, long timestamp, double value,
      Map<String, String> tags, byte[] tsuid) {
    dataPointCount.incrementAndGet();
    KafkaProducerRecord record = KafkaProducerRecordBuilder.builder()//
        .metric(metric)//
        .timestamp(timestamp)//
        .isFloatValue(false)//
        .doubleValue(value)//
        .tags(tags)//
        .tsuid(tsuid)//
        .build();
    try {
      RecordMetadata metadata =
          kafkaProducer.send(new ProducerRecord<String, KafkaProducerRecord>(metric, record)).get();
      dataPointSuccessCount.incrementAndGet();
      return Deferred.<Object> fromResult(metadata);
    } catch (InterruptedException e) {
      dataPointFailureCount.incrementAndGet();
      return Deferred.fromError(e);
    } catch (ExecutionException e) {
      dataPointFailureCount.incrementAndGet();
      return Deferred.fromError(e);
    }
  }

  @Override
  public Deferred<Object> publishAnnotation(Annotation annotation) {
    annotationCount.incrementAndGet();
    KafkaProducerRecord record = KafkaProducerRecordBuilder.builder()//
        .annotation(annotation)//
        .build();
    try {
      String topic = kafkaProperties.getProperty("tsd.extension.plugin.rt.kakfa.topic.annotation");
      if (topic == null || topic.trim().equals("")) {
        topic = "opentsdb.annotation";
      }
      RecordMetadata metadata =
          kafkaProducer.send(new ProducerRecord<String, KafkaProducerRecord>(topic, record)).get();
      annotationSuccessCount.incrementAndGet();
      return Deferred.<Object> fromResult(metadata);
    } catch (InterruptedException e) {
      annotationFailureCount.incrementAndGet();
      return Deferred.fromError(e);
    } catch (ExecutionException e) {
      annotationFailureCount.incrementAndGet();
      return Deferred.fromError(e);
    }
  }

  /**
   * <pre>
   * metric The name of the metric associated with the data point
   * timestamp Timestamp as a Unix epoch in seconds or milliseconds (depending on the TSD's configuration)
   * value Value for the data point
   * tags Tagk/v pairs
   * tsuid Time series UID for the value
   * annotation The published annotation
   * </pre>
   */
  public static final class KafkaProducerRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String metric;
    private long timestamp;
    private boolean isFloatValue = true;
    private long longValue;
    private double doubleValue;
    private Map<String, String> tags = Maps.newHashMap();
    private byte[] tsuid;
    private Annotation annotation;

    KafkaProducerRecord() {
    }

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

    public boolean isFloatValue() {
      return isFloatValue;
    }

    public void setFloatValue(boolean isFloatValue) {
      this.isFloatValue = isFloatValue;
    }

    public long getLongValue() {
      return longValue;
    }

    public void setLongValue(long longValue) {
      this.longValue = longValue;
    }

    public double getDoubleValue() {
      return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
      this.doubleValue = doubleValue;
    }

    public Map<String, String> getTags() {
      return tags;
    }

    public void setTags(Map<String, String> tags) {
      this.tags = tags;
    }

    public byte[] getTsuid() {
      return tsuid;
    }

    public void setTsuid(byte[] tsuid) {
      this.tsuid = tsuid;
    }

    public Annotation getAnnotation() {
      return annotation;
    }

    public void setAnnotation(Annotation annotation) {
      this.annotation = annotation;
    }

  }

  public static final class KafkaProducerRecordBuilder {

    private KafkaProducerRecord record = new KafkaProducerRecord();

    private KafkaProducerRecordBuilder() {
    }

    public static KafkaProducerRecordBuilder builder() {
      return new KafkaProducerRecordBuilder();
    }

    public KafkaProducerRecord build() {
      return record;
    }

    public KafkaProducerRecordBuilder metric(String metric) {
      record.setMetric(metric);
      return this;
    }

    public KafkaProducerRecordBuilder timestamp(long timestamp) {
      record.setTimestamp(timestamp);
      return this;
    }

    public KafkaProducerRecordBuilder isFloatValue(boolean isFloatValue) {
      record.setFloatValue(isFloatValue);
      return this;
    }

    public KafkaProducerRecordBuilder longValue(long longValue) {
      record.setLongValue(longValue);
      return this;
    }

    public KafkaProducerRecordBuilder doubleValue(double doubleValue) {
      record.setDoubleValue(doubleValue);
      return this;
    }

    public KafkaProducerRecordBuilder tags(Map<String, String> tags) {
      record.setTags(tags);
      return this;
    }

    public KafkaProducerRecordBuilder addTag(String tagk, String tagv) {
      record.getTags().put(tagk, tagv);
      return this;
    }

    public KafkaProducerRecordBuilder tsuid(byte[] tsuid) {
      record.setTsuid(tsuid);
      return this;
    }

    public KafkaProducerRecordBuilder annotation(Annotation annotation) {
      record.setAnnotation(annotation);
      return this;
    }

  }

}
