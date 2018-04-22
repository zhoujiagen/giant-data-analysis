package com.spike.giantdataanalysis.coordination;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 协同工具类.
 * @author zhoujiagen
 */
public class Coordinations {

  /** 默认的Curator重试策略 **/
  public static RetryPolicy DEFAULT_CURATOR_RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

  /** 获取Curator客户端 */
  public static CuratorFramework curatorClient(String zookeeperConnectionString,
      RetryPolicy retryPolicy) {
    CuratorFramework client =
        CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
    return client;
  }

  /** 启动Curator客户端. */
  public static void start(CuratorFramework client) {
    client.start();
  }

  /** 关闭Curator客户端. */
  public static void close(CuratorFramework client) {
    client.close();
  }

  /** 生成唯一ID */
  public static final String id() {
    return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
  }

  /**  */
  /**
   * 获取本机IP
   * @return Map[网卡名称, IP列表]
   */
  public static final Map<String, List<String>> getIps() {
    Map<String, List<String>> result = Maps.newHashMap();

    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

      while (interfaces.hasMoreElements()) {
        NetworkInterface ni = interfaces.nextElement();

        List<String> niIps = Lists.newArrayList();
        Enumeration<InetAddress> addresss = ni.getInetAddresses();
        while (addresss.hasMoreElements()) {
          InetAddress nextElement = addresss.nextElement();
          String hostAddress = nextElement.getHostAddress();
          niIps.add(hostAddress);
        }
        result.put(ni.getName() + "/" + ni.getDisplayName(), niIps);
      }
    } catch (Exception e) {
      // ignore
    }

    return result;
  }

}
