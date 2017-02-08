package com.spike.giantdataanalysis.storm.tridentlog;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

public class SlackBotFunction extends BaseFunction {
  private static final long serialVersionUID = -1216846823166001988L;
  private static final Logger LOG = LoggerFactory.getLogger(SlackBotFunction.class);

  public static final String PARAM_TOKEN = "token";

  private SlackSession session;

  @SuppressWarnings("rawtypes")
  @Override
  public void prepare(Map conf, TridentOperationContext context) {
    LOG.debug("Prepare: {}", conf);
    super.prepare(conf, context);

    String token = (String) conf.get(PARAM_TOKEN);
    try {
      session = SlackSessionFactory.createWebSocketSlackSession(token);
      session.connect();
    } catch (Exception e) {
      LOG.warn("Error initializing Slack", e);
    }
  }

  public void execute(TridentTuple tuple, TridentCollector collector) {

    StringBuilder sb = new StringBuilder();
    sb.append("On " + new Date(tuple.getLongByField("timestamp")) + " ");
    sb.append("the application \"" + tuple.getStringByField("logger") + "\" ");
    sb.append("changed alert state based on a threshold of " + tuple.getDoubleByField("threshold")
        + ".\n");
    sb.append("The last value was " + tuple.getDoubleByField("average") + "\n");
    sb.append("The last message was \"" + tuple.getStringByField("message") + "\"");

    SlackUser user = session.findUserByUserName("zhoujiagen");
    session.sendMessageToUser(user, sb.toString(), null);
  }

}
