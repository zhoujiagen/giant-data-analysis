package com.spike.giantdataanalysis.curator.example;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

import com.spike.giantdataanalysis.curator.support.Curators;

public class HelloWorldCurator {
  public static void main(String[] args) {

    // 初始化CuratorFramework
    String connectString = Curators.ZooKeepers.DEFAULT_QUORUM_CONN_STRING;
    RetryPolicy retryPolicy = Curators.EXPONENTIAL_BACKOFF_RETRY(1000, 3);
    try (CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);) {
      // 启动
      client.start();

      try {
        // 写
        String path = "/curator";
        client.create().forPath(path, "Hello Curator".getBytes());

        // 读
        byte[] data = client.getData().forPath(path);
        System.out.println(new String(data));

        // 删除
        client.delete().forPath(path);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
