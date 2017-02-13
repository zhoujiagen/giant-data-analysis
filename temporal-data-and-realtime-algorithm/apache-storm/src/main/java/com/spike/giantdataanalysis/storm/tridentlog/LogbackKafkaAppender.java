package com.spike.giantdataanalysis.storm.tridentlog;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.spike.giantdataanalysis.storm.tridentlog.formatter.JsonFormatter;
import com.spike.giantdataanalysis.storm.tridentlog.formatter.LogFormatter;

public class LogbackKafkaAppender extends AppenderBase<ILoggingEvent> {

  public static final String DEFAULT_TOPIC_NAME = "log-analysis";

  public LogbackKafkaAppender() {
  }

  // configurable
  private LogFormatter formatter;
  private String kafkaBootStrapServersString = "localhost:9092";
  private String topic = DEFAULT_TOPIC_NAME;

  private Producer<String, String> producer;
  private static Callback callback = new Callback() {
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
      if (metadata != null) {
        System.err.println("记录元信息: " + metadata);
      }

      if (exception != null) {
        exception.printStackTrace();
      }
    }
  };

  @Override
  public void start() {
    super.start();

    Properties props = new Properties();
    props.put("bootstrap.servers", kafkaBootStrapServersString);
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    producer = new KafkaProducer<String, String>(props);
  }

  @Override
  public void stop() {
    super.stop();

    producer.close();
  }

  @Override
  protected void append(ILoggingEvent event) {
    if (this.formatter == null) this.formatter = new JsonFormatter();

    String messageKey = String.valueOf(event.getTimeStamp());
    String message = this.formatter.format(event);
    ProducerRecord<String, String> record = new ProducerRecord<String, String>(//
        this.topic, messageKey, message);

    producer.send(record, callback);
  }

  // getter/setter

  public LogFormatter getFormatter() {
    return formatter;
  }

  public void setFormatter(LogFormatter formatter) {
    this.formatter = formatter;
  }

  public String getKafkaBootStrapServersString() {
    return kafkaBootStrapServersString;
  }

  public void setKafkaBootStrapServersString(String kafkaBootStrapServersString) {
    this.kafkaBootStrapServersString = kafkaBootStrapServersString;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

}
