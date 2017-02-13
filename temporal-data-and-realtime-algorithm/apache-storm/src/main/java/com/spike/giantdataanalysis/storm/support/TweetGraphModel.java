package com.spike.giantdataanalysis.storm.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Tweet的图模型
 * 
 * 图数据模型
 * 节点: User, Word, URL, Hashtag
 * 关系: mentions_user, retweets_user, follows_user, mentions_hashtag, uses_word, mentions_url
 * </pre>
 * @author zhoujiagen
 */
public class TweetGraphModel {
  public static final Logger LOG = LoggerFactory.getLogger(TweetGraphModel.class);

  // 节点
  public static final String V_USER = "user";
  public static final String V_WORD = "word";
  public static final String V_URL = "url";
  public static final String V_HASHTAG = "hashtag";
  // 节点属性
  public static final String V_USER_USER = "user";
  public static final String V_USER_NAME = "name";
  public static final String V_HASHTAG_TEXT = "text";

  // 边
  public static final String E_MENTIONS_USER = "mentions_user";
  public static final String E_RETWEETS_USER = "retweets_user";
  public static final String E_FOLLOWS_USER = "follows_user";
  public static final String E_MENTIONS_HASHTAG = "mentions_hashtag";
  public static final String E_USES_WORD = "uses_word";
  public static final String E_MENTIONS_URL = "mentions_url";
  // 边属性
  public static final String E_MENTIONS_HASHTAG_TIMESTAMP = "timestamp";

  // JSON数据模型
  public static final String JSON_KEY_USER = "user";
  public static final String JSON_KEY_NAME = "name";
  public static final String JSON_KEY_HASHTAGS = "hashtags";

  // 模拟Tweet日志流
  public static final void mockTweetStreamLog() {
    String filepath =
        "/Users/zhang/workspace/Github_local/python-playground/src/com/spike/text/nltk/twitter/extracted_tweets.txt";

    Random random = new Random(new Date().getTime());
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath));) {
      String line = null;
      int i = 0;
      while ((line = reader.readLine()) != null) {
        LOG.info(line);

        // for debug
        // JSONObject json = (JSONObject) JSONValue.parse(line);
        // if (json != null) {
        // System.err.println(json.get("user"));
        // System.err.println(json.get("name"));
        // System.err.println(json.get("hashtags"));
        // } else {
        // System.err.println(line);
        // }
        
        Thread.sleep(random.nextInt(10) * 100L);
        i++;
        if (i == 10) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    mockTweetStreamLog();
  }
}
