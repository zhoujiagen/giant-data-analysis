package com.spike.giantdataanalysis.kafka.example;

import java.util.Date;

import org.apache.kafka.log4jappender.KafkaLog4jAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * KafkaLog4jAppender的使用
 * 
 * REF: http://stackoverflow.com/questions/32301129/kafka-log4j-appender-not-sending-messages
 * 
 * log4j.properties:
 * 
 * log4j.logger.logGen=DEBUG, kafka
 * 
 * ### kafka
 * log4j.appender.kafka=org.apache.kafka.log4jappender.KafkaLog4jAppender
 * log4j.appender.kafka.topic=log4j1-topic
 * log4j.appender.kafka.brokerList=127.0.0.1:9092
 * log4j.appender.kafka.compressionType=none
 * log4j.appender.kafka.syncSend=true
 * log4j.appender.kafka.layout=org.apache.log4j.PatternLayout
 * log4j.appender.kafka.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} [%p] [%t] %l => %m%n
 * 
 * </pre>
 * @author zhoujiagen
 * @see KafkaLog4jAppender
 */
public class ExampleKafkaLog4jAppender {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleKafkaLog4jAppender.class);

  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
      String messageKey = String.valueOf(new Date().getTime());
      LOG.info(messageKey);
    }
  }

}
