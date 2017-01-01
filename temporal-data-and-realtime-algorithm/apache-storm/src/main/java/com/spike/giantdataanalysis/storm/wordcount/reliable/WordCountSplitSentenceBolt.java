package com.spike.giantdataanalysis.storm.wordcount.reliable;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * <pre>
 * 语句分割Bolt
 * 
 * receive tuple sample:
 *  {"sentence": "my dog has fleas"}
 *  
 * emit tuple sample: 
 *  {"word": "my"}
 *  {"word": "dog"}
 *  {"word": "has"}
 *  {"word": "fleas"}
 * </pre>
 * @author zhoujiagen
 */
public class WordCountSplitSentenceBolt extends BaseRichBolt {
  private static final long serialVersionUID = 1117348316397891510L;

  public static final String ID = WordCountSplitSentenceBolt.class.getSimpleName();
  public static final String FIELD_WORD = "word";

  private OutputCollector collector;

  @SuppressWarnings("rawtypes")
  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;
  }

  @Override
  public void execute(Tuple input) {
    String sentence = input.getStringByField(WordCountSentenceSpout.FIELD_SENTENCE);
    String[] words = sentence.split(" ");
    for (String word : words) {
      // DIFF: 锚定
      this.collector.emit(input, new Values(word));
    }
    // DIFF: 确认
    this.collector.ack(input);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields(FIELD_WORD));
  }

}
