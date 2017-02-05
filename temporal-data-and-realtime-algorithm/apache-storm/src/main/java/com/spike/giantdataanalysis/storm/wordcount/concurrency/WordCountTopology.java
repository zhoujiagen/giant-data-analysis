package com.spike.giantdataanalysis.storm.wordcount.concurrency;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.spike.giantdataanalysis.storm.wordcount.WordCountCountWordsBolt;
import com.spike.giantdataanalysis.storm.wordcount.WordCountReportBolt;
import com.spike.giantdataanalysis.storm.wordcount.WordCountSentenceSpout;
import com.spike.giantdataanalysis.storm.wordcount.WordCountSplitSentenceBolt;

/**
 * 单词计数拓扑结构定义
 * @author zhoujiagen
 */
public class WordCountTopology {

  // 这个名字还有限制, Topology name cannot contain any of the following: #{"." "/" ":" "\\"}
  public static final String NAME = WordCountTopology.class.getSimpleName();

  public static final StormTopology topology() {

    WordCountSentenceSpout sentenceSpout = new WordCountSentenceSpout();
    WordCountSplitSentenceBolt splitSentenceBolt = new WordCountSplitSentenceBolt();
    WordCountCountWordsBolt countWordsBolt = new WordCountCountWordsBolt();
    WordCountReportBolt reportBolt = new WordCountReportBolt();

    TopologyBuilder builder = new TopologyBuilder();

    // DIFF: 增加Executor
    builder.setSpout(WordCountSentenceSpout.ID, sentenceSpout, 2);

    // WordCountSentenceSpout -> WordCountSplitSentenceBolt
    // DIFF: 增加Executor和Task
    // Executor数量为2, Task数量为4, 即2 tasks per executor
    builder.setBolt(WordCountSplitSentenceBolt.ID, splitSentenceBolt, 2)//
        .setNumTasks(4)//
        .shuffleGrouping(WordCountSentenceSpout.ID);

    // WordCountSplitSentenceBolt -> WordCountCountWordsBolt
    // DIFF: 增加Executor
    // Executor数量为4, 即1 task per executor
    builder.setBolt(WordCountCountWordsBolt.ID, countWordsBolt, 4)//
        .fieldsGrouping(WordCountSplitSentenceBolt.ID,
          new Fields(WordCountSplitSentenceBolt.FIELD_WORD));

    // WordCountCountWordsBolt -> WordCountReportBolt
    builder.setBolt(WordCountReportBolt.ID, reportBolt)//
        .globalGrouping(WordCountCountWordsBolt.ID);

    return builder.createTopology();
  }
}
