package com.spike.giantdataanalysis.storm.wordcount.reliable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * <pre>
 * 语句生成Spout
 * 
 * emit tuple sample: 
 *  {"sentence": "my dog has fleas"}
 * </pre>
 * @author zhoujiagen
 */
public class WordCountSentenceSpout extends BaseRichSpout {
  private static final long serialVersionUID = 4415886809114690971L;

  public static final String ID = WordCountSentenceSpout.class.getSimpleName();
  public static final String FIELD_SENTENCE = "sentence";

  // DIFF: 记录待确认tuple
  private ConcurrentHashMap<UUID, Values> pending;

  private SpoutOutputCollector collector;

  @SuppressWarnings("rawtypes")
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;
    this.pending = new ConcurrentHashMap<UUID, Values>();
  }

  @Override
  public void nextTuple() {
    Values values = new Values(SentenceStream.next());
    UUID msgId = UUID.randomUUID();
    this.pending.put(msgId, values);
    // DIFF: 带消息ID的发射
    this.collector.emit(values, msgId);
    Utils.sleep(1);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields(FIELD_SENTENCE));
  }

  // DIFF: 收到成功确认时的处理
  @Override
  public void ack(Object msgId) {
    this.pending.remove(msgId);
  }

  // DIFF: 未收到成功确认时的处理
  @Override
  public void fail(Object msgId) {
    // 重试发射
    this.collector.emit(this.pending.get(msgId), msgId);
  }
}
