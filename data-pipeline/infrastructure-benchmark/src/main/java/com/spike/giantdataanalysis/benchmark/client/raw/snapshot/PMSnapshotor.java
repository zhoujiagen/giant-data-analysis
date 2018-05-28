package com.spike.giantdataanalysis.benchmark.client.raw.snapshot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.benchmark.client.raw.emitter.PMValueEmitter;
import com.spike.giantdataanalysis.benchmark.client.raw.gather.PMValueGather;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMValue;

public class PMSnapshotor implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(PMSnapshotor.class);

  private final PMSnapshotParameter parameter;

  private final PMValueGather gather;
  private final PMValueEmitter emitter;

  public PMSnapshotor(PMSnapshotParameter parameter, PMValueGather gather, PMValueEmitter emitter) {
    this.parameter = parameter;
    this.gather = gather;
    this.emitter = emitter;
  }

  @Override
  public void run() {

    LOG.info("执行参数: {}", parameter.toString());

    long start = System.currentTimeMillis();
    long end;

    while (true) {
      try {
        end = System.currentTimeMillis();

        if (end - start > parameter.getDuration()) {

          List<PMValue> list = gather.gather();
          if (list != null && list.size() > 0) {
            for (PMValue pmValue : list) {
              pmValue.setGroup(parameter.getMetricGroup());
              emitter.emit(pmValue);
            }
          }

          start = System.currentTimeMillis();

        } else {
          if (parameter.getCheckDutation() > 0) {
            Thread.sleep(parameter.getCheckDutation());
          }
        }
      } catch (Exception e) {
        LOG.error("Snapshot failed", e);
      }
    }

  }

}
