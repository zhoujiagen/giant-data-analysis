package com.spike.giantdataanalysis.commons.lang;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @see SimpleDateFormat
 * @author zhoujiagen
 */
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {
  public static final SimpleDateFormat DEFAULT_DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public static final SimpleDateFormat DATE_YMD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  /** 当前日期，包括时分秒 */
  public static String now() {
    return DEFAULT_DATE_FORMAT.format(new Date());
  }

  public static String now(SimpleDateFormat sdf) {
    return sdf.format(new Date());
  }

}
