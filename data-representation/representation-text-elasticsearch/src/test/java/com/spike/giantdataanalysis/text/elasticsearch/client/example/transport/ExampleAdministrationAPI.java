package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import static com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients.adminClient;
import static com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients.defaultClient;
import static com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients.indicesClient;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Jsons;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/**
 * <pre>
 * 管理客户端示例:
 * (1) 索引管理 @see {@link #indicesAdministration()}
 * REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/2.4/java-admin-indices.html
 * 
 * (2) 集群管理 @see {@link #clusterAdministration()}
 * </pre>
 * 
 * @author zhoujiagen
 */
@RunWith(RandomizedRunner.class)
public final class ExampleAdministrationAPI {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleAdministrationAPI.class);

  // static StreamOutput ERR = new OutputStreamStreamOutput(new SystemErrStream());

  static final String index = "twitter"; // 索引名称
  static final String type = "type"; // 文档类型名称

  @Test
  public void main() {
    try (TransportClient client = defaultClient();) {
      AdminClient adminClient = adminClient(client);

      // 索引操作管理
      // indicesAdministration(adminClient);

      // 集群管理
      clusterAdministration(adminClient);

      Thread.sleep(5000L);// wait for a moment
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static void indicesAdministration(AdminClient adminClient) throws Exception {
    IndicesAdminClient indiciesAdminClient = indicesClient(adminClient);

    // 创建默认配置的索引
    createIndex(indiciesAdminClient, index);
    // 带配置创建索引
    // createIndexWithSettings(indiciesAdminClient, index);
    // 创建索引和文档类型
    // createIndexWithType(indiciesAdminClient, index, type);
    // 在已有索引上添加/更新文档类型
    // createTypeInExistedIndex(indiciesAdminClient, index, type);
    // 刷新索引
    refreshIndex(indiciesAdminClient, index);
    // 索引配置读取和设置
    settingIndex(indiciesAdminClient, index);

    Thread.sleep(5000L);// wait for a moment

    deleteIndex(indiciesAdminClient, index);
  }

  static void createIndex(IndicesAdminClient indiciesAdminClient, String index) {
    LOG.debug("创建索引{}", index);

    // throw IndexAlreadyExistsException if index already exists
    // 默认配置, 无mapping
    CreateIndexResponse createResponse = indiciesAdminClient.prepareCreate(index).get();
    LOG.info(Responses.asString(createResponse));
  }

  static void createIndexWithSettings(IndicesAdminClient indiciesAdminClient, String index) {
    LOG.debug("带配置创建索引{}", index);

    Settings settings = Settings.builder()//
        .put("index.number_of_shards", 2)//
        .put("index.number_of_replicas", 2)//
        .build();
    CreateIndexResponse createResponse =
        indiciesAdminClient.prepareCreate(index).setSettings(settings).get();
    LOG.info(Responses.asString(createResponse));
  }

  static void createIndexWithType(IndicesAdminClient indiciesAdminClient, String index, String type)
      throws IOException {
    LOG.debug("创建索引{}和文档类型{}", index, type);

    Map<String, Object> map = Jsons.fileToMap("twitter_tweet_mapping.json");
    // 或者使用ES内建帮助类import static org.elasticsearch.common.xcontent.XContentFactory.*;
    // XContentBuilder builder = jsonBuilder().map(map);
    CreateIndexResponse createResponse =
        indiciesAdminClient.prepareCreate(index).addMapping(type, map).get();
    LOG.info(Responses.asString(createResponse));
  }

  static void createTypeInExistedIndex(IndicesAdminClient indiciesAdminClient, String index,
      String type) throws IOException {
    LOG.debug("在已有索引{}上添加/更新文档类型{}", index, type);

    Map<String, Object> map = Jsons.fileToMap("twitter_tweet_mapping.json");
    PutMappingResponse putMappingResponse = indiciesAdminClient.preparePutMapping(index)//
        .setType(type)//
        .setSource(map).get();
    LOG.info(Responses.asString(putMappingResponse));

  }

  static void deleteIndex(IndicesAdminClient indiciesAdminClient, String index) {
    LOG.debug("删除索引{}", index);

    DeleteIndexResponse deleteResponse = indiciesAdminClient.prepareDelete(index).get();
    LOG.info(Responses.asString(deleteResponse));
  }

  static void refreshIndex(IndicesAdminClient indiciesAdminClient, String index) {
    LOG.debug("创新索引{}", index);

    RefreshResponse refreshResponse = indiciesAdminClient.prepareRefresh(index).get();
    LOG.info(Responses.asString(refreshResponse));
  }

  private static void settingIndex(IndicesAdminClient indiciesAdminClient, String index) {
    LOG.debug("索引配置读取和设置{}", index);

    // 读取配置
    GetSettingsResponse getSettingsResponse = indiciesAdminClient.prepareGetSettings(index).get();
    LOG.info(Responses.asString(getSettingsResponse));

    // 设置配置
    UpdateSettingsResponse updateSettingsResponse = //
        indiciesAdminClient.prepareUpdateSettings(index)//
            .setSettings(Settings.builder().put("index.number_of_replicas", 2)).get();
    LOG.info(Responses.asString(updateSettingsResponse));
  }

  static void clusterAdministration(AdminClient adminClient) {
    ClusterAdminClient clusterAdminClient = adminClient.cluster();

    LOG.debug("查看集群信息");

    ClusterHealthResponse clusterHealthResponse = clusterAdminClient.prepareHealth().get();
    LOG.info(Responses.asString(clusterHealthResponse));
  }

}
