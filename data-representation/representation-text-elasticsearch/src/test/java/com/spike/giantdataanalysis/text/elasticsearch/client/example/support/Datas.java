package com.spike.giantdataanalysis.text.elasticsearch.client.example.support;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成数据的工具类
 * @author zhoujiagen
 */
public final class Datas {
  private static final Logger LOG = LoggerFactory.getLogger(Datas.class);

  public static void main(String[] args) {
    TwitterTweet();
  }

  /**
   * 生成index[twitter], type[tweet]的数据
   */
  public static void TwitterTweet() {
    final String index = "twitter";
    final String type = "tweet";
    LOG.debug("生成index[{}], type[{}]的数据", index, type);

    try {
      List<Map<String, Object>> datas = Jsons.fileToMapList("twitter_tweet_datas.json");
      System.out.println(datas);

    } catch (FileNotFoundException e) {
      LOG.error(e.getMessage(), e);
    }

    try (TransportClient client = Clients.defaultClient();) {
      List<Map<String, Object>> datas = Jsons.fileToMapList("twitter_tweet_datas.json");
      if (CollectionUtils.isNotEmpty(datas)) {
        for (Map<String, Object> sourceMap : datas) {
          IndexResponse indexResponse = client.prepareIndex(index, type).setSource(sourceMap).get();
          LOG.info(Responses.asString(indexResponse));
        }
      }
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

}
