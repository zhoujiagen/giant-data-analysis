package com.spike.giantdataanalysis.storm.tridentlog;

import java.util.Map;

import org.json.simple.JSONValue;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class JsonProjectFunction extends BaseFunction {
  private static final long serialVersionUID = 9025844207660740588L;

  private Fields jsonFields;

  public JsonProjectFunction(Fields jsonFields) {
    this.jsonFields = jsonFields;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
    String json = (String) tuple.getValue(0); // str

    // use org.json.simple.JSONValue
    Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
    Values values = new Values();
    for (int i = 0, len = jsonFields.size(); i < len; i++) {
      values.add(map.get(jsonFields.get(i))); // value maybe null
    }

    collector.emit(values);
  }

}
