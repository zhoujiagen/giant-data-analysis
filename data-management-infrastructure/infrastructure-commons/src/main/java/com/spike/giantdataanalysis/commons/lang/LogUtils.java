package com.spike.giantdataanalysis.commons.lang;

/**
 * 简单的控制台输出日志工具类
 * @author zhoujiagen
 */
public final class LogUtils {
  private static final String INFO = "[ INFO]";
  private static final String WARN = "[ WARN]";
  private static final String DEBUG = "[DEBUG]";
  private static final String ERROR = "[ERROR]";

  public static void info(String message) {
    if (StringUtils.isBlank(message)) return;

    System.out.println(DateUtils.now() + StringUtils.TAB + INFO + StringUtils.TAB + message);
  }

  public static void warn(String message) {
    if (StringUtils.isBlank(message)) return;

    System.out.println(DateUtils.now() + StringUtils.TAB + WARN + StringUtils.TAB + message);
  }

  public static void debug(String message) {
    if (StringUtils.isBlank(message)) return;

    System.out.println(DateUtils.now() + StringUtils.TAB + DEBUG + StringUtils.TAB + message);
  }

  public static void error(String message, Throwable cause) {
    if (cause == null) return;

    String hint = DateUtils.now() + StringUtils.TAB + ERROR + StringUtils.TAB;

    if (StringUtils.isBlank(message)) {
      System.out.println(hint + message);
    } else {
      System.out.println(hint);
    }

    cause.printStackTrace();
  }

}
