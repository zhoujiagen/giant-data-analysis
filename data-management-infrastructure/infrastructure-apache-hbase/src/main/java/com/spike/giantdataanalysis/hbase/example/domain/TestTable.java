package com.spike.giantdataanalysis.hbase.example.domain;

import org.apache.hadoop.hbase.util.Bytes;

public class TestTable {

  public static final String T_NAME = "testtable";

  public static final byte[] ROW_1 = Bytes.toBytes("row-1");
  public static final byte[] ROW_2 = Bytes.toBytes("row-2");
  public static final String PREFIX_ROW = "row-";

  public static final byte[] CF_1 = Bytes.toBytes("colfam1");
  public static final byte[] CF_2 = Bytes.toBytes("colfam2");

  public static final byte[] C_1 = Bytes.toBytes("col-1");
  public static final byte[] C_2 = Bytes.toBytes("col-2");
  public static final String PREFIX_C = "col-";

  public static final byte[] V_5 = Bytes.toBytes("val-5");
  public static final String PREFIX_V = "val-";
}
