package com.spike.giantdataanalysis.commons.io.nio2.walk.loganalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Log Analysis Task, a Callable
 * @author zhoujiagen
 */
public class ExampleLogAnalysisTask implements Callable<Path> {
  // interested package prefix of exception stack
  private static final String PACKAGE_PREFIX = "com.spike";
  // exception log lines' reguar exception
  private static final String EXCEPTION_PATTERN =
      ".+\\.(\\w+Exception):(.+(nested)*.+(Exception:)*.+)$";
  // the summary file path
  private static final String GATHER_RESULT_DIR = "D:/a.txt";

  private Path path;

  public ExampleLogAnalysisTask(Path path) {
    this.path = path;
  }

  @Override
  public Path call() throws Exception {
    System.out.println("start Task: " + this.path.toString());
    doCall();
    return this.path;
  }

  private void doCall() {
    Path outPath =
        Paths.get(this.path.getParent().toString(), "out_" + this.path.getFileName().toString());
    // System.out.println("outPath=" + outPath.getFileName());
    Charset cs = Charset.forName("UTF-8");

    String exceptionRegex = EXCEPTION_PATTERN;
    Pattern exceptionPattern = Pattern.compile(exceptionRegex);
    Matcher matcher = null;

    // map of exceptionName-causes
    Map<String, String> exceptionNameReasonMap = new HashMap<String, String>();
    // map of exception counting
    Map<String, Integer> exceptionNameCountMap = new HashMap<String, Integer>();
    // exception additional message
    Map<String, Object> exceptionNameAdditionalInfoMap = new HashMap<String, Object>();

    try (BufferedReader reader = Files.newBufferedReader(path, cs);
        BufferedWriter writer = Files.newBufferedWriter(outPath, cs);
        // append to summary file, hard coded
        BufferedWriter writerAll = Files.newBufferedWriter(Paths.get(GATHER_RESULT_DIR), cs,
          StandardOpenOption.APPEND, StandardOpenOption.CREATE);) {
      String line = null;
      boolean isException = false;
      String exceptionName = null, exceptionReason = null;
      String currentExceptionName = null;

      while ((line = reader.readLine()) != null) {
        if (Pattern.matches(exceptionRegex, line)) {
          isException = true;
          matcher = exceptionPattern.matcher(line);
          while (matcher.find()) {
            // group1: exception name
            exceptionName = line.substring(matcher.start(1), matcher.end(1));
            currentExceptionName = exceptionName;
            // group2: reason
            exceptionReason = line.substring(matcher.start(2), matcher.end(2));

            exceptionNameReasonMap.put(exceptionName, exceptionReason);
            if (exceptionNameCountMap.containsKey(currentExceptionName)) {
              exceptionNameCountMap.put(currentExceptionName,
                exceptionNameCountMap.get(currentExceptionName) + 1);
            } else {
              exceptionNameCountMap.put(currentExceptionName, 1);
            }
          }

          // read next lines to get more specific information
          continue;
        }

        if (isException) {
          if (line.contains("at ") || line.contains("Caused by")) {
            if (line.contains(PACKAGE_PREFIX)) {
              // find first match
              if (!exceptionNameAdditionalInfoMap.containsKey(currentExceptionName)) {
                exceptionNameAdditionalInfoMap.put(currentExceptionName, line);
              }
            }
          } else {// skip this exception
            isException = false;
          }
        }
      } // end of while

      writerAll.write("\n" + this.path.toString() + "\n");
      for (String key : exceptionNameAdditionalInfoMap.keySet()) {
        System.out.println(key + "\n" + exceptionNameAdditionalInfoMap.get(key));
        // populate result in file in each directory
        // writer.write(key + "[" + exceptionNameCountMap.get(key) +
        // "]\n"
        // + exceptionNameAdditionalInfoMap.get(key) + "\n");

        // populate result in summary file
        writerAll.write(key + "[" + exceptionNameCountMap.get(key) + "]\n"
            + exceptionNameAdditionalInfoMap.get(key) + "\n");
      }
      writerAll.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
