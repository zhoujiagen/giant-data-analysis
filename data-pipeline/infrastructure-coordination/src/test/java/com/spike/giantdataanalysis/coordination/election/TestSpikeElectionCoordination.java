package com.spike.giantdataanalysis.coordination.election;

import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.curator.retry.RetryOneTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.TestCoordinationHelper;

public class TestSpikeElectionCoordination implements TestCoordinationHelper {

  private static final Logger LOG = LoggerFactory.getLogger(TestSpikeElectionCoordination.class);

  static final String mutexPath = "/test/workers";
  static final String latchPath = "/test/workers";

  public static void main(String[] args) {

    // 确保membershipPath路径存在
    CuratorFramework client =
        Coordinations.curatorClient(zookeeperConnectionString, new RetryOneTime(2));
    System.err.println(client.isZk34CompatibilityMode());
  }

  // REF: http://curator.apache.org/curator-recipes/leader-latch.html
  static void useLeaderLatch(CuratorFramework client) {

    String id = Coordinations.id();
    LeaderLatch ll = new LeaderLatch(client, latchPath, id);
    try {
      ll.start();

      // 阻塞等待, 直到当前实例称为Leader
      ll.await();

    } catch (Exception e) {
      LOG.error("", e);
    }

    try {
      System.in.read();

      LOG.info("{} quit the leadership group!", id);
      ll.close();
    } catch (IOException e) {
      LOG.error("", e);
    }
  }

  // REF: http://curator.apache.org/curator-recipes/leader-election.html
  static void useLeaderSelector(CuratorFramework client) {

    LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
      @Override
      public void takeLeadership(CuratorFramework client) throws Exception {
        LOG.info("Well, I am the leader, now I am going to peapare something for workers.");

        // none-return group
        while (true) {
          try {
            LOG.info("DO LEADER WORK.");
            Thread.sleep(5000l);
          } catch (InterruptedException e) {
            LOG.error("", e);
          }
        }
      }
    };

    String id = Coordinations.id();
    LeaderSelector ls = new LeaderSelector(client, mutexPath, listener);
    ls.setId(id);
    ls.autoRequeue();
    ls.start();

    try {
      // 确认是否有Leader, 以及自己是否是Leader
      boolean leaderFound = false;
      boolean isLeader = false;
      while (!leaderFound) {
        Participant leader = ls.getLeader();

        if (!leader.isLeader()) {
          LOG.info("{}: Current no leader found, wait a few seconds...", id);
          Thread.sleep(1000l);
        } else {
          LOG.info("{}: Leader found, its id is {}: same? ", id, leader.getId(),
            id.equals(leader.getId()));
          if (id.equals(leader.getId())) {
            isLeader = true;
          }
          break;
        }
      }

      if (!isLeader) {
        LOG.info("Well, I am the worker, now I am going to do something.");

        while (true) {

          try {
            LOG.info("DO WORKER WORK.");
            Thread.sleep(5000l);
          } catch (InterruptedException e) {
            LOG.error("", e);
          }

        }
      }

    } catch (Exception e) {
      LOG.error("", e);
    }

    try {
      System.in.read();

      LOG.info("{} quit the leadership group!", id);
      ls.close();
    } catch (IOException e) {
      LOG.error("", e);
    }

  }

}
