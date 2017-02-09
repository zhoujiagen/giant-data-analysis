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
import com.spike.giantdataanalysis.storm.tridentlog.formatter.LogFormatter;

public class LogAnalysisTopology {
  public static final String NAME = LogAnalysisTopology.class.getSimpleName();

  public static final String KAFKA_TOPIC_NAME = "log-analysis";

  /**
   * <pre>
   * Init Kafka Spout
   * REF: http://storm.apache.org/releases/0.9.7/storm-kafka.html
   * </pre>
   * @return
   */
  public static OpaqueTridentKafkaSpout kafkaSpout() {
    BrokerHosts hosts = new ZkHosts("localhost:2188");

    TridentKafkaConfig config = new TridentKafkaConfig(hosts, KAFKA_TOPIC_NAME);
    config.scheme = new SchemeAsMultiScheme(new StringScheme());
    // 从kafka topic尾部开始读取
    config.startOffsetTime = -1;// see kafka.api.OffsetRequest.LatestTime()

    OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(config);

    return spout;
  }

  public static StormTopology topology() {
    TridentTopology topology = new TridentTopology();

    OpaqueTridentKafkaSpout spout = kafkaSpout();

    String txId = "kafka-stream";
    Stream inputStream = topology.newStream(txId, spout);

    Fields strField = new Fields(StringScheme.STRING_SCHEME_KEY);

    Fields levelField = new Fields(LogFormatter.FIELD_LEVEL);
    Fields timestampField = new Fields(LogFormatter.FIELD_TIMESTAMP);
    Fields messageField = new Fields(LogFormatter.FIELD_MESSAGE);
    Fields loggerField = new Fields(LogFormatter.FIELD_LOGGER);
    Fields jsonFields = new Fields(//
        levelField.get(0), timestampField.get(0), messageField.get(0), loggerField.get(0));

    Fields averageField = new Fields(MovingAverageFunction.FIELD_AVERAGE);

    Fields changeField = new Fields(ThresholdFilterFunction.FIELD_CHANGE);
    Fields thresholdField = new Fields(ThresholdFilterFunction.FIELD_THRESHOLD);
    Fields changeAndThresholdField = new Fields(changeField.get(0), thresholdField.get(0));

    Fields emptyField = new Fields();

    EWMA ewma = new EWMA().sliding(1.0, EWMA.Time.MINUTES).withAlpha(EWMA.ONE_MINUTE_ALPHA);

    Stream stream = inputStream//
        // JsonProjectFunction
        .each(strField, new JsonProjectFunction(jsonFields), jsonFields)//
        // project
        .project(jsonFields)//
        // MovingAverageFunction
        .each(timestampField, new MovingAverageFunction(ewma, EWMA.Time.MINUTES), averageField)//
        // ThresholdFilterFunction
        .each(averageField, new ThresholdFilterFunction(50d), changeAndThresholdField);

    // BooleanFilter
    Stream filteredStream = stream.each(changeField, new BooleanFilter());
    // SlackBotFunction
    filteredStream.each(filteredStream.getOutputFields(), new SlackBotFunction(), emptyField);

    return topology.build();
  }
}
