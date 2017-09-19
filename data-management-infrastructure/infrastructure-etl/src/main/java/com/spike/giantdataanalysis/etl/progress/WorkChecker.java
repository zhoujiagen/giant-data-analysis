package com.spike.giantdataanalysis.etl.progress;

import java.util.List;

import com.spike.giantdataanalysis.etl.config.ETLConfig;
import com.spike.giantdataanalysis.etl.exception.ETLException;
import com.spike.giantdataanalysis.etl.process.DataFileOps;

interface WorkChecker {

  /** 检查是否还有工作 */
  boolean check() throws ETLException;

  static class ImportWorkChecker implements WorkChecker {

    private static ImportWorkChecker INTSTANCE = new ImportWorkChecker();

    private ImportWorkChecker() {
    }

    public static ImportWorkChecker getInstance() {
      return INTSTANCE;
    }

    @Override
    public boolean check() throws ETLException {
      // 没有未做和在做的数据文件, 则退出
      final List<String> dataFileDirs = ETLConfig.dataFileDirs();
      List<String> leftNoneFiles = DataFileOps.I().locate(dataFileDirs, ProgressEnum.NONE);
      List<String> leftDoingFiles = DataFileOps.I().locate(dataFileDirs, ProgressEnum.DOING);
      if (leftNoneFiles.size() == 0 && leftDoingFiles.size() == 0) {
        return true;
      }

      return false;
    }

  }
}
