package com.spike.giantdataanalysis.text.elasticsearch.client.example.support;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.commons.lang.StringUtils;

/**
 * 客户端工具类
 * @author zhoujiagen
 * @see com.spike.giantdataanalysis.text.elasticsearch.client.example.transport.TransportClientExample
 */
public final class Clients {

  private static final Logger LOG = LoggerFactory.getLogger(Clients.class);

  /**
   * 获取索引管理客户端
   * @param adminClient
   * @return
   * @see #adminClient(TransportClient)
   */
  public static IndicesAdminClient indicesClient(AdminClient adminClient) {
    if (adminClient == null) return null;

    return adminClient.indices();
  }

  /**
   * 获取管理客户端
   * @param client
   * @return
   * @see #newClient(String, int, Map)
   */
  public static AdminClient adminClient(TransportClient client) {
    if (client == null) return null;

    return client.admin();
  }

  /**
   * 创建新客户端
   * @param host
   * @param port
   * @param configs
   * @return
   * @throws UnknownHostException
   * @see {@link #defaultClient()}
   */
  public static TransportClient newClient(String host, int port, Map<String, Object> configs)
      throws UnknownHostException {

    if (StringUtils.isBlank(host)) throw new UnknownHostException();

    Builder builder = Settings.settingsBuilder();
    for (String key : configs.keySet()) {
      builder.put(key, configs.get(key));
    }
    Settings settings = builder.build();

    TransportClient client = TransportClient.builder()//
        .settings(settings).build()//
        .addTransportAddress(//
          new InetSocketTransportAddress(InetAddress.getByName(host), port));

    return client;

  }

  /**
   * 测试用的默认客户端
   * @return
   * @throws UnknownHostException
   */
  public static TransportClient defaultClient() throws UnknownHostException {

    Builder builder = Settings.settingsBuilder();
    // 集群名称
    builder.put("cluster.name", "elasticsearch");
    // 是否忽略与连接节点验证集群名称
    builder.put("client.transport.ignore_cluster_name", true);
    // 等待ping节点响应的时间
    builder.put("client.transport.ping_timeout", 5, TimeUnit.SECONDS);
    // ping节点的时间间隔
    builder.put("client.transport.nodes_sampler_interval", 5, TimeUnit.SECONDS);
    Settings settings = builder.build();

    TransportClient client = TransportClient.builder()//
        .settings(settings).build()//
        .addTransportAddress(//
          new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

    return client;
  }

  /**
   * 显示客户端信息
   * @param client
   */
  public static void info(TransportClient client) {
    if (client == null) return;

    LOG.info("using client: {}", client);

    LOG.info("listedNodes: {}", client.listedNodes());
    LOG.info("connectedNodes: {}", client.connectedNodes());
    LOG.info("filteredNodes: {}", client.filteredNodes());
    LOG.info("transportAddresses: {}", client.transportAddresses());
  }

}
