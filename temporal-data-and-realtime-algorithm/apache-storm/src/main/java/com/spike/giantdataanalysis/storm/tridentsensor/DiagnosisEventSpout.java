package com.spike.giantdataanalysis.storm.tridentsensor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.storm.guava.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.TridentCollector;
import storm.trident.spout.ITridentSpout;
import storm.trident.topology.TransactionAttempt;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;

/**
 * 诊断事件Spout: 输出字段为{@link DiagnosisEventSpout#FIELD_EVENT}
 * @author zhoujiagen
 */
public class DiagnosisEventSpout implements ITridentSpout<Long> {
  private static final long serialVersionUID = -1548400866223033600L;

  public static final String FIELD_EVENT = "event";

  private BatchCoordinator<Long> coordinator = new DefaultCoordinator();
  private Emitter<Long> emitter = new DiagnosisEventEmitter();

  @SuppressWarnings("rawtypes")
  @Override
  public BatchCoordinator<Long> getCoordinator(String txStateId, Map conf, TopologyContext context) {
    return coordinator;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Emitter<Long> getEmitter(String txStateId, Map conf, TopologyContext context) {
    return emitter;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Map getComponentConfiguration() {
    return null;
  }

  @Override
  public Fields getOutputFields() {
    return new Fields(FIELD_EVENT);
  }

  // ==============================================================================支持类
  /**
   * <pre>
   * 批次协同逻辑处理
   * 
   * 这里不关心批次中的元数据, 使用了Long.
   * </pre>
   */
  public static class DefaultCoordinator implements BatchCoordinator<Long>, Serializable {
    private static final long serialVersionUID = -6146067422091991042L;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCoordinator.class);

    @Override
    public Long initializeTransaction(long txid, Long prevMetadata, Long currMetadata) {
      LOG.info("Initialize Transaction[{}]", txid);
      return null;
    }

    @Override
    public void success(long txid) {
      LOG.info("Successful Transaction[{}]", txid);
    }

    @Override
    public boolean isReady(long txid) {
      return true;
    }

    @Override
    public void close() {
      LOG.info("Close {}", DefaultCoordinator.class.getSimpleName());
    }
  }

  /**
   * 事件发出逻辑处理
   */
  public static class DiagnosisEventEmitter implements Emitter<Long>, Serializable {
    private static final long serialVersionUID = -7462570457193439871L;
    private static final Logger LOG = LoggerFactory.getLogger(DiagnosisEventEmitter.class);

    // 成功事务数量计数
    private AtomicLong successfulTransactions = new AtomicLong(1l);

    private static final int NUM_OF_EVENTS = 10000; // 发出事件数量

    @Override
    public void emitBatch(TransactionAttempt tx, Long coordinatorMeta, TridentCollector collector) {
      for (int i = 0; i < NUM_OF_EVENTS; i++) {
        List<Object> events = Lists.newArrayList();

        double lat = new Double(-30 + (int) (Math.random() * 75));
        double lng = new Double(-120 + (int) (Math.random() * 70));
        long time = System.currentTimeMillis();
        String diagnosisCode = new Integer(320 + (int) (Math.random() * 7)).toString();
        DiagnosisEvent event = new DiagnosisEvent(lat, lng, time, diagnosisCode);
        events.add(event);

        collector.emit(events);
      }

    }

    @Override
    public void success(TransactionAttempt tx) {
      LOG.info("Successful Transaction Attempt[{}]", tx.getId());
      successfulTransactions.incrementAndGet();
    }

    @Override
    public void close() {
      LOG.info("Close {}", DiagnosisEventEmitter.class.getSimpleName());
    }

  }

  /**
   * 事件抽象: 经纬度, 时间, ICD-9-CM诊断码
   */
  public static class DiagnosisEvent implements Serializable {
    private static final long serialVersionUID = 304081633907383473L;

    private double lat;
    private double lng;
    private long time;
    private String diagnosisCode;

    public DiagnosisEvent(double lat, double lng, long time, String diagnosisCode) {
      this.time = time;
      this.lat = lat;
      this.lng = lng;
      this.diagnosisCode = diagnosisCode;
    }

    public double getLat() {
      return lat;
    }

    public double getLng() {
      return lng;
    }

    public long getTime() {
      return time;
    }

    public String getDiagnosisCode() {
      return diagnosisCode;
    }

    @Override
    public String toString() {
      return "DiagnosisEvent [lat=" + lat + ", lng=" + lng + ", time=" + time + ", diagnosisCode="
          + diagnosisCode + "]";
    }
  }

}
