package com.spike.giantdataanalysis.coordination.lock;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.coordination.TestCoordinationHelper;
import com.spike.giantdataanalysis.coordination.exception.CoordinationException;

public class TestLockCoordination implements TestCoordinationHelper {
  private static final Logger LOG = LoggerFactory.getLogger(TestLockCoordination.class);

  static final String lockPath = "/test/locks";

  public static void main(String[] args) {

    List<Thread> acquirerList = Lists.newArrayList();
    for (int i = 0; i < 5; i++) {
      final String acquirer = "INSTANCE-" + String.valueOf(i + 1);
      Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
          LockCoordination lc = new LockCoordination(zookeeperConnectionString, lockPath, acquirer);
          Random random = new Random(new Date().getTime());

          while (true) {
            try {
              Thread.sleep(2000l);

              lc.acquire();// +
              Thread.sleep(random.nextInt(10) * 1000);
              lc.release();// -

            } catch (InterruptedException | CoordinationException e) {
              LOG.error("", e);
            }
          }
        }
      });

      acquirerList.add(t);
    }

    for (Thread t : acquirerList) {
      t.start();
    }

    for (Thread t : acquirerList) {
      try {
        t.join();
      } catch (InterruptedException e) {
        LOG.error("", e);
      }
    }
  }
}
