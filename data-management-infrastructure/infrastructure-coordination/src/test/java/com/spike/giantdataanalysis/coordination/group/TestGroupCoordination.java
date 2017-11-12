package com.spike.giantdataanalysis.coordination.group;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.RetryOneTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.TestCoordinationHelper;

public class TestGroupCoordination implements TestCoordinationHelper {
  private static final Logger LOG = LoggerFactory.getLogger(TestGroupCoordination.class);

  static final String membershipPath = "/test/workers";

  public static void main(String[] args) {

    // 确保membershipPath路径存在
    CuratorFramework client =
        Coordinations.curatorClient(zookeeperConnectionString, new RetryOneTime(2));
    System.err.println("ZK 3.4 compatible? " + client.isZk34CompatibilityMode());
    try {
      Coordinations.start(client);
      client.delete().deletingChildrenIfNeeded().forPath(membershipPath);
      client.create().creatingParentsIfNeeded().forPath(membershipPath);
    } catch (Exception e) {
      LOG.error("", e);
      LOG.info("Quit application.");
      System.exit(-1);
    }

    // 启动组成员
    for (int i = 0; i < 10; i++) {
      new Thread(new GroupWork(String.valueOf(i + 1))).start();
    }

    try {
      System.in.read();
    } catch (IOException e) {
      LOG.error("", e);
    }
  }

  static class GroupWork implements Runnable {

    private final String memberId;

    public GroupWork(String memberId) {
      this.memberId = memberId;
    }

    @Override
    public void run() {
      GroupCoordination gc =
          new GroupCoordination(zookeeperConnectionString, membershipPath, memberId, memberId);
      gc.join();// JOIN
      boolean inGroup = true;

      Random random = new Random(new Date().getTime());
      boolean shouldInGroup;

      while (true) {

        try {
          Thread.sleep(2 * 1000l);
        } catch (InterruptedException e) {
          LOG.error("", e);
        }

        shouldInGroup = random.nextBoolean();

        if (!shouldInGroup) {
          gc.quit();// QUIT
          inGroup = false;
        } else {
          if (!inGroup) {
            gc.rejoin();// REJOIN
            inGroup = true;
          }
        }
      }

    }
  }

}
