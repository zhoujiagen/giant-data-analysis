package com.spike.giantdataanalysis.storm.support;

import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import backtype.storm.spout.SchemeAsMultiScheme;

/**
 * Kafka工具类
 * @author zhoujiagen
 */
public final class Kafkas {

  /**
   * <pre>
   * Init Kafka Spout
   * REF: http://storm.apache.org/releases/0.9.7/storm-kafka.html
   * </pre>
   * @param zkHosts ZooKeeper连接串, 例如localhost:2188
   * @param topic 主题名称
   * @param startOffsetTime 读取位置: -1从尾部开始读取, -2从头部开始读取
   * @return
   * @see kafka.api.OffsetRequest.LatestTime()
   * @see kafka.api.OffsetRequest.EarliestTime()
   */
  public static OpaqueTridentKafkaSpout spout(String zkHosts, String topic, long startOffsetTime) {
    BrokerHosts hosts = new ZkHosts(zkHosts);

    TridentKafkaConfig config = new TridentKafkaConfig(hosts, topic);
    config.scheme = new SchemeAsMultiScheme(new StringScheme());
    config.startOffsetTime = startOffsetTime;

    OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(config);

    return spout;
  }
}
