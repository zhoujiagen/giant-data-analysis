package com.spike.giantdataanalysis.storm.tridentsensor;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.utils.Utils;

import com.spike.giantdataanalysis.storm.wordcount.WordCountTopology;

public class OutbreakDetectionApplication {
  public static void main(String[] args) {
    Config config = new Config();

    // 本地模式
    LocalCluster localCluster = new LocalCluster();

    // 提交拓扑
    localCluster.submitTopology(OutbreakDetectionTopology.NAME, config,
      OutbreakDetectionTopology.topology());

    Utils.sleep(20 * 1000L);// 20s

    localCluster.killTopology(WordCountTopology.NAME);
    localCluster.shutdown();
  }
}
