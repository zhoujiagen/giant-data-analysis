package com.spike.giantdataanalysis.coordination.election;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.coordination.TestCoordinationHelper;
import com.spike.giantdataanalysis.coordination.exception.CoordinationException;

public class TestElectionCoordination implements TestCoordinationHelper {

  private static final Logger LOG = LoggerFactory.getLogger(TestElectionCoordination.class);

  static final String leadershipPath = "/test/workers";

  static class DummyElectionCoordination extends ElectionCoordination {

    public DummyElectionCoordination(String zookeeperConnectionString, String leadershipPath,
        String memberId) {
      super(zookeeperConnectionString, leadershipPath, memberId);
    }

    @Override
    protected void playAsLeader(CuratorFramework client) throws CoordinationException {
      while (true) {
        try {
          Thread.sleep(3000l);
          LOG.info("playAsLeader...");
        } catch (Exception e) {
          LOG.error("", e);
        }
      }

    }

    @Override
    protected void palyAsFollower(LeaderSelector leaderSelector, CuratorFramework client)
        throws CoordinationException {

      while (true) {
        try {
          Thread.sleep(1000l);
          LOG.info("palyAsFollower...");
        } catch (Exception e) {
          LOG.error("", e);
        }
      }
    }
  }

  public static void main(String[] args) {
    List<Thread> instances = Lists.newArrayList();

    for (int i = 0; i < 10; i++) {
      final String instanceId = "INSTANCE-" + String.valueOf(i + 1);

      Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
          DummyElectionCoordination ec =
              new DummyElectionCoordination(zookeeperConnectionString, leadershipPath, instanceId);
          try {
            ec.blockingCheckLeadership();
          } catch (CoordinationException e) {
            LOG.error(instanceId + " leaves.", e);
          }

        }
      });

      instances.add(t);
    }

    for (Thread t : instances) {
      t.start();
    }

    for (Thread t : instances) {
      try {
        t.join();
      } catch (InterruptedException e) {
        LOG.error("", e);
      }
    }
  }
}
