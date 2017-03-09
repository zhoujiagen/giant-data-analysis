package com.spike.giantdataanalysis.storm.tridentlog;

import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogKafkaAppenderTest {

  private static final Logger LOG = LoggerFactory.getLogger(LogKafkaAppenderTest.class);

  @Test
  public void testKafka() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "127.0.0.1:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    Producer<String, String> producer = new KafkaProducer<String, String>(props);
    String messageKey = String.valueOf(new Date().getTime());
    ProducerRecord<String, String> record = new ProducerRecord<String, String>(//
        "log4j1-topic", //
        messageKey, //
        "hello!!!" + messageKey//
    );
    producer.send(record);

    producer.close();
  }

  @Test
  public void testUsage() {
    for (int i = 0; i < 100; i++) {
      String messageKey = String.valueOf(new Date().getTime());
      LOG.info(messageKey);
    }
  }

  /**
   * <pre>
   * 生成日志
   * 
   * (1) 每5秒一条日志 6次
   * (2) 每1秒一条日志 15次
   * (3) 每5秒一条日志 6次
   * </pre>
   * @throws InterruptedException
   */
  @Test
  public void generateDataPattern() throws InterruptedException {

    int slowCount = 6;
    int fastCount = 15;
    // slow state
    for (int i = 0; i < slowCount; i++) {
      LOG.warn("This is a warning (slow state).");
      Thread.sleep(5000);
    }
    // enter rapid state
    for (int i = 0; i < fastCount; i++) {
      LOG.warn("This is a warning (rapid state).");
      Thread.sleep(1000);
    }
    // return to slow state
    for (int i = 0; i < slowCount; i++) {
      LOG.warn("This is a warning (slow state).");
      Thread.sleep(5000);
    }

  }
}
