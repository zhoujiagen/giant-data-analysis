package com.spike.giantdataanalysis.storm.tridentsensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.IMetricsContext;
import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.state.map.IBackingMap;
import storm.trident.state.map.NonTransactionalMap;

/**
 * 简单的{@link StateFactory}的实现
 * @author zhoujiagen
 * @see NonTransactionalMap
 */
public class OutbreakTrendFactory implements StateFactory {

  private static final long serialVersionUID = 6462654444418657960L;

  @SuppressWarnings("rawtypes")
  @Override
  public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
    return new OutbreakTrendState(new OutbreakTrendBackingMap());
  }

  // ==============================================================================支持类

  /**
   * 状态实现
   */
  public static class OutbreakTrendState extends NonTransactionalMap<Long> {
    protected OutbreakTrendState(IBackingMap<Long> backing) {
      super(backing);
    }
  }

  /**
   * 内存中存储实现
   */
  public static class OutbreakTrendBackingMap implements IBackingMap<Long> {
    private static final Logger LOG = LoggerFactory.getLogger(OutbreakTrendBackingMap.class);

    Map<String, Long> storage = new ConcurrentHashMap<String, Long>();// 内存中存储, 测试用

    @Override
    public List<Long> multiGet(List<List<Object>> keys) {
      List<Long> values = new ArrayList<Long>();
      for (List<Object> key : keys) {
        Long value = storage.get(key.get(0));
        if (value == null) {
          values.add(new Long(0));
        } else {
          values.add(value);
        }
      }
      return values;
    }

    @Override
    public void multiPut(List<List<Object>> keys, List<Long> vals) {
      for (int i = 0; i < keys.size(); i++) {
        LOG.info("Persisting [" + keys.get(i).get(0) + "] ==> [" + vals.get(i) + "]");
        storage.put((String) keys.get(i).get(0), vals.get(i));
      }
    }
  }

}
