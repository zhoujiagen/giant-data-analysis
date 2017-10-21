package com.spike.giantdataanalysis.etl.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.etl.config.ETLConfig;
import com.spike.giantdataanalysis.etl.exception.ETLException;
import com.spike.giantdataanalysis.etl.process.DataFileOps;
import com.spike.giantdataanalysis.etl.process.DataImportor;
import com.spike.giantdataanalysis.etl.process.LineParser;
import com.spike.giantdataanalysis.etl.progress.ProgressEnum;
import com.spike.giantdataanalysis.etl.progress.ProgressHolder;
import com.spike.giantdataanalysis.etl.progress.ProgressReportThread;
import com.spike.giantdataanalysis.etl.progress.WorkCheckerThread;
import com.spike.giantdataanalysis.etl.progress.WorkStatus;
import com.spike.giantdataanalysis.etl.supports.ETLConstants;

public class ExampleMain {

  private static final Logger LOG = LoggerFactory.getLogger(ExampleMain.class);

  public static void main(String[] args) {

    final int workerSize = ETLConfig.workSize();
    ExecutorService ES = Executors.newFixedThreadPool(workerSize);

    final List<String> dataFileDirs = ETLConfig.dataFileDirs();

    final LineParser<List<String>> lineParser = new ExampleLineParser();
    final DataImportor<List<String>> dataImportor = new ExampleDataImportor();

    // 之前正在做的(可能是补做)
    List<String> doingDataFiles = DataFileOps.I().locate(dataFileDirs, ProgressEnum.DOING);
    // ProgressHolder.I().set(doingDataFiles, ProgressEnum.NONE); // 不需要标记, 以免与进展文件冲突
    for (String doingDataFile : doingDataFiles) {
      final String f_doingDataFile = doingDataFile;
      ES.execute(new Runnable() {
        @Override
        public void run() {
          parseAndImport(f_doingDataFile, 10, lineParser, dataImportor);
        }
      });
    }

    // 未做的
    List<String> noneDataFiles = DataFileOps.I().locate(dataFileDirs, ProgressEnum.NONE);
    ProgressHolder.I().set(noneDataFiles, ProgressEnum.NONE);
    for (String noneDataFile : noneDataFiles) {
      final String f_noneDataFile = noneDataFile;
      ES.execute(new Runnable() {
        @Override
        public void run() {
          parseAndImport(f_noneDataFile, 10, lineParser, dataImportor);
        }
      });
    }

    LOG.info("启动检查任务线程: {}", WorkCheckerThread.class.getSimpleName());
    Thread checkerThread = new Thread(new WorkCheckerThread());
    checkerThread.start();

    LOG.info("启动进展报告线程: {}", ProgressReportThread.class.getSimpleName());
    Thread progressThread = new Thread(new ProgressReportThread());
    progressThread.start();

    try {
      checkerThread.join();
      progressThread.join();
    } catch (InterruptedException e) {
      LOG.error("", e);
    }
  }

  private static void parseAndImport(String filePath, int batchLineCount,
      LineParser<List<String>> lineParser, DataImportor<List<String>> importor) {
    LOG.info("开始处理文件: filePath={}, 参数batchLineCount={}", filePath, batchLineCount);

    if (filePath == null || "".equals(filePath.trim())) {
      return;
    }

    if (batchLineCount <= 0) {
      batchLineCount = ETLConstants.DEFAULT_LINE_CNT;
    }

    File file = new File(filePath);
    if (!file.exists()) {
      LOG.error("文件{}不存在!", filePath);
      return;
    }

    long handledLineCount = 0l; // 重做时已经处理的行数量

    // 文件名称表示的处理状态
    ProgressEnum showProgressEnum = DataFileOps.I().progress(filePath);
    // 目标文件路径, 后续操作均是对目标文件的操作
    String targetFilePath = filePath;
    if (ProgressEnum.NONE.equals(showProgressEnum)) {
      targetFilePath = DataFileOps.I().mark(filePath, ProgressEnum.DOING);
    } else if (ProgressEnum.DOING.equals(showProgressEnum)) {
      WorkStatus workStatus =
          ProgressHolder.I().loadFormProgressFile(ETLConfig.progressFile(), filePath);
      LOG.info("DOING workStatus = {}", workStatus);
      if (workStatus != null) {
        // may never happened
        if (ProgressEnum.FINISHED.equals(workStatus.getProgressEnum())) {
          DataFileOps.I().mark(filePath, ProgressEnum.FINISHED);
          return;
        }
        handledLineCount = workStatus.getHandledCount();
      }
    } else if (ProgressEnum.FINISHED.equals(showProgressEnum)) {
      return;
    }

    // 现在文件路径以.DOING结尾

    LOG.info("文件={}, 已处理行数={}", targetFilePath, handledLineCount);
    // 更新进展记录中已处理行数
    ProgressHolder.I().update(targetFilePath, ProgressEnum.DOING, handledLineCount);

    List<List<String>> datas = new ArrayList<>();

    // 调整读取与导入速度
    long tryHandleCount = 0l;

    // TODO(zhoujiagen) tuning buffer size???
    try (BufferedReader reader = new BufferedReader(new FileReader(targetFilePath), 8192);) {

      String line = null;
      boolean handleResult = false;
      long lineIndex = 0;
      while ((line = reader.readLine()) != null) {
        if (lineIndex < handledLineCount) { // 跳过已经处理的部分
          lineIndex++;
          continue;
        }

        if (lineIndex > 0 && lineIndex % 1000 == 0) {
          LOG.info(
            "\n文件: {}, 已读取行数[{}], 尝试处理行数[{}], 实际已处理行数[{}], 当前待处理行数[{}]", //
            targetFilePath, lineIndex, tryHandleCount,
            ProgressHolder.I().handledCount(targetFilePath), datas.size());
        }
        if (tryHandleCount > ProgressHolder.I().handledCount(targetFilePath) + batchLineCount //
            && datas.size() >= batchLineCount) {
          LOG.info("\n读取速度[{}]超出导入速度[{}], 等待一下, 当前准备数据量[{}]", //
            tryHandleCount, ProgressHolder.I().handledCount(targetFilePath), datas.size());
          try {
            Thread.sleep(1000l);
          } catch (InterruptedException e) {/* ignore */
          }
        }

        if (!ETLConstants.BLANK.equals(line)) {
          datas.add(lineParser.parse(line, ETLConfig.fieldSeparator()));
        }
        if (datas.size() >= batchLineCount) {

          try {
            // 执行导入
            handleResult = importor.handle(targetFilePath, datas);
            // 更新进展记录中实际处理行数
            ProgressHolder.I().update(targetFilePath, ProgressEnum.DOING, datas.size());
          } catch (ETLException e) {
            LOG.error("import failed", e);
          }
          if (handleResult) {
            datas.clear();
          }
        }

        tryHandleCount++;
        lineIndex++;
      }

      // 处理剩下的记录
      if (datas.size() > 0) {
        try {
          importor.handle(targetFilePath, datas);
          ProgressHolder.I().update(targetFilePath, ProgressEnum.DOING, datas.size());
        } catch (ETLException e) {
          LOG.error("import failed");
        }
      }
      DataFileOps.I().mark(targetFilePath, ProgressEnum.FINISHED);
      ProgressHolder.I().update(targetFilePath, ProgressEnum.FINISHED, 0);
    } catch (Exception e) {
      LOG.error("处理文件[" + targetFilePath + "]失败", e);
    }

  }
}
