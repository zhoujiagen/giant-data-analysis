package com.spike.giantdataanalysis.etl.config;

import java.util.List;
import java.util.ResourceBundle;

import com.google.common.base.Splitter;
import com.spike.giantdataanalysis.etl.supports.ETLConstants;

/**
 * 配置参数
 */
public final class ETLConfig {

  public static final String RES = "etl";

  public static List<String> dataFileDirs() {
    String strValue = ResourceBundle.getBundle(RES).getString("ETL_DATA_FILE_DIR");
    if (strValue == null || "".equals(strValue)) return null;
    return Splitter.on(ETLConstants.COMMA).splitToList(strValue);
  }

  public static String progressFile() {
    String strValue = ResourceBundle.getBundle(RES).getString("ETL_PROGRESS_REPORT_FILE");
    return strValue;
  }

  public static int workSize() {
    String strValue = ResourceBundle.getBundle(RES).getString("ETL_WORKER_SIZE");
    return Integer.valueOf(strValue);
  }

  public static String fieldSeparator() {
    String strValue = ResourceBundle.getBundle(RES).getString("ETL_FIELD_SEPRATOR");
    return strValue;
  }
}
