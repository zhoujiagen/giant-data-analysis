package com.spike.giantdataanalysis.etl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.etl.config.ETLConfig;
import com.spike.giantdataanalysis.etl.supports.ETLConstants;

public class TestDataFilePrepare {
  public static void main(String[] args) throws IOException {

    final List<String> fields = Lists.newArrayList("a", "b", "c", "d", "e");

    List<String> dataFileDirs = ETLConfig.dataFileDirs();
    for (String dataFileDir : dataFileDirs) {
      File dataFileDirFile = new File(dataFileDir);
      if (!dataFileDirFile.exists()) continue;

      // clean all
      File[] existedFiles = dataFileDirFile.listFiles();
      if (existedFiles != null && existedFiles.length > 0) {
        for (File existedFile : existedFiles) {
          existedFile.delete();
        }
      }

      for (int i = 0; i <= 9; i++) {
        File newFile = new File(dataFileDir + ETLConstants.FILE_SEP + String.valueOf(i) + ".txt");
        newFile.createNewFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
          for (int lineIndex = 0; lineIndex < 20; lineIndex++) {
            writer.write(Joiner.on(ETLConfig.fieldSeparator()).join(fields));
            writer.write(ETLConstants.NEWLINE);
            writer.flush();
          }
        }
      }

    }
  }
}
