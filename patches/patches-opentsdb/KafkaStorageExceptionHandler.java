package com.spike.giantdataanalysis.opentsdb;

import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import net.opentsdb.core.IncomingDataPoint;
import net.opentsdb.core.TSDB;
import net.opentsdb.stats.StatsCollector;
import net.opentsdb.tsd.StorageExceptionHandler;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.stumbleupon.async.Deferred;

/**
 * 使用Kafka的存储异常处理器.
 * @author zhoujiagen
 */
public class KafkaStorageExceptionHandler extends StorageExceptionHandler {

  private Properties kafkaProperties;
  private KafkaProducer<String, ErrorRecord> kafkaProducer;
  private String errorTopic;

  private AtomicLong dataPointCount = new AtomicLong(0);
  private AtomicLong dataPointSuccessCount = new AtomicLong(0);
  private AtomicLong dataPointFailureCount = new AtomicLong(0);

  @Override
  public void initialize(TSDB tsdb) {
    this.kafkaProperties = tsdb.getConfig().kafkaProperties();
    if (kafkaProperties.isEmpty()) {
      throw new IllegalStateException(
          "Kafka properties 'tsd.extension.plugin.kakfa.file' not set!");
    }
    String errorTopicKey = "tsd.extension.plugin.storage_exception_handler.kakfa.topic.error";
    errorTopic = kafkaProperties.getProperty(errorTopicKey);
    if (errorTopic == null || "".equals(errorTopic.trim())) {
      throw new IllegalStateException("Kafka properties '" + errorTopicKey + "' not set!");
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
    collector.record("storage_exception_handler.kafka.dp.all", dataPointCount);
    collector.record("storage_exception_handler.kafka.dp.success", dataPointSuccessCount);
    collector.record("storage_exception_handler", dataPointFailureCount);
  }

  @Override
  public void handleError(IncomingDataPoint dp, Exception exception) {
    dataPointCount.incrementAndGet();
    try {
      ErrorRecord record = new ErrorRecord(dp, exception);
      kafkaProducer.send(new ProducerRecord<String, ErrorRecord>(errorTopic, record)).get();
    } catch (InterruptedException e) {
      dataPointFailureCount.incrementAndGet();
    } catch (ExecutionException e) {
      dataPointFailureCount.incrementAndGet();
    }

  }

  public static final class ErrorRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private final IncomingDataPoint dp;
    private final Exception exception;

    public ErrorRecord(IncomingDataPoint dp, Exception exception) {
      this.dp = dp;
      this.exception = exception;
    }

    public IncomingDataPoint getDp() {
      return dp;
    }

    public Exception getException() {
      return exception;
    }

  }

}
