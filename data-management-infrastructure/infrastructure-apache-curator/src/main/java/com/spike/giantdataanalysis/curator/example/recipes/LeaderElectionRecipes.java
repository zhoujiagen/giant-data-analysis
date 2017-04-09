package com.spike.giantdataanalysis.curator.example.recipes;

import java.util.Date;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import com.spike.giantdataanalysis.curator.support.Curators;

/**
 * <pre>
 * Leader选举
 * 
 * 演示: 运行多个实例, 即在不同的JVM中运行.
 * 注意: kill掉一个进程后, 等待, 会重新执行.
 * </pre>
 * @author zhoujiagen
 */
public class LeaderElectionRecipes {
  public static void main(String[] args) {
    new Thread(new Agent()).start();
  }

  static class Agent implements Runnable {

    @Override
    public void run() {
      CuratorFramework client =
          Curators.CLIENT(Curators.ZooKeepers.DEFAULT_QUORUM_CONN_STRING,
            Curators.EXPONENTIAL_BACKOFF_RETRY(1000, 3));
      client.start();

      final String leaderPath = "/master";
      LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
          System.out.println(Thread.currentThread().getName() + ": I am the leader at "
              + new Date().getTime());
          // DO SOMETHING WITH CLIENT
          Thread.sleep(2000l);
        }
      };

      try (LeaderSelector ls = new LeaderSelector(client, leaderPath, listener);) {
        ls.autoRequeue(); // 自动成为候选者
        ls.start();

        while (true) {
          // 重新选举的时间间隔
          try {
            if (ls.hasLeadership()) {
              Thread.sleep(3000l);
              ls.interruptLeadership(); // 主动放弃
            } else {
              Thread.sleep(5000l);
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

    }
  }

}
