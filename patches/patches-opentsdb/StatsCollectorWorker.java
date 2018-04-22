package net.opentsdb.tsd;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.opentsdb.GangliaStatsCollector;

/**
 * 定制的统计信息收集工作者
 */
public class StatsCollectorWorker implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(StatsCollectorWorker.class);

  private final GangliaStatsCollector collector;

  public static long SLEEP_DURATION = 10000l;

  public StatsCollectorWorker(GangliaStatsCollector collector) {
    Preconditions.checkArgument(collector != null,
      "Argument GangliaStatsCollector should not be null!");

    this.collector = collector;
  }

  public void shutdown() {
    LOG.info("Shutdown StatsCollectorWorker");

    System.exit(0);
  }

  @Override
  public void run() {

    while (true) {
      LOG.debug("Collect Stats at timestamp: {}", new Date().getTime());

      ConnectionManager.collectStats(collector);
      RpcHandler.collectStats(collector);
      RpcManager.collectStats(collector);
      collector.getTsdb().collectStats(collector);

      try {
        Thread.sleep(SLEEP_DURATION);
      } catch (InterruptedException e) {
      }
    }

  }

}
