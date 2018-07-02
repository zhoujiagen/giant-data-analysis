package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.commons.lang.DateUtils;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Jsons;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/**
 * <pre>
 * 文档API示例
 * 
 * 单文档API
 * Index, Get, Delete, Update
 * 
 * 多文档API
 * MultiGet, Bulk
 * </pre>
 * 
 * @author zhoujiagen
 */
@RunWith(RandomizedRunner.class)
public final class ExampleDocumentAPI {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleDocumentAPI.class);

  static final String index = "twitter";// 索引名称
  static final String type = "tweet";// 文档类型
  static String docID = "";

  @Test
  public void main() {
    try (TransportClient client = Clients.defaultClient();) {
      // 索引文档
      index(client);
      // 查询文档
      get(client);
      // 更新文档
      update(client);
      // 批量查询文档
      multiGet(client);
      // 删除文档
      delete(client);

      // 批量操作
      bulk(client);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  // REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/java-docs-index.html
  static void index(TransportClient client) throws FileNotFoundException {
    LOG.debug("索引文档");

    Jsons.setDateFormat(DateUtils.DATE_YMD_FORMAT);
    Map<String, Object> sourceMap = Jsons.fileToMap("twitter_tweet_data.json");
    IndexResponse indexResponse = client.prepareIndex(index, type)//
        .setSource(sourceMap)//
        .get();
    LOG.info(Responses.asString(indexResponse));
    docID = indexResponse.getId();
  }

  // REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/java-docs-get.html
  static void get(TransportClient client) {
    LOG.debug("查询文档");

    GetResponse getResponse = client.prepareGet(index, type, docID).get();
    LOG.info(Responses.asString(getResponse));
  }

  // REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/java-docs-multi-get.html
  static void multiGet(TransportClient client) {
    LOG.debug("批量查询文档");

    MultiGetResponse multiGetResponse = client.prepareMultiGet()//
        .add(index, type, Lists.newArrayList(docID))//
        .get();
    LOG.info(Responses.asString(multiGetResponse));
  }

  /**
   * <pre>
   * (1) 字段更新; 使用script更新 
   * (2) 添加额外字段merge; 原文档不存在时upsert
   * </pre>
   * 
   * @param client
   * @throws ExecutionException
   * @throws InterruptedException
   */
  // REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/java-docs-update.html
  static void update(TransportClient client) throws InterruptedException, ExecutionException {
    // or use update by query
    LOG.debug("更新文档");

    UpdateRequest updateRequest = new UpdateRequest(index, type, docID);
    Map<String, Object> sourceMap = new HashMap<String, Object>();
    sourceMap.put("message", "trying out Elasticsearch again");
    updateRequest.doc(sourceMap);

    UpdateResponse updateResponse = client.update(updateRequest).get();
    LOG.info(Responses.asString(updateResponse));
  }

  // REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/java-docs-delete.html
  static void delete(TransportClient client) {
    // or use delete by query
    LOG.debug("删除文档");

    DeleteResponse deleteResponse = client.prepareDelete(index, type, docID).get();
    LOG.info(Responses.asString(deleteResponse));
  }

  // REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/java-docs-bulk.html
  static void bulk(TransportClient client) {
    LOG.debug("批量操作文档");

    BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

    IndexRequest indexRequest = new IndexRequest(index, type);
    Map<String, Object> sourceMap = Maps.newHashMap();
    sourceMap.put("user", "kimchy");
    sourceMap.put("postDate", DateUtils.now(DateUtils.DATE_YMD_FORMAT));
    sourceMap.put("message", "trying out Elasticsearch");
    indexRequest.id("1");
    indexRequest.source(sourceMap);
    bulkRequestBuilder.add(indexRequest);// +

    indexRequest = new IndexRequest(index, type);
    sourceMap = Maps.newHashMap();
    sourceMap.put("user", "kimchy");
    sourceMap.put("postDate", DateUtils.now(DateUtils.DATE_YMD_FORMAT));
    sourceMap.put("message", "another post");
    indexRequest.id("2");
    indexRequest.source(sourceMap);
    bulkRequestBuilder.add(indexRequest);// +

    BulkResponse bulkResponse = bulkRequestBuilder.get();
    LOG.info(Responses.asString(bulkResponse));
  }
}
