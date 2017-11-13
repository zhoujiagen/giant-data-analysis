package com.spike.giantdataanalysis.coordination;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.RetryOneTime;

public class TestCoordinations implements TestCoordinationHelper {

  public static void main(String[] args) throws Exception {
    // System.out.println(Coordinations.getIps());

    CuratorFramework client =
        Coordinations.curatorClient(zookeeperConnectionString, new RetryOneTime(2));
    System.err.println("ZK 3.4 compatible? " + client.isZk34CompatibilityMode());
    client.start();

    String path = "/task/masters";
    client.create().creatingParentsIfNeeded().forPath(path);
    path = "/task/members";
    client.create().creatingParentsIfNeeded().forPath(path);
  }
}
