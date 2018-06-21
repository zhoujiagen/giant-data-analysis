package com.spike.giantdataanalysis.benchmark.client.raw.example;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.benchmark.client.raw.config.PGenParameter;
import com.spike.giantdataanalysis.benchmark.client.raw.emitter.PMValueEmitter;
import com.spike.giantdataanalysis.benchmark.client.raw.gather.PMValueGather;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.CountedPMRecord;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMRecord;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMValue;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.SampledPMRecord;
import com.spike.giantdataanalysis.benchmark.client.raw.snapshot.PMSnapshotParameter;
import com.spike.giantdataanalysis.benchmark.client.raw.snapshot.PMSnapshotor;

public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
    ThreadGroup tg = new ThreadGroup(ExampleMain.class.getSimpleName());

    // (1) 负载生成器线程
    PGenParameter wpgp = new PGenParameter(); // 负载生成器参数
    wpgp.setWorkerIndex(1);
    wpgp.setActionDuration(0l);
    wpgp.setWaitTime(1000l);
    Set<PMRecord> pmRecords = Sets.newHashSet(); // 性能指标记录
    pmRecords.add(CountedPMRecord.getInstance());
    Thread writeThread =
        new Thread(tg, new ExampleWritePGen(wpgp, pmRecords), "ExampleWritePGen-1");
    writeThread.start();
    pmRecords = Sets.newHashSet();
    pmRecords.add(SampledPMRecord.getInstance());
    wpgp.setWorkerIndex(2);
    Thread writeThread2 =
        new Thread(tg, new ExampleWritePGen(wpgp, pmRecords), "ExampleWritePGen-2");
    writeThread2.start();

    // (2) 快照线程
    PMSnapshotParameter sp = new PMSnapshotParameter();
    sp.setDuration(5000l);
    sp.setCheckDutation(1000l);
    sp.setMetricGroup("test");
    PMValueGather gather = new PMValueGather() { // 性能指标值收集器
      @Override
      public List<PMValue> gather() {
        List<PMValue> values = CountedPMRecord.getInstance().values();
        values.addAll(SampledPMRecord.getInstance().values());
        return values;
      }
    };
    PMValueEmitter emitter = PMValueEmitter.DEFAULT; // 性能指标值提交器
    PMSnapshotor pmSnapshotor = new PMSnapshotor(sp, gather, emitter);
    Thread snapshotorThread = new Thread(tg, pmSnapshotor);
    snapshotorThread.setDaemon(true);
    snapshotorThread.start();

    // (3) 等待
    writeThread.join();
    writeThread2.join();
    snapshotorThread.join();
  }
}
