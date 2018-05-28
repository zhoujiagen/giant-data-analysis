package com.spike.giantdataanalysis.benchmark.client.raw.emitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMValue;

/**
 * 基于文件的性能指标值提交器
 */
public class FilePMValueEmitter implements PMValueEmitter {
  private static final Logger LOG = LoggerFactory.getLogger(FilePMValueEmitter.class);

  public static final String SEP = "|";
  public static final String NEW_LINE = "\n";
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

  private BufferedWriter writer;

  public FilePMValueEmitter(File file) {
    try {
      Files.createParentDirs(file); // 确保父级目录存在

      writer = new BufferedWriter(new FileWriter(file), 1024 * 2); // 2MB
    } catch (IOException e) {
      LOG.error("", e);
    }
  }

  @Override
  public void close() throws Exception {
    if (writer == null) {
      return;
    }

    writer.flush();
    writer.close();
  }

  @Override
  public void emit(PMValue pmValue) {

    try {
      writer.write(DATE_FORMAT.format(new Date()));
      writer.write(SEP);
      writer.write(pmValue.getGroup());
      writer.write(SEP);
      writer.write(pmValue.getMetric());
      writer.write(SEP);
      writer.write(pmValue.getValue());
      writer.write(SEP);
      writer.write(pmValue.getUnit());
      writer.write(NEW_LINE);

      writer.flush();
    } catch (IOException e) {
      LOG.error("emit to file failed", e);
    }

  }

}
