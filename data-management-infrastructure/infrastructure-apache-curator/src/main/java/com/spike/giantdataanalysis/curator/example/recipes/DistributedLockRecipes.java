package com.spike.giantdataanalysis.curator.example.recipes;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import com.spike.giantdataanalysis.curator.support.Curators;

/**
 * <pre>
 * 进程间的分布式锁
 * 
 * 演示: 运行多个实例, 即在不同的JVM中运行.
 * 注意: kill掉一个进程后, 等待, 会重新执行.
 * </pre>
 * @author zhoujiagen
 */
public class DistributedLockRecipes {

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

      String lockPath = "/lock";
      InterProcessMutex lock = new InterProcessMutex(client, lockPath);
      Random random = new Random(new Date().getTime());
      try {
        while (true) {
          // 获取锁
          if (lock.acquire(5, TimeUnit.SECONDS)) {
            // DO SOMETHING
            System.out.println(Thread.currentThread().getName() + ": I got the lock at "
                + new Date().getTime());
            Thread.sleep(random.nextInt(2000));

            // 释放锁
            lock.release();
          } else {
            Thread.sleep(random.nextInt(1000));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }
}
