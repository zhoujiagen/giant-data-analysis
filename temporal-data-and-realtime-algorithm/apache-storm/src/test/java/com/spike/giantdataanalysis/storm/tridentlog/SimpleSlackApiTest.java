package com.spike.giantdataanalysis.storm.tridentlog;

import java.io.IOException;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

/**
 * <pre>
 * A Slack bot
 * 
 * https://github.com/Ullink/simple-slack-api
 * </pre>
 * @author zhoujiagen
 */
public class SimpleSlackApiTest {
  public static void main(String[] args) throws IOException {
    /**
     * <pre>
     * https://api.slack.com/bot-users
     * storm-alert-bot
     * xoxb-138595343058-qoHQm5MQKMO0RQybTSvVXVBh
     * 
     * NEED UPDATE!!!
     * </pre>
     */
    SlackSession session =
        SlackSessionFactory
            .createWebSocketSlackSession("xoxb-138595343058-hZJXobxwCNuo74wH3AVY7NwD");
    session.connect();

    // get a channel to send message
    SlackChannel channel = session.findChannelByName("thoughts");
    session.sendMessage(channel, "Hey there");

    SlackUser user = session.findUserByUserName("zhoujiagen");
    session.sendMessageToUser(user, "Hi, how are you", null);
  }

}
