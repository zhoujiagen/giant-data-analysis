package com.spike.giantdataanalysis.kafka.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.kafka.support.Kafkas;

/**
 * 消费者再均衡监听器示例.
 * @author zhoujiagen
 */
public class ExampleConsumerRelanceListener {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleConsumerRelanceListener.class);

  static List<String> topics = new ArrayList<>();

  public static void main(String[] args) {
    topics.add("test-topic");

    new OnRevoke<String, String>(Kafkas.<String, String> consumer(new Properties())).play();
    new OnAssigned<String, String>(Kafkas.<String, String> consumer(new Properties()),//
        new MockingDataBase<String, String>()).play();
  }

  /** example of ConsumerRebalanceListener#onPartitionsRevoked */
  static class OnRevoke<K, V> {
    private final KafkaConsumer<K, V> consumer;
    // 当前的各分区偏移量
    private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    public OnRevoke(KafkaConsumer<K, V> consumer) {
      this.consumer = consumer;
    }

    private class HandleRebalance implements ConsumerRebalanceListener {

      @Override
      public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        LOG.info("lost partitions in reblance, commit current offsets: {}", offsets);
        consumer.commitSync(offsets); //
      }

      @Override
      public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
      }

    }

    public void play() {

      try {
        consumer.subscribe(topics, new HandleRebalance()); // 订阅主题

        while (true) {
          ConsumerRecords<K, V> records = consumer.poll(100l);
          for (ConsumerRecord<K, V> record : records) {
            LOG.info("topic={}, partition={}, offset={}, key={}, value={}",//
              record.topic(), record.partition(), record.offset(), record.key(), record.value());

            offsets.put(new TopicPartition(record.topic(), record.partition()), //
              new OffsetAndMetadata(record.offset() + 1, "no metadata"));
          }
          consumer.commitAsync(offsets, null); // 异步提交
        }
      } catch (WakeupException e) {
        // ignore because of closing consumer
      } catch (Exception e) {
        LOG.error("unexpected error", e);
      } finally {
        try {
          consumer.commitSync(offsets); // 同步提交
        } finally {
          LOG.info("close consumer={}", consumer);
          consumer.close();
        }
      }

    }
  }

  /** example of ConsumerRebalanceListener#onPartitionsAssigned */
  static class OnAssigned<K, V> {
    private final KafkaConsumer<K, V> consumer;
    private final MockingDataBase<K, V> database;

    public OnAssigned(KafkaConsumer<K, V> consumer, MockingDataBase<K, V> database) {
      this.consumer = consumer;
      this.database = database;
    }

    private class SaveOffsetsOnReblance implements ConsumerRebalanceListener {
      private final KafkaConsumer<K, V> consumer;

      public SaveOffsetsOnReblance(KafkaConsumer<K, V> consumer) {
        this.consumer = consumer;
      }

      @Override
      public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        database.commitTx(); // save to database
      }

      @Override
      public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
          // 定位到数据库中存储的偏移量处
          consumer.seek(partition, database.getOffset(partition).offset());
        }
      }
    }

    public void play() {

      consumer.subscribe(topics, new SaveOffsetsOnReblance(consumer));
      consumer.poll(0); // poll immediately

      for (TopicPartition partition : consumer.assignment()) {
        consumer.seek(partition, database.getOffset(partition).offset());
      }

      while (true) {
        ConsumerRecords<K, V> records = consumer.poll(100l);
        for (ConsumerRecord<K, V> record : records) {
          this.process(record);
          database.updateOffset(// save to database cache
            new TopicPartition(record.topic(), record.partition()),//
            new OffsetAndMetadata(record.offset()));
        }

        database.commitTx();// save to database
      }
    }

    private void process(ConsumerRecord<K, V> record) {
      LOG.info("processing ConsumerRecord=" + Kafkas.asString(record));
      database.storeRecord(record);
    }
  }

  /** 模拟数据库服务: use synchronized for atomic operations. */
  static class MockingDataBase<K, V> {

    private final Map<TopicPartition, OffsetAndMetadata> offsets = new ConcurrentHashMap<>();
    private final LinkedList<ConsumerRecord<K, V>> records = new LinkedList<>();

    public synchronized void storeRecord(ConsumerRecord<K, V> record) {
      records.add(record);
    }

    public synchronized void updateOffset(TopicPartition partition,
        OffsetAndMetadata offsetAndMetadata) {
      offsets.put(partition, offsetAndMetadata);
    }

    public OffsetAndMetadata getOffset(TopicPartition topicPartition) {
      return offsets.get(topicPartition);
    }

    /** 将数据和偏移量保存作为一个原子操作. */
    public synchronized void commitTx() {
      LOG.info("Size: offsets={}, records={}", offsets.size(), records.size());
      if (!offsets.isEmpty() || !records.isEmpty()) {
        LOG.info("commit tx");
      }
    }
  }

}
