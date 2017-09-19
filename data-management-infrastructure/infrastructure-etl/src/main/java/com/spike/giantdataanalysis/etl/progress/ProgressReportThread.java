package com.spike.giantdataanalysis.etl.progress;

import com.spike.giantdataanalysis.etl.config.ETLConfig;

/** 进展报告线程 */
public final class ProgressReportThread implements Runnable {

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(10000l);
      } catch (InterruptedException e) {
        Thread.interrupted();
      }

      ProgressHolder.I().dumpToFile(ETLConfig.progressFile());
    }
  }

}
