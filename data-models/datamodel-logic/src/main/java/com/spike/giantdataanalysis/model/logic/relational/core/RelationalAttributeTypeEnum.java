package com.spike.giantdataanalysis.model.logic.relational.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 数据类型: 参考MySQL的数据类型.
 * 
 * <pre>
 * YEAR ，字节数为1，取值范围为“1901——2155”
 * DATE，字节数为4，取值范围为“1000-01-01——9999-12-31”
 * TIME，字节数为3，取值范围为“-838:59:59——838:59:59”
 * DATETIME，字节数为8，取值范围为“1000-01-01 00:00:00——9999-12-31 23:59:59”
 * TIMESTAMP，字节数为4，取值范围为“19700101080001——20380119111407”
 * </pre>
 */
public enum RelationalAttributeTypeEnum {
  DECIMAL(0, BigDecimal.class), //
  TINY(1, Short.class), //
  SHORT(2, Short.class), //
  LONG(3, Long.class), //
  FLOAT(4, Float.class), //
  DOUBLE(5, Double.class), //
  NULL(6, Void.class), //
  TIMESTAMP(7, Date.class), //
  LONGLONG(8, BigInteger.class), //
  INT24(9, Integer.class), //
  DATE(10, Date.class), //
  TIME(11, Date.class), //
  DATETIME(12, Date.class), //
  YEAR(13, Date.class), //
  NEWDATE(14, Date.class), //
  VARCHAR(15, String.class), //
  BIT(16, Boolean.class), //
  TIMESTAMP2(17, Date.class), //
  DATETIME2(18, Date.class), //
  TIME2(19, Date.class), //
  JSON(20, Object.class), //
  NEWDECIMAL(21, BigDecimal.class), //
  ENUM(247, List.class), // 枚举: String
  SET(248, Set.class), // 集合: String
  TINY_BLOB(249, Blob.class), //
  MEDIUM_BLOB(250, Blob.class), //
  LONG_BLOB(251, Blob.class), //
  BLOB(252, Blob.class), //
  VAR_STRING(253, String.class), //
  STRING(254, String.class), //
  GEOMETRY(255, String.class);

  public final int id;
  public final Class<?> reprClass;

  RelationalAttributeTypeEnum(int id, Class<?> reprClass) {
    this.id = id;
    this.reprClass = reprClass;
  }

  public static class Blob {
    public byte[] data;
  }

}
