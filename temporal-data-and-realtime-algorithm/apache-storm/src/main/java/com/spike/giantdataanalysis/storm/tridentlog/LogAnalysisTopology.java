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
import backtype.storm.tuple.Fields;

import com.spike.giantdataanalysis.storm.tridentlog.MovingAverageFunction.EWMA;

public class LogAnalysisTopology {
  public static final String NAME = LogAnalysisTopology.class.getSimpleName();

  public static final String KAFKA_TOPIC_NAME = "log-analysis";
  
  
  public static StormTopology topology() {
    TridentTopology topology = new TridentTopology();

    // Kafka Spout
    // REF: http://storm.apache.org/releases/0.9.7/storm-kafka.html
    BrokerHosts hosts = new ZkHosts("localhost");
    TridentKafkaConfig config = new TridentKafkaConfig(hosts, KAFKA_TOPIC_NAME);
    config.scheme = new SchemeAsMultiScheme(new StringScheme());
    config.startOffsetTime = -1;
    OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(config);

    String txId = "kafka-stream";
    Stream inputStream = topology.newStream(txId, spout);

    Fields jsonFields = new Fields("level", "timestamp", "message", "logger");
    EWMA ewma = new EWMA().sliding(1.0, EWMA.Time.MINUTES).withAlpha(EWMA.ONE_MINUTE_ALPHA);

    Stream stream =
        inputStream//
            // JsonProjectFunction
            .each(new Fields("str"), new JsonProjectFunction(jsonFields), jsonFields)//
            .project(jsonFields)//
            // MovingAverageFunction
            .each(new Fields("timestamp"), new MovingAverageFunction(ewma, EWMA.Time.MINUTES),
              new Fields("average"))//
            // ThresholdFilterFunction
            .each(new Fields("average"), new ThresholdFilterFunction(50d),
              new Fields("change", "threshold"));

    // BooleanFilter
    Stream filteredStream = stream.each(new Fields("change"), new BooleanFilter());
    // XMPPFunction
    filteredStream.each(filteredStream.getOutputFields(), new SlackBotFunction(), new Fields());

    return topology.build();
  }
}
