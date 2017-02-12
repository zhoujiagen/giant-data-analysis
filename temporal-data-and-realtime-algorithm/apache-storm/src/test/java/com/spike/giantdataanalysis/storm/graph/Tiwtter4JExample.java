package com.spike.giantdataanalysis.storm.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * <pre>
 * Tiwtter4J示例
 * 
 * WARNING: 404 https://stream.twitter.com/1.1/statuses/filter.json
 * </pre>
 * @author zhoujiagen
 */
public class Tiwtter4JExample {
  private static final Logger LOG = LoggerFactory.getLogger(Tiwtter4JExample.class);

  public static void main(String[] args) throws TwitterException {
    // 发消息
    // Twitter twitter = new TwitterFactory().getInstance();
    // twitter.updateStatus("Hello, there.");

    // 接收流
    StatusListener listener = new SampleStatusListener();
    TwitterStream stream = new TwitterStreamFactory().getInstance();
    stream.addListener(listener);

    String track = "china";
    FilterQuery query = new FilterQuery().track(track);
    stream.filter(query);
  }

  // =============================================================================支持类
  public static class SampleStatusListener implements StatusListener {

    @Override
    public void onException(Exception ex) {
      LOG.error(ex.getMessage(), ex);
    }

    @Override
    public void onStatus(Status status) {
      LOG.info(status.toString());
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
      LOG.info(statusDeletionNotice.toString());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
      LOG.info(String.valueOf(numberOfLimitedStatuses));
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
      LOG.info(userId + ", " + upToStatusId);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
      LOG.info(warning.toString());
    }
  }

}
