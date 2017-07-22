package com.spike.giantdataanalysis.opentsdb.example;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import net.opentsdb.core.TSDB;
import net.opentsdb.utils.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.opentsdb.supports.OpenTSDBs.errBack;
import com.spike.giantdataanalysis.opentsdb.supports.OpenTSDBs.succBatchBack;
import com.stumbleupon.async.Deferred;

// REF net.opentsdb.examples.AddDataExample
public class OpenTSDBExample {

  private static final Logger LOG = LoggerFactory.getLogger(OpenTSDBExample.class);

  public static void main(String[] args) throws Exception {
//    if (args.length != 1) {
//      LOG.error("Invalid argument, only support 'opentsdb.conf'");
//      return;
//    }

    try {
      String file = System.getProperty("user.dir") + "/src/main/resources/opentsdb.conf";
      Preconditions.checkState(new File(file).exists(), "file[" + file + "] not exists");

      LOG.debug("Initialize TSDB instance...");
      Config config = new Config(file);
      TSDB tsdb = new TSDB(config);

      String metric = "sys.if.bytes.out";
      Map<String, String> tags = Maps.newHashMap();
      tags.put("host", "web01");
      tags.put("colo", "lga");
      tags.put("interface", "eth0");

      long timestamp = System.currentTimeMillis();
      long value = 1234l;
      int n = 100;
      ArrayList<Deferred<Object>> deferreds = new ArrayList<Deferred<Object>>(n);
      for (int i = 0; i < n; i++) { // 批量添加
        Deferred<Object> deferred = tsdb.addPoint(metric, timestamp, value + i, tags);
        deferreds.add(deferred);
        timestamp += 30;
      }

      LOG.debug("Writing data to TSDB, metric: {}, size: {}", metric, String.valueOf(n));
      Deferred.groupInOrder(deferreds)//
          .addErrback(new errBack())//
          .addCallback(new succBatchBack())//
          .join();// Block the thread until the deferred returns it's result.

      tsdb.flush().joinUninterruptibly();
      // Gracefully shutdown connection to TSDB. This is CRITICAL as it will
      // flush any pending operations to HBase.
      LOG.debug("Shutdown TSDN instance...");
      tsdb.shutdown().join();

    } catch (Exception e) {
      LOG.error("failed", e);
    }
  }
}
