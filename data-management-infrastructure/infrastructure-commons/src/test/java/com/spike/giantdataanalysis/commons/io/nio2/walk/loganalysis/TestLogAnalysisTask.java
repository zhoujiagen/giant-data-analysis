package com.spike.giantdataanalysis.commons.io.nio2.walk.loganalysis;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Test;

public class TestLogAnalysisTask {
  @Test
  public void testCall() throws Exception {
    Path path = FileSystems.getDefault().getPath("D:/sts-workspace/javaiospike/src/test/resources",
      "sample_exception.txt");
    ExampleLogAnalysisTask task = new ExampleLogAnalysisTask(path);
    task.call();
  }
}
