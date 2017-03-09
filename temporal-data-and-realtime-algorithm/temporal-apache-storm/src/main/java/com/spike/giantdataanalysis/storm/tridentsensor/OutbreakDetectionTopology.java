package com.spike.giantdataanalysis.storm.tridentsensor;

import storm.trident.Stream;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.fluent.GroupedStream;
import storm.trident.operation.CombinerAggregator;
import storm.trident.operation.builtin.Count;
import storm.trident.state.StateFactory;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

public class OutbreakDetectionTopology {
  public static final String NAME = OutbreakDetectionTopology.class.getSimpleName();

  // 使用流式API
  public static StormTopology topology() {
    TridentTopology topology = new TridentTopology();

    // 准备流处理中使用的字段
    Fields eventField = new Fields(DiagnosisEventSpout.FIELD_EVENT);
    Fields cityField = new Fields(CityAssignment.FIELD_CITY);
    Fields eventAndCityFields = new Fields(eventField.get(0), cityField.get(0));
    Fields hourField = new Fields(HourAssignment.FIELD_HOUR);
    Fields groupKeyField = new Fields(HourAssignment.FIELD_GROUP_KEY);
    Fields hourAndGroupKeyFields = new Fields(hourField.get(0), groupKeyField.get(0));
    Fields alertField = new Fields(OutbreakDetector.FIELD_ALERT);
    Fields emptyField = new Fields();

    // 从Spout获取输入流
    DiagnosisEventSpout spout = new DiagnosisEventSpout();
    String txId = DiagnosisEventSpout.FIELD_EVENT;
    Stream inputStream = topology.newStream(txId, spout);

    StateFactory stateFactory = new OutbreakTrendFactory();
    CombinerAggregator<Long> agg = new Count();
    Fields countField = new Fields("count");
    Fields groupKeyAndCountFields = new Fields(groupKeyField.get(0), countField.get(0));

    inputStream
    // DiseaseFilter
        .each(eventField, new DiseaseFilter())
        // CityAssignment
        .each(eventField, new CityAssignment(), cityField)
        // HourAssignment
        .each(eventAndCityFields, new HourAssignment(), hourAndGroupKeyFields)

        // 汇总
        .groupBy(groupKeyField)
        // 持久化
        .persistentAggregate(stateFactory, agg, countField)
        //
        .newValuesStream()

        // OutbreakDetector
        .each(groupKeyAndCountFields, new OutbreakDetector(), alertField)
        // DispatchAlert
        .each(alertField, new DispatchAlert(), emptyField);

    return topology.build();
  }

  // 不使用流式API
  public static StormTopology topology2() {
    TridentTopology topology = new TridentTopology();

    // 准备流处理中使用的字段
    Fields eventField = new Fields(DiagnosisEventSpout.FIELD_EVENT);
    Fields cityField = new Fields(CityAssignment.FIELD_CITY);
    Fields eventAndCityFields = new Fields(eventField.get(0), cityField.get(0));

    Fields hourField = new Fields(HourAssignment.FIELD_HOUR);
    Fields groupKeyField = new Fields(HourAssignment.FIELD_GROUP_KEY);
    Fields hourAndGroupKeyFields = new Fields(hourField.get(0), groupKeyField.get(0));

    Fields alertField = new Fields(OutbreakDetector.FIELD_ALERT);
    Fields emptyField = new Fields();

    // 从Spout获取输入流
    DiagnosisEventSpout spout = new DiagnosisEventSpout();
    String txId = DiagnosisEventSpout.FIELD_EVENT;
    Stream inputStream = topology.newStream(txId, spout);

    // DiseaseFilter
    Stream filteredStream = //
        inputStream.each(eventField, new DiseaseFilter());

    // CityAssignment
    Stream cityAssignedStream = //
        filteredStream.each(eventField, new CityAssignment(), cityField);

    // HourAssignment
    Stream hourAssignedStream = //
        cityAssignedStream.each(eventAndCityFields, new HourAssignment(), hourAndGroupKeyFields);

    // 汇总
    GroupedStream groupedStream = //
        hourAssignedStream.groupBy(groupKeyField);

    // 持久化
    StateFactory stateFactory = new OutbreakTrendFactory();
    CombinerAggregator<Long> agg = new Count();
    Fields countField = new Fields("count");
    TridentState state = groupedStream.persistentAggregate(stateFactory, agg, countField);
    Stream persistedStream = state.newValuesStream();

    // OutbreakDetector
    Fields groupKeyAndCountFields = new Fields(groupKeyField.get(0), countField.get(0));
    Stream detectedStream =
        persistedStream.each(groupKeyAndCountFields, new OutbreakDetector(), alertField);

    // DispatchAlert
    @SuppressWarnings("unused")
    Stream alteredStream = detectedStream.each(alertField, new DispatchAlert(), emptyField);

    return topology.build();
  }
}
