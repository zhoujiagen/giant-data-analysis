package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/**
 * <pre>
 * 在本地JVM中启动节点示例
 * REF: https://www.elastic.co/guide/en/elasticsearch/client/java-api/2.0/node-client.html
 * </pre>
 * @author zhoujiagen
 */
public class NodeClientExample {
  private static final Logger LOG = LoggerFactory.getLogger(NodeClientExample.class);

  public static void main(String[] args) {
    Map<String, String> settings = Maps.newHashMap();
    settings.put("path.home", "target/elasticsearch");

    // local in the JVM
    try (Node node = NodeBuilder.nodeBuilder()//
        .local(true)//
        .settings(Settings.settingsBuilder().put(settings)).node();) {
      Client client = node.client();

      SearchResponse searchResponse = client.prepareSearch()//
          .setQuery(QueryBuilders.matchAllQuery())//
          .get();
      LOG.info(Responses.asString(searchResponse));
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }

  }
}
