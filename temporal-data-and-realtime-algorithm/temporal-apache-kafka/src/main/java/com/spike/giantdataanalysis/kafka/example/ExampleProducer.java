package com.spike.giantdataanalysis.kafka.example;

import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.kafka.support.KafkaProducerConfigEnum;

/**
 * <pre>
 * KafKa Producer示例
 * 
 * 命令行客户端测试：
 * $ bin/kafka-console-consumer.sh --zookeeper localhost:2188 --topic test --from-beginning
 * 
 * </pre>
 * @author zhoujiagen
 * @see KafkaProducer
 */
public final class ExampleProducer {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleProducer.class);

  /** 主题名称 */
  public static final String TOPIC_NAME = "test";

  public static void main(String[] args) {
    Properties config = new Properties();
    // broker列表
    config.put(KafkaProducerConfigEnum.BOOTSTRAP_SERVERS.getKey(), "localhost:9092");
    // 分区leader等待follower同步完成
    config.put(KafkaProducerConfigEnum.ACKS.getKey(), "all");
    // 不重试
    config.put(KafkaProducerConfigEnum.RETRIES.getKey(), 0);
    // 消息批量的大小
    config.put(KafkaProducerConfigEnum.BATCH_SIZE.getKey(), 16384);
    // 发送时的等待延迟(毫秒)
    config.put(KafkaProducerConfigEnum.LINGER_MS.getKey(), 1);
    // 用于缓存消息的字节数
    config.put(KafkaProducerConfigEnum.BUFFER_MEMORY.getKey(), 33554432);
    // 键序列化器
    config.put(KafkaProducerConfigEnum.KEY_SERIALIZER.getKey(),
      "org.apache.kafka.common.serialization.StringSerializer");
    // 值序列化器
    config.put(KafkaProducerConfigEnum.VALUE_SERIALIZER.getKey(),
      "org.apache.kafka.common.serialization.StringSerializer");

    LOG.info("创建生产者...");
    try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(config);) {

      for (int i = 0; i < 10; i++) {
        Date now = new Date();
        String message = "Message Publish time: " + now;

        ProducerRecord<String, String> record = new ProducerRecord<String, String>(//
            TOPIC_NAME, //
            String.valueOf(now.getTime()), //
            message//
            );

        Callback callback = new Callback() {
          @Override
          public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (metadata != null) {
              LOG.info("记录元信息: " + metadata);
            }
            if (exception != null) {
              LOG.error("发送消息失败", exception);
            }
          }
        };

        LOG.info("发送消息：" + record.toString());
        kafkaProducer.send(record, callback);

        Thread.sleep(1000L);// wait a second
      }
    } catch (Exception e) {
      LOG.error("发布消息失败", e);
    }

  }

}