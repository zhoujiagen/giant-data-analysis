package com.spike.giantdataanalysis.kafka.example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.kafka.support.KafkaProducerConfigEnum;

/**
 * 带分区功能的Kafka Producer示例
 * @author zhoujiagen
 * @see KafkaProducerExample
 */
public class ExampleProducerWithPartitioner {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleProducerWithPartitioner.class);

  private static final Random RANDOM = new Random(new Date().getTime());

  public static void main(String[] args) {
    Properties config = new Properties();
    config.put(KafkaProducerConfigEnum.BOOTSTRAP_SERVERS.getKey(), "localhost:9092");
    config.put(KafkaProducerConfigEnum.ACKS.getKey(), "all");
    config.put(KafkaProducerConfigEnum.RETRIES.getKey(), 0);
    config.put(KafkaProducerConfigEnum.BATCH_SIZE.getKey(), 16384);
    config.put(KafkaProducerConfigEnum.LINGER_MS.getKey(), 1);
    config.put(KafkaProducerConfigEnum.BUFFER_MEMORY.getKey(), 33554432);
    config.put(KafkaProducerConfigEnum.KEY_SERIALIZER.getKey(),
      "org.apache.kafka.common.serialization.StringSerializer");
    config.put(KafkaProducerConfigEnum.VALUE_SERIALIZER.getKey(),
      "org.apache.kafka.common.serialization.StringSerializer");

    // 指定分区实现类
    config.put(KafkaProducerConfigEnum.PARTITIONER_CLASS.getKey(),
      IPAddressPartitioner.class.getCanonicalName());

    LOG.info("创建生产者...");
    try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(config);) {
      for (int i = 0; i < 10; i++) {

        Date now = new Date();
        int lastFragmentInIPAddress = RANDOM.nextInt() % 256;
        if (lastFragmentInIPAddress < 0) {
          lastFragmentInIPAddress = -lastFragmentInIPAddress;
        }
        String messageKey = "192.168.11." + String.valueOf(lastFragmentInIPAddress);
        String messageValue = "Message Publish time: " + now;

        ProducerRecord<String, String> record = new ProducerRecord<String, String>(//
            ExampleProducer.TOPIC_NAME, //
            messageKey, //
            messageValue//
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

        Thread.sleep(1000L);
      }
    } catch (Exception e) {
      LOG.error("发布消息失败", e);
    }

  }

  // =============================================================================支持类
  /**
   * IP分区实现
   * @see Partitioner
   * @see DefaultPartitioner
   */
  public static class IPAddressPartitioner implements Partitioner {

    private static final Logger LOG = LoggerFactory.getLogger(IPAddressPartitioner.class);

    @Override
    public void configure(Map<String, ?> configs) {
      // do nothing
    }

    /**
     * @see DefaultPartitioner#partition(String, Object, byte[], Object, byte[], Cluster)
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value,
        byte[] valueBytes, Cluster cluster) {
      LOG.info("计算分区...");

      int result = 0;

      List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
      int numPartitions = partitions.size();
      LOG.info("主题[" + topic + "]可用的分区数量是: " + numPartitions);

      if (keyBytes == null) {

        LOG.info("键为空");

        // 可用的分区数量
        List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
        LOG.info("size of available partitions for topic[" + topic + "] is: "
            + availablePartitions.size());

        if (availablePartitions.size() > 0) {
          int part = 256 % availablePartitions.size();
          result = availablePartitions.get(part).partition();
        } else {
          result = 256 % numPartitions;
        }

      } else {

        LOG.info("键不为空");

        String partitionKey = (String) key;
        int lastFragmentInIPAddresss =
            Integer.parseInt(partitionKey.substring(partitionKey.lastIndexOf(".") + 1));

        result = lastFragmentInIPAddresss % numPartitions;
      }

      LOG.info("计算分区结果：" + result);
      return result;
    }

    @Override
    public void close() {
      // do nothing
    }

  }

}