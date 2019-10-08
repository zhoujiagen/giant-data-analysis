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
public enum RelationalAttributeTypeEnum implements RelationalAlgebraEnum {
  DECIMAL(0, BigDecimal.class), //

  REAL(-1, BigDecimal.class), //
  DEC(-1, BigDecimal.class), //
  FIXED(-1, BigDecimal.class), //
  NUMERIC(-1, BigDecimal.class), //

  TINY(1, Short.class), //
  SHORT(2, Short.class), //
  LONG(3, Long.class), //
  FLOAT(4, Float.class), //
  DOUBLE(5, Double.class), //
  NULL(6, Void.class), //
  TIMESTAMP(7, Date.class), //
  LONGLONG(8, BigInteger.class), //

  INT24(9, Integer.class), //
  TINYINT(-1, Integer.class), //
  SMALLINT(-1, Integer.class), //
  MEDIUMINT(-1, Integer.class), //
  INT(-1, Integer.class), //
  INTEGER(-1, Integer.class), //
  BIGINT(-1, BigInteger.class), //

  DATE(10, Date.class), //
  TIME(11, Date.class), //
  DATETIME(12, Date.class), //
  YEAR(13, Date.class), //
  NEWDATE(14, Date.class), //

  VARCHAR(15, String.class), //
  CHAR(-1, String.class), //
  CHARACTER(-1, String.class), //
  NVARCHAR(-1, String.class), //

  BIT(16, Boolean.class), //
  BOOL(-1, Boolean.class), //
  BOOLEAN(-1, Boolean.class), //
  SERIAL(-1, Boolean.class), // ???

  TIMESTAMP2(17, Date.class), //
  DATETIME2(18, Date.class), //
  TIME2(19, Date.class), //

  JSON(20, Object.class), //

  NEWDECIMAL(21, BigDecimal.class), //

  ENUM(247, List.class), // 枚举: String
  SET(248, Set.class), // 集合: String

  TINY_BLOB(249, RelationalBlobAttributeValue.class), //
  MEDIUM_BLOB(250, RelationalBlobAttributeValue.class), //
  LONG_BLOB(251, RelationalBlobAttributeValue.class), //
  BLOB(252, RelationalBlobAttributeValue.class), //

  VAR_STRING(253, String.class), //
  STRING(254, String.class), //

  TINYTEXT(-1, String.class), //
  TEXT(-1, String.class), //
  MEDIUMTEXT(-1, String.class), //
  LONGTEXT(-1, String.class), //

  BINARY(-1, RelationalBlobAttributeValue.class), //
  VARBINARY(-1, RelationalBlobAttributeValue.class), //

  GEOMETRY(255, String.class), //

  GEOMETRYCOLLECTION(-1, Object.class), //
  GEOMCOLLECTION(-1, Object.class), //
  LINESTRING(-1, Object.class), //
  MULTILINESTRING(-1, Object.class), //
  MULTIPOINT(-1, Object.class), //
  MULTIPOLYGON(-1, Object.class), //
  POINT(-1, Object.class), //
  POLYGON(-1, Object.class);

  public final int id;
  public final Class<?> reprClass;

  RelationalAttributeTypeEnum(int id, Class<?> reprClass) {
    this.id = id;
    this.reprClass = reprClass;
  }

  @Override
  public String literal() {
    return name();
  }
}
