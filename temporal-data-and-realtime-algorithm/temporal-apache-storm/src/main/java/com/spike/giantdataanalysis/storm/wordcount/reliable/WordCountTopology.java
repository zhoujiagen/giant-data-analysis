package com.spike.giantdataanalysis.storm.wordcount.reliable;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

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

    builder.setSpout(WordCountSentenceSpout.ID, sentenceSpout);

    // WordCountSentenceSpout -> WordCountSplitSentenceBolt
    builder.setBolt(WordCountSplitSentenceBolt.ID, splitSentenceBolt)//
        .shuffleGrouping(WordCountSentenceSpout.ID);

    // WordCountSplitSentenceBolt -> WordCountCountWordsBolt
    builder.setBolt(WordCountCountWordsBolt.ID, countWordsBolt)//
        .fieldsGrouping(WordCountSplitSentenceBolt.ID,
          new Fields(WordCountSplitSentenceBolt.FIELD_WORD));

    // WordCountCountWordsBolt -> WordCountReportBolt
    builder.setBolt(WordCountReportBolt.ID, reportBolt)//
        .globalGrouping(WordCountCountWordsBolt.ID);

    return builder.createTopology();
  }
}
