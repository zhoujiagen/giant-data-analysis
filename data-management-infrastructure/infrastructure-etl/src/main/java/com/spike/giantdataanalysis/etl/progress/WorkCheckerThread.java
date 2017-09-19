package com.spike.giantdataanalysis.etl.progress;

import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.etl.config.ETLConfig;
import com.spike.giantdataanalysis.etl.exception.ETLException;
import com.spike.giantdataanalysis.etl.progress.WorkChecker.ImportWorkChecker;

public final class WorkCheckerThread implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(WorkCheckerThread.class);

  @Override
  public void run() {
    boolean exitFlag = false;
    while (true) {
      try {
        Thread.sleep(5000l);
      } catch (InterruptedException e) {
        Thread.interrupted();
      }

      try {
        exitFlag = ImportWorkChecker.getInstance().check();
      } catch (ETLException e) {
        LOG.error("", e);
      }
      LOG.info("检查是否可以退出: {}", exitFlag);
      if (exitFlag) {
        ProgressHolder.I().dumpToFile(ETLConfig.progressFile());
        try {
          ProgressHolder.I().dump(new PrintWriter(System.out));
        } catch (ETLException e) {
          LOG.error("", e);
        }

        System.exit(0);
      }
    }
  }
}
