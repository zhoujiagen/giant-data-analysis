package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/***
 * <pre>
 * *连接仅协同节点的客户端.*REF:https:// www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/client-connected-to-client-node.html
 * </pre>
 * 
 * **
 * @author zhoujiagen
 */
@RunWith(RandomizedRunner.class)
public class ExampleCoordinatingOnlyNodeClient {
  private static final Logger LOG =
      LoggerFactory.getLogger(ExampleCoordinatingOnlyNodeClient.class);

  @Test
  public void main() {

    // FIXME(zhoujiagen): java.lang.IllegalStateException: Unsupported transport.type []
    // coordinating only node
    Settings settings = Settings.builder()//
        .put("", "")//
        .put("path.home", "target/") //
        .put("network.host", "127.0.0.1")//
        .put("node.master", false)//
        .put("node.data", false)//
        .put("node.ingest", false)//
        .put("search.remote.connect", false)//
        .put("http.enabled", true)//
        .build();

    try (Node node = new Node(settings);) {
      node.start();
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
