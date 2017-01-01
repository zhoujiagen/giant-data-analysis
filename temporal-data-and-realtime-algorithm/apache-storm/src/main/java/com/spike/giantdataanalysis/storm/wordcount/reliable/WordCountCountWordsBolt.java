package com.spike.giantdataanalysis.storm.wordcount.reliable;

import java.util.HashMap;
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
 * 单词计数Bolt
 * 
 * receive tuple sample:
 *  {"word": "dog"}
 *  {"word": "dog"}
 *  {"word": "dog"}
 *  {"word": "dog"}
 *  
 * emit tuple sample: 
 *  {"word": "dog", "count": 5}
 * </pre>
 * @author zhoujiagen
 */
public class WordCountCountWordsBolt extends BaseRichBolt {
  private static final long serialVersionUID = 3505328365169652769L;

  public static final String ID = WordCountCountWordsBolt.class.getSimpleName();
  public static final String FIELD_WORD = "word";
  public static final String FIELD_COUNT = "count";

  private OutputCollector collector;

  // 内部单词-数量映射, 实现了Serializable接口
  private HashMap<String, Long> wordCountMap;

  @SuppressWarnings("rawtypes")
  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;
    wordCountMap = new HashMap<String, Long>();
  }

  @Override
  public void execute(Tuple input) {
    String word = input.getStringByField(WordCountSplitSentenceBolt.FIELD_WORD);
    Long count = this.wordCountMap.get(word);
    if (count == null) count = 0L;
    count++;
    this.wordCountMap.put(word, count);

    // DIFF: 锚定
    this.collector.emit(input, new Values(word, count));
    // DIFF: 确认
    this.collector.ack(input);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields(FIELD_WORD, FIELD_COUNT));
  }

}
