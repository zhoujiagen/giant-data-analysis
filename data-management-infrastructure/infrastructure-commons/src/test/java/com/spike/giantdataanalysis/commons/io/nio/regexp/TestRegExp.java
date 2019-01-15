package com.spike.giantdataanalysis.commons.io.nio.regexp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestRegExp {

  @Test
  public void exceptionAnalysisInLog() {
    String dirPathString = "D:/sts-workspace/javaiospike/src/test/resources";
    Path path = Paths.get(dirPathString, "sample_exception.txt");
    Path outPath = Paths.get(dirPathString, "out_" + path.getFileName().toString());
    Charset cs = Charset.forName("UTF-8");

    // pattern 1
    // String exceptionRegex =
    // ".+\\.(\\w+Exception):(.+nested.+(Exception:)*.+)$";

    // pattern 2
    // String exceptionRegex =
    // "(.+Exception):(.+nested.+(Exception:)*.+)$";// may contain timestamp

    // pattern 3
    String exceptionRegex = ".+\\.(\\w+Exception):(.+(Exception:)*.+)$";

    Pattern exceptionPattern = Pattern.compile(exceptionRegex);
    Matcher matcher = null;

    // 异常名称-原因
    Map<String, String> exceptionNameReasonMap = new HashMap<String, String>();
    // 异常次数计数
    Map<String, Integer> exceptionNameCountMap = new HashMap<String, Integer>();
    // 异常附加信息计数
    Map<String, Object> exceptionNameAdditionalInfoMap = new HashMap<String, Object>();

    try (BufferedReader reader = Files.newBufferedReader(path, cs);
        BufferedWriter writer = Files.newBufferedWriter(outPath, cs);) {
      String line = null;
      boolean isException = false;
      String exceptionName = null, exceptionReason = null;
      String currentExceptionName = null;// 当前异常名称

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
            if (line.contains("com.spike")) {
              if (!exceptionNameAdditionalInfoMap.containsKey(currentExceptionName)) {
                exceptionNameAdditionalInfoMap.put(currentExceptionName, line);
              }
            }
          } else {
            isException = false;
          }
        }
      } // end of while

      for (String key : exceptionNameAdditionalInfoMap.keySet()) {
        System.out.println(key + "\n" + exceptionNameAdditionalInfoMap.get(key));
        writer.write(key + "\n" + exceptionNameAdditionalInfoMap.get(key));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void demoOfRepetiveSubSequenceMatch() {
    String input = "<p>aaaaaaaaaaa<br/></p><p><br/></p><p><br/></p>"
        + "<p><img src=\"http://demo.com/image/20150407/89071428386945282.gif\" style=\"\" title=\"89071428386945282.gif\"/></p>"
        + "<p><img src=\"http://demo.com/image/20150407/68921428386945298.png\" style=\"\" title=\"68921428386945298.png\"/></p>"
        + "<p><img src=\"http://demo.com/image/20150407/11111428386945309.png\" style=\"\" title=\"11111428386945309.png\"/></p>";

    String subSequencePatternString = "<img src=\"([^\"]+)\"\\sstyle=\"\"\\stitle=\"([^\"]+)\"/>";
    Pattern pattern = Pattern.compile(subSequencePatternString);
    Matcher matcher = pattern.matcher(input);

    List<String> srces = new ArrayList<String>();
    while (matcher.find()) {
      // the group: 1
      srces.add(input.substring(matcher.start(1), matcher.end(1)));
    }

    assertEquals(3, srces.size());

    assertEquals("http://demo.com/image/20150407/89071428386945282.gif", srces.get(0));
    assertEquals("http://demo.com/image/20150407/68921428386945298.png", srces.get(1));
    assertEquals("http://demo.com/image/20150407/11111428386945309.png", srces.get(2));
  }

  @Test
  public void demoOfAllMatch() {
    String input = "<p>aaaaaaaaaaa<br/></p><p><br/></p><p><br/></p>"
        + "<p><img src=\"http://demo.com/image/20150407/89071428386945282.gif\" style=\"\" title=\"89071428386945282.gif\"/></p>"
        + "<p><img src=\"http://demo.com/image/20150407/68921428386945298.png\" style=\"\" title=\"68921428386945298.png\"/></p>"
        + "<p><img src=\"http://demo.com/image/20150407/11111428386945309.png\" style=\"\" title=\"11111428386945309.png\"/></p>";

    String inputPatternString =
        ".*(<p><img src=\"([^\"]+)\"\\sstyle=\"\"\\stitle=\"([^\"]+)\"/>.*</p>)+.*";
    Pattern pattern = Pattern.compile(inputPatternString);
    Matcher matcher = pattern.matcher("");
    matcher.reset(input);

    assertTrue(matcher.matches());

    System.out.println(matcher.groupCount());

    assertEquals(input, matcher.group());
  }

  @Test
  public void navie() {
    assertTrue(true);
  }
}
