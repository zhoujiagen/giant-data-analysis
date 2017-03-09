package com.spike.giantdataanalysis.storm.tridentlog;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

import com.spike.giantdataanalysis.storm.tridentlog.formatter.LogFormatter;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

public class SlackBotFunction extends BaseFunction {
  private static final long serialVersionUID = -1216846823166001988L;
  private static final Logger LOG = LoggerFactory.getLogger(SlackBotFunction.class);

  public static final String PARAM_TOKEN = "token";
  public static final String PARAM_USE = "user";

  // REF: https://github.com/Ullink/simple-slack-api
  private SlackSession session;
  private SlackUser user;

  @SuppressWarnings("rawtypes")
  @Override
  public void prepare(Map conf, TridentOperationContext context) {
    LOG.debug("Prepare: {}", conf);
    super.prepare(conf, context);

    String token = (String) conf.get(PARAM_TOKEN);
    String userName = (String) conf.get(PARAM_USE);
    try {
      session = SlackSessionFactory.createWebSocketSlackSession(token);
      session.connect();

      user = session.findUserByUserName(userName);
    } catch (Exception e) {
      LOG.warn("Error initializing Slack", e);
    }
  }

  public void execute(TridentTuple tuple, TridentCollector collector) {

    StringBuilder sb = new StringBuilder();
    sb.append("On " + new Date(tuple.getLongByField(LogFormatter.FIELD_TIMESTAMP)) + " ");
    sb.append("the application \"" + tuple.getStringByField(LogFormatter.FIELD_LOGGER) + "\" ");
    sb.append("changed alert state based on a threshold of "
        + tuple.getDoubleByField(ThresholdFilterFunction.FIELD_THRESHOLD) + ".\n");
    sb.append("The last value was " + tuple.getDoubleByField(MovingAverageFunction.FIELD_AVERAGE)
        + "\n");
    sb.append("The last message was \"" + tuple.getStringByField(LogFormatter.FIELD_MESSAGE) + "\"");

    if (session != null && user != null) {
      session.sendMessageToUser(user, sb.toString(), null);
    } else {
      LOG.warn(sb.toString());
    }
  }

}
