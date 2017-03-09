package com.spike.giantdataanalysis.storm.wordcount.reliable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * <pre>
 * 上报Bolt
 * 
 * receive tuple sample:
 *  {"word": "my", "count": x}
 *  {"word": "dog", "count": y}
 *  
 * emit tuple sample: 
 *  None
 * </pre>
 * @author zhoujiagen
 */
public class WordCountReportBolt extends BaseRichBolt {
  private static final long serialVersionUID = -772763033176458429L;

  public static final String ID = WordCountReportBolt.class.getSimpleName();

  private HashMap<String, Long> wordCountMap;
  // DIFF: 添加collector以确认
  private OutputCollector collector;

  @SuppressWarnings("rawtypes")
  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.wordCountMap = new HashMap<String, Long>();
    this.collector = collector;
  }

  @Override
  public void execute(Tuple input) {
    String word = input.getStringByField(WordCountCountWordsBolt.FIELD_WORD);
    Long count = input.getLongByField(WordCountCountWordsBolt.FIELD_COUNT);
    this.wordCountMap.put(word, count);

    // DIFF: 确认
    this.collector.ack(input);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // do nothing
  }

  /** WARNING: 在集群上运行时, {@link #cleanup()}不能保证被执行 */
  @Override
  public void cleanup() {
    System.out.println("=============================");
    System.out.println("Result");
    System.out.println("=============================");

    List<String> words = new ArrayList<String>(wordCountMap.keySet());
    Collections.sort(words); // 排序

    for (String word : words) {
      System.out.printf("%15s:\t%d\n", word, wordCountMap.get(word));
      // System.out.println(word + ": " + wordCountMap.get(word));
    }
    System.out.println("=============================");
  }

}
