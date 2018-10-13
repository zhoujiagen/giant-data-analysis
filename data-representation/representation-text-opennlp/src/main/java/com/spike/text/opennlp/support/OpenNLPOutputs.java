package com.spike.text.opennlp.support;

import static com.spike.giantdataanalysis.commons.lang.StringUtils.NEWLINE;
import static com.spike.giantdataanalysis.commons.lang.StringUtils.REPEAT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * OpenNLP输出工具类
 * @author zhoujiagen
 */
public final class OpenNLPOutputs {

  private static final String OUTER_SYMBOL = "=";
  private static final String INNER_SYMBOL = "-";
  private static final int REPEAT_SIZE = 50;

  public static String render(String title, String[] result) {
    if (result == null) return EMPTY;

    StringBuilder sb = new StringBuilder(NEWLINE);

    sb.append(REPEAT(OUTER_SYMBOL, REPEAT_SIZE)).append(NEWLINE);
    sb.append(title).append(NEWLINE);
    sb.append(REPEAT("-", REPEAT_SIZE)).append(NEWLINE);
    sb.append("Total results: ").append(result.length).append(NEWLINE);
    sb.append(REPEAT(INNER_SYMBOL, REPEAT_SIZE)).append(NEWLINE);
    for (int i = 0, len = result.length; i < len; i++) {
      sb.append("[" + i + "] ").append(result[i]).append(NEWLINE);
    }
    sb.append(REPEAT(OUTER_SYMBOL, REPEAT_SIZE)).append(NEWLINE);

    return sb.toString();
  }

  public static String render(String title, String[]... strings) {
    if (strings == null || strings.length == 0) return EMPTY;
    int length = strings.length;

    int size = Integer.MAX_VALUE;
    for (String[] string : strings) {
      if (string == null || string.length == 0) return EMPTY;
      if (size > string.length) size = string.length;
    }

    StringBuilder sb = new StringBuilder(NEWLINE);

    sb.append(REPEAT(OUTER_SYMBOL, REPEAT_SIZE)).append(NEWLINE);
    sb.append(title).append(NEWLINE);
    sb.append(REPEAT("-", REPEAT_SIZE)).append(NEWLINE);
    sb.append("Total results: ").append(size).append(NEWLINE);
    sb.append(REPEAT(INNER_SYMBOL, REPEAT_SIZE)).append(NEWLINE);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < length; j++) {
        sb.append(strings[j][i]);
        if (j != length - 1) {
          sb.append("/");
        }
      }
      sb.append(SPACE);
    }
    sb.append(NEWLINE);
    sb.append(REPEAT(OUTER_SYMBOL, REPEAT_SIZE)).append(NEWLINE);

    return sb.toString();

  }

}
