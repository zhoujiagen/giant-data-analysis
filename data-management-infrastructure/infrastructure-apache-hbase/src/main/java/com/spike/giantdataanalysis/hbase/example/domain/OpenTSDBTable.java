package com.spike.giantdataanalysis.hbase.example.domain;

/**
 * OpenTSDB HBase Schema常量类.
 */
public class OpenTSDBTable {

  // 表名
  /** CF: t. */
  public static final String T_TSDB = "tsdb";
  /** CF: id, name. */
  public static final String T_TSDB_UID = "tsdb-uid";
  /** CF: name. */
  public static final String T_TSDB_META = "tsdb-meta";
  /** CF: t. FIXME(zhoujiagen) more specific. */
  public static final String T_TSDB_TREE = "tsdb-tree";

  // 列族名
  public static final String CF_ID = "id";
  public static final String CF_NAME = "name";
  public static final String CF_T = "t";

  // 列名
  public static final String C_METRICS = "metrics";
  public static final String C_TAGK = "tagk";
  public static final String C_TAGV = "tagv";

}
