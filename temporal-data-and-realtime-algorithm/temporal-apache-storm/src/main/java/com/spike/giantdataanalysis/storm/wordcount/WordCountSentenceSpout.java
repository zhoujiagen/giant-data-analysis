package com.spike.giantdataanalysis.storm.wordcount;

import java.util.Map;

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

  private SpoutOutputCollector collector;

  @SuppressWarnings("rawtypes")
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;
  }

  @Override
  public void nextTuple() {
    this.collector.emit(new Values(SentenceStream.next()));
    Utils.sleep(1);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields(FIELD_SENTENCE));
  }

}
