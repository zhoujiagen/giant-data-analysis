package com.spike.giantdataanalysis.storm.tridentlog;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

public class LogAnalysisApplication {

  public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
    Config conf = new Config();
    conf.put(SlackBotFunction.PARAM_TOKEN, "xoxb-138595343058-qoHQm5MQKMO0RQybTSvVXVBh");
    conf.setMaxSpoutPending(5);

    if (args.length == 0) {
      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology(LogAnalysisTopology.NAME, conf, LogAnalysisTopology.topology());
    } else {
      conf.setNumWorkers(3);
      StormSubmitter.submitTopology(args[0], conf, LogAnalysisTopology.topology());
    }
  }
}
