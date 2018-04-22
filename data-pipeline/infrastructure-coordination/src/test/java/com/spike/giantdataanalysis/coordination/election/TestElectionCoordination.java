package com.spike.giantdataanalysis.coordination.election;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.coordination.TestCoordinationHelper;
import com.spike.giantdataanalysis.coordination.exception.CoordinationException;

public class TestElectionCoordination implements TestCoordinationHelper {

  static final String leadershipPath = "/test/leadershippath";

  public static void main(String[] args) {
    List<Thread> instances = Lists.newArrayList();

    for (int i = 0; i < 10; i++) {
      final String instanceId = "INSTANCE-" + String.valueOf(i + 1);

      Thread t = new Thread(new Runnable() {
        @Override
        public void run() {

          LeaderSelectorListener lsl = new LeaderSelectorListenerAdapter() {

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
              System.out.println("I am the leader: " + instanceId);
              while (true) {
                Thread.sleep(3000l);
                System.out.println("palyAsFollower...");
              }
            }
          };

          ElectionCoordination ec =
              new ElectionCoordination(zookeeperConnectionString, leadershipPath, instanceId, lsl);
          try {
            boolean isLeader = ec.blockingCheckLeadership();
            if (isLeader) {

            } else {

              while (true) {
                try {
                  Thread.sleep(10000l);
                  System.out.println("palyAsFollower...");
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

            }
          } catch (CoordinationException e) {
            System.err.println(instanceId + " leaves.");
            e.printStackTrace();
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
        e.printStackTrace();
      }
    }
  }
}
