package com.spike.giantdataanalysis.storm.tridentlog;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

public class LogAnalysisApplication {

  public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
    Config conf = new Config();
    conf.put(SlackBotFunction.PARAM_TOKEN, "xoxb-138595343058-hZJXobxwCNuo74wH3AVY7NwD");
    conf.put(SlackBotFunction.PARAM_USE, "zhoujiagen");
    conf.setMaxSpoutPending(5);

    if (args.length == 0) {
      // 需要引入kafka依赖
      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology(LogAnalysisTopology.NAME, conf, LogAnalysisTopology.topology());
    } else {
      conf.setNumWorkers(3);
      StormSubmitter.submitTopology(args[0], conf, LogAnalysisTopology.topology());
    }
  }
}
