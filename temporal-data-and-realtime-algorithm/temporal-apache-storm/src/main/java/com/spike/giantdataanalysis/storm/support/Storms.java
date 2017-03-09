package com.spike.giantdataanalysis.storm.support;

import java.util.Map;

import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Storm工具类
 * @author zhoujiagen
 */
public final class Storms {

  /**
   * Json投影函数
   * @see BaseFunction
   */
  public static class JsonProjectFunction extends BaseFunction {
    private static final long serialVersionUID = 9025844207660740588L;
    private static final Logger LOG = LoggerFactory.getLogger(JsonProjectFunction.class);

    private Fields jsonFields;

    public JsonProjectFunction(Fields jsonFields) {
      this.jsonFields = jsonFields;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(TridentTuple tuple, TridentCollector collector) {
      String json = tuple.getString(0); // str
      LOG.debug("JSON={}", json);

      // use org.json.simple.JSONValue
      Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
      if (map != null) {
        Values values = new Values();
        for (int i = 0, len = jsonFields.size(); i < len; i++) {
          values.add(map.get(jsonFields.get(i))); // value maybe null
        }
        collector.emit(values);
      }
    }

  }
}
