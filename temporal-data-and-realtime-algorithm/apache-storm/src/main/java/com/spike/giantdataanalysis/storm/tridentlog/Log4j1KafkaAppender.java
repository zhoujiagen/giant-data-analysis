//package com.spike.giantdataanalysis.storm.tridentlog;
//
//import java.util.Properties;
//
//import org.apache.kafka.clients.producer.Callback;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.log4j.AppenderSkeleton;
//import org.apache.log4j.FileAppender;
//import org.apache.log4j.spi.LoggingEvent;
//
//import com.spike.giantdataanalysis.storm.tridentlog.formatter.JsonFormatter;
//import com.spike.giantdataanalysis.storm.tridentlog.formatter.LogFormatter;
//
///**
// * @author zhoujiagen
// * @see FileAppender
// */
//public class Log4j1KafkaAppender extends AppenderSkeleton {
//
//  public static final String DEFAULT_TOPIC_NAME = "log-analysis";
//
//  private LogFormatter formatter;
//
//  private String kafkaBootStrapServersString = "localhost:9092";
//  private String topic = DEFAULT_TOPIC_NAME;
//  private Producer<String, String> producer;
//  private static Callback callback = new Callback() {
//    @Override
//    public void onCompletion(RecordMetadata metadata, Exception exception) {
//      if (metadata != null) {
//        System.err.println("记录元信息: " + metadata);
//      }
//
//      if (exception != null) {
//        exception.printStackTrace();
//      }
//    }
//  };
//
//  public Log4j1KafkaAppender() {
//  }
//
//  @Override
//  public void activateOptions() {
//    Properties props = new Properties();
//    props.put("bootstrap.servers", kafkaBootStrapServersString);
//    props.put("acks", "all");
//    props.put("retries", 0);
//    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//    producer = new KafkaProducer<String, String>(props);
//  }
//
//  @Override
//  public void close() {
//    producer.close();
//  }
//
//  @Override
//  public boolean requiresLayout() {
//    return false;
//  }
//
//  @Override
//  protected void append(LoggingEvent event) {
//    if (this.formatter == null) this.formatter = new JsonFormatter();
//
//    String messageKey = String.valueOf(event.getTimeStamp());
//    String message = this.formatter.format(event);
//    ProducerRecord<String, String> record = new ProducerRecord<String, String>(//
//        this.topic, messageKey, message);
//
//    producer.send(record, callback);
//  }
//
//  // ========================================================== getter/setter
//  public String getTopic() {
//    return topic;
//  }
//
//  public void setTopic(String topic) {
//    this.topic = topic;
//  }
//
//  public LogFormatter getFormatter() {
//    return formatter;
//  }
//
//  public void setFormatter(LogFormatter formatter) {
//    this.formatter = formatter;
//  }
//
//  public String getKafkaBootStrapServersString() {
//    return kafkaBootStrapServersString;
//  }
//
//  public void setKafkaBootStrapServersString(String kafkaBootStrapServersString) {
//    this.kafkaBootStrapServersString = kafkaBootStrapServersString;
//  }
//
//}
