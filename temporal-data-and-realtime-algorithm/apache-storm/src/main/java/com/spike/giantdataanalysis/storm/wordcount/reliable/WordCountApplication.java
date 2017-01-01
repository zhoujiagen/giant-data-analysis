package com.spike.giantdataanalysis.storm.wordcount.reliable;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.utils.Utils;

/**
 * 单词计数应用
 * @author zhoujiagen
 */
public class WordCountApplication {
  public static void main(String[] args) {
    Config config = new Config();

    // 本地模式
    LocalCluster localCluster = new LocalCluster();

    // 提交拓扑
    localCluster.submitTopology(WordCountTopology.NAME, config, WordCountTopology.topology());

    Utils.sleep(10 * 1000L);// 10s

    localCluster.killTopology(WordCountTopology.NAME);
    localCluster.shutdown();
  }
}
