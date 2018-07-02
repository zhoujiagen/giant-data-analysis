package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients;

@RunWith(RandomizedRunner.class)
public class ExampleTransportClient {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleTransportClient.class);

  @Test
  public void main() throws UnknownHostException {
    Builder builder = Settings.builder();
    // 集群名称
    builder.put("cluster.name", "elasticsearch");
    // 是否忽略与连接节点验证集群名称
    builder.put("client.transport.ignore_cluster_name", true);
    // 等待ping节点响应的时间
    builder.put("client.transport.ping_timeout", 5, TimeUnit.SECONDS);
    // ping节点的时间间隔
    builder.put("client.transport.nodes_sampler_interval", 5, TimeUnit.SECONDS);
    Settings settings = builder.build();

    try (TransportClient client = Clients.newClient("localhost", 9300, settings);) {
      LOG.debug("using client: {}", client);

      LOG.info("listedNodes: {}", client.listedNodes());
      LOG.info("connectedNodes: {}", client.connectedNodes());
      LOG.info("filteredNodes: {}", client.filteredNodes());
      LOG.info("transportAddresses: {}", client.transportAddresses());

    } catch (UnknownHostException e) {
      LOG.error(e.getMessage(), e);
    }
  }

}
