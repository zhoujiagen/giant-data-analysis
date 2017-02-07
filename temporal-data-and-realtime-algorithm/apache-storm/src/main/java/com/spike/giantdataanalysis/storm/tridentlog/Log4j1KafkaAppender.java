package com.spike.giantdataanalysis.storm.tridentlog;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.FileAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @see
 * @author zhoujiagen
 * @see FileAppender
 */
public class Log4j1KafkaAppender extends AppenderSkeleton {

  private Producer<String, String> producer;

  public Log4j1KafkaAppender() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "1");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    this.producer = new KafkaProducer<String, String>(props);
  }

  @Override
  public void close() {
    System.err.println("close...");
    this.producer.close();
  }

  @Override
  public boolean requiresLayout() {
    return false;
  }

  @Override
  protected void append(LoggingEvent event) {
    System.err.println(event);
  }
}
