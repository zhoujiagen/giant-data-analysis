package com.spike.giantdataanalysis.storm.wordcount.production;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.utils.Utils;

import com.spike.giantdataanalysis.storm.wordcount.WordCountTopology;

/**
 * <pre>
 * 单词计数应用
 * 
 * 集群运行方式:
 * $ storm jar path-to-jar \
 *    com.spike.giantdataanalysis.storm.wordcount.production.WordCountApplication \
 *    WordCountTopology
 * </pre>
 * @author zhoujiagen
 */
public class WordCountApplication {
  public static void main(String[] args) {
    Config config = new Config();
    config.setNumWorkers(2);

    if (args.length == 0) {// 本地模式

      LocalCluster localCluster = new LocalCluster();
      // 提交拓扑
      localCluster.submitTopology(WordCountTopology.NAME, config, WordCountTopology.topology());

      Utils.sleep(10 * 1000L);// 10s

      localCluster.killTopology(WordCountTopology.NAME);
      localCluster.shutdown();

    } else { // 集群模式

      try {
        StormSubmitter.submitTopology(args[0], config, WordCountTopology.topology());
      } catch (AlreadyAliveException | InvalidTopologyException e) {
        e.printStackTrace();
      }

    }

  }
}
