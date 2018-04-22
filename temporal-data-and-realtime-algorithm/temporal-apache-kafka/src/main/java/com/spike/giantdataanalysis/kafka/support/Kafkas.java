package com.spike.giantdataanalysis.kafka.support;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka工具类.
 * @author zhoujiagen
 */
public final class Kafkas {
  private static final Logger LOG = LoggerFactory.getLogger(Kafkas.class);

  private Kafkas() {
  }

  // ======================================== Producer

  /**
   * 创建生产者.
   * @param kafkaProperties
   * @return
   * @see org.apache.kafka.clients.producer.ProducerConfig
   */
  public static <K, V> KafkaProducer<K, V> producer(Properties kafkaProperties) {
    return new KafkaProducer<>(kafkaProperties);
  }

  /**
   * 创建生产者记录 - 指定分区.
   * @param topic
   * @param key
   * @param value
   * @return
   */
  public static <K, V> ProducerRecord<K, V> producerRecord(String topic, Integer partition, K key,
      V value) {
    return new ProducerRecord<K, V>(topic, partition, key, value);
  }

  /**
   * 创建生产者记录.
   * @param topic
   * @param key
   * @param value
   * @return
   */
  public static <K, V> ProducerRecord<K, V> producerRecord(String topic, K key, V value) {
    return new ProducerRecord<K, V>(topic, key, value);
  }

  /**
   * 创建生产者记录 - key为null.
   * @param topic
   * @param value
   * @return
   */
  public static <K, V> ProducerRecord<K, V> producerRecord(String topic, V value) {
    return new ProducerRecord<K, V>(topic, value);
  }

  /**
   * 生产消息: Fire and forget.
   * @param producer
   * @param producerRecord
   * @return
   */
  public static <K, V> Future<RecordMetadata> sendFireAndForget(KafkaProducer<K, V> producer,
      ProducerRecord<K, V> producerRecord) {
    return producer.send(producerRecord);
  }

  /**
   * 生产消息: 同步.
   * @param producer
   * @param producerRecord
   * @return
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static <K, V> RecordMetadata sendSynchronously(KafkaProducer<K, V> producer,
      ProducerRecord<K, V> producerRecord) throws InterruptedException, ExecutionException {
    return producer.send(producerRecord).get();
  }

  /**
   * 生产消息: 异步
   * @param producer
   * @param producerRecord
   * @param callback
   * @return
   */
  public static <K, V> Future<RecordMetadata> sendAsynchronously(KafkaProducer<K, V> producer,
      ProducerRecord<K, V> producerRecord, Callback callback) {
    return producer.send(producerRecord, callback);
  }

  // ======================================== Partitioner
  /**
   * 总将特定键分配到最后一个分区的分区器.
   */
  public static class SpecificKeyPartitioner implements Partitioner {

    public static final String CONFIG_NAME_KEY = "key.name";
    private String key = "default";

    @Override
    public void configure(Map<String, ?> configs) {
      key = (String) configs.get(CONFIG_NAME_KEY);
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value,
        byte[] valueBytes, Cluster cluster) {

      // 分区数量
      int numPartitions = cluster.partitionsForTopic(topic).size();

      if ((keyBytes == null) || (!(key instanceof String))) {
        throw new InvalidRecordException("expect a String type key!");
      }

      if (this.key.equals((String) key)) {
        // 最后一个分区
        return numPartitions;
      } else {
        // 按键的值哈希到其他分区
        return Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1);
      }

    }

    @Override
    public void close() {
      // do nothing
    }
  }

  // ======================================== Consumer

  /**
   * 创建消费者.
   * <p>
   * 按照规则, 一个消费者使用一个线程.
   * @param kafkaProperties
   * @return
   * @see org.apache.kafka.clients.consumer.ConsumerConfig
   */
  public static <K, V> KafkaConsumer<K, V> consumer(Properties kafkaProperties) {
    return new KafkaConsumer<>(kafkaProperties);
  }

  /** 订阅主题. */
  public static <K, V> void subscribe(KafkaConsumer<K, V> consumer, List<String> topics) {
    consumer.subscribe(topics);
  }

  /** 订阅主题. */
  public static <K, V> void subscribe(KafkaConsumer<K, V> consumer, List<String> topics,
      ConsumerRebalanceListener listener) {
    consumer.subscribe(topics, listener);
  }

  /** 订阅主题. */
  public static <K, V> void subscribe(KafkaConsumer<K, V> consumer, Pattern pattern,
      ConsumerRebalanceListener listener) {
    consumer.subscribe(pattern, listener);
  }

  /** 展示消费者记录内容. */
  public static <K, V> String asString(ConsumerRecord<K, V> record) {
    StringBuilder sb = new StringBuilder();
    sb.append("topic=").append(record.topic()).append(", ");
    sb.append("partition=").append(record.partition()).append(", ");
    sb.append("offset=").append(record.offset()).append(", ");
    sb.append("key=").append(record.key()).append(", ");
    sb.append("value=").append(record.value()).append(", ");
    return sb.toString();
  }

  /**
   * 轮询.
   * 
   * <pre>
   * 在消费者群组中, 除获取数据外, 执行
   * (1) 第一次调用时, 查找GroupCoordinator, 加入群组, 接受分配的分区;
   * (2) 如果发生分区再均衡, 重新获取分配的分区;
   * (3) 发送心跳: 消费者必须持续轮询, 否则会被认为死亡.
   * 
   * org.apache.kafka.clients.consumer.InvalidOffsetException - if the offset for a partition or set of partitions is undefined or out of range and no offset reset policy has been configured
   * org.apache.kafka.common.errors.WakeupException - if wakeup() is called before or while this function is called
   * org.apache.kafka.common.errors.AuthorizationException - if caller does Read access to any of the subscribed topics or to the configured groupId
   * org.apache.kafka.common.KafkaException - for any other unrecoverable errors (e.g. invalid groupId or session timeout, errors deserializing key/value pairs, or any new error cases in future versions)
   * </pre>
   * @param consumer
   * @param timeoutMs 等待Broker返回数据的毫秒数
   */
  public static <K, V> ConsumerRecords<K, V> poll(KafkaConsumer<K, V> consumer, long timeoutMs) {
    return consumer.poll(timeoutMs);
  }

  /**
   * 提交当前偏移量: 同步方式, 提交由poll()返回的最新偏移量.
   * 
   * <pre>
   * auto.commit.offset设置为false时, 由应用程序决定何时提交.
   * 没有发生不可恢复错误时, 会一直重试直到提交成功.
   * </pre>
   * @param consumer
   */
  public static <K, V> void commitSync(KafkaConsumer<K, V> consumer) {
    try {
      consumer.commitSync();
    } catch (CommitFailedException e) {
      LOG.error("commit sync failed", e);
    }
  }

  /**
   * 提交当前偏移量: 异步方式.
   * @param consumer
   */
  public static <K, V> void commitAsync(KafkaConsumer<K, V> consumer) {
    consumer.commitAsync();
  }

  /**
   * 提交当前偏移量: 异步方式, 带回调.
   * @param consumer
   * @param callback
   */
  public static <K, V> void
      commitAsync(KafkaConsumer<K, V> consumer, OffsetCommitCallback callback) {
    consumer.commitAsync(callback);
  }

  /**
   * 提交特定的偏移量: 异步方式, 带回调.
   * @param consumer
   * @param offsets
   * @param callback
   */
  public static <K, V> void commitAsync(KafkaConsumer<K, V> consumer,
      final Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback) {
    consumer.commitAsync(callback);
  }

  /**
   * 由消费者记录构造主题分片表示.
   * @param record
   * @return
   */
  public static <K, V> TopicPartition topicPartition(ConsumerRecord<K, V> record) {
    return new TopicPartition(record.topic(), record.partition());
  }

  /**
   * 构造偏移量和元数据.
   * @param offset
   * @param metadata
   * @return
   */
  public static OffsetAndMetadata offsetAndMetadata(long offset, String metadata) {
    return new OffsetAndMetadata(offset, metadata);
  }

  /**
   * 定位到分区指定偏移量处.
   * @param consumer
   * @param partition
   * @param offset
   */
  public static <K, V> void
      seek(KafkaConsumer<K, V> consumer, TopicPartition partition, long offset) {
    consumer.seek(partition, offset);
  }

  /**
   * 定位到分区头部.
   * @param consumer
   * @param partition
   */
  public static <K, V> void seekToBeginning(KafkaConsumer<K, V> consumer, TopicPartition partition) {
    consumer.seekToBeginning(partition);
  }

  /**
   * 定位到分区尾部.
   * @param consumer
   * @param partition
   */
  public static <K, V> void seekToEnd(KafkaConsumer<K, V> consumer, TopicPartition partition) {
    consumer.seekToEnd(partition);
  }

  /**
   * 暂停消费者.
   * @param consumer
   * @param partitions
   */
  public static <K, V> void pause(KafkaConsumer<K, V> consumer, TopicPartition... partitions) {
    consumer.pause(partitions);
  }

  /**
   * 从暂停中恢复消费者.
   * @param consumer
   * @param partitions
   */
  public static <K, V> void resume(KafkaConsumer<K, V> consumer, TopicPartition... partitions) {
    consumer.resume(partitions);
  }

  /**
   * 消费者退出.
   * @param consumer
   * @return
   */
  public static <K, V> Thread wakeup(final KafkaConsumer<K, V> consumer) {
    return new Thread(new Runnable() {
      @Override
      public void run() {
        LOG.info("consumer={} exit...");
        // 唯一一个可以在其他线程中安全调用的方法
        consumer.wakeup();
      }
    });
  }

  /**
   * 获取消费者在分区中下一个记录的偏移量.
   * @param consumer
   * @param partition
   * @return
   */
  public static <K, V> long position(KafkaConsumer<K, V> consumer, TopicPartition partition) {
    return consumer.position(partition);
  }

}
