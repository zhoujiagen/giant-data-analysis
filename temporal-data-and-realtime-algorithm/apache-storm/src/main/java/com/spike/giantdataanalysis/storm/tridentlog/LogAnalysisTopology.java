package com.spike.giantdataanalysis.storm.tridentlog;

import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;

public class LogAnalysisTopology {
  public static final String NAME = LogAnalysisTopology.class.getSimpleName();

  public static StormTopology topology() {
    TridentTopology topology = new TridentTopology();

    // REF: http://storm.apache.org/releases/0.9.7/storm-kafka.html
    BrokerHosts hosts = new ZkHosts("localhost");
    String topic = "log-analysis";
    TridentKafkaConfig config = new TridentKafkaConfig(hosts, topic);
    config.scheme = new SchemeAsMultiScheme(new StringScheme());
    config.startOffsetTime = -1;
    OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(config);

    String txId = "kafka-stream";
    @SuppressWarnings("unused")
    Stream inputStream = topology.newStream(txId, spout);

    return topology.build();
  }
}
