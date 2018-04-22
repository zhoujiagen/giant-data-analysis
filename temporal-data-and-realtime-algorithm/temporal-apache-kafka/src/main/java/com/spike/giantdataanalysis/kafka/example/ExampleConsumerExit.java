package com.spike.giantdataanalysis.kafka.example;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.kafka.support.Kafkas;

/**
 * 消费者退出.
 * @author zhoujiagen
 */
public class ExampleConsumerExit {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleConsumerExit.class);

  public static void main(String[] args) {
    Thread mainThread = Thread.currentThread();

    KafkaConsumer<String, String> consumer = Kafkas.<String, String> consumer(new Properties());
    try {

      while (true) {
        ConsumerRecords<String, String> records = Kafkas.poll(consumer, 1000l);
        for (ConsumerRecord<String, String> record : records) {
          LOG.info(Kafkas.asString(record));
        }
        for (TopicPartition partition : consumer.assignment()) {
          LOG.info("commit offset at position={}", Kafkas.position(consumer, partition));
          Kafkas.commitSync(consumer);
        }
      }

    } catch (WakeupException e) {
      // just ignore
    } finally {
      LOG.info("close consumer...");
      consumer.close();
    }

    // 触发退出消费循环
    // 或者 Runtime.getRuntime().addShutdownHook(Kafkas.wakeup(consumer));
    Thread wakeupThread = Kafkas.wakeup(consumer);
    wakeupThread.start();

    try {
      mainThread.join();
    } catch (InterruptedException e) {
      LOG.error("something wrong", e);
    }

  }
}
