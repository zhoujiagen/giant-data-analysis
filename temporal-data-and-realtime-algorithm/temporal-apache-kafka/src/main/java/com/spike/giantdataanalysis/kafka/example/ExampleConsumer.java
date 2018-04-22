package com.spike.giantdataanalysis.kafka.example;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.kafka.support.KafkaConsumerConfigEnum;

public final class ExampleConsumer {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleConsumer.class);

  /** 组ID */
  public static final String GROUP_NAME = "test";

  public static void main(String[] args) {

    Properties props = new Properties();
    props.put(KafkaConsumerConfigEnum.BOOTSTRAP_SERVERS.getKey(), "localhost:9092");
    // 消费者所属的组ID
    props.put(KafkaConsumerConfigEnum.GROUP_ID.getKey(), GROUP_NAME);
    // 开启自动提交
    props.put(KafkaConsumerConfigEnum.ENABLE_AUTO_COMMIT.getKey(), "true");
    // 自动提交时间间隔
    props.put(KafkaConsumerConfigEnum.AUTO_COMMIT_INTERVAL_MS.getKey(), "1000");
    // 会话超时时间
    props.put(KafkaConsumerConfigEnum.SESSION_TIMEOUT_MS.getKey(), "30000");
    props.put(KafkaConsumerConfigEnum.KEY_DESERIALIZER.getKey(),
      "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(KafkaConsumerConfigEnum.VALUE_DESERIALIZER.getKey(),
      "org.apache.kafka.common.serialization.StringDeserializer");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);) {
      // 订阅主题
      consumer.subscribe(Arrays.asList("foo", "bar", "test"));

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(100);
        for (ConsumerRecord<String, String> record : records) {
          System.out.printf(record.toString());
        }

        Thread.sleep(3 * 1000L);
      }
    } catch (Exception e) {
      LOG.error("消费消息出错！", e);
    }
  }
}