package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 
 */
public enum RelationalAlgebraJoinTypeEnum implements RelationalAlgebraEnum {
  /** 自然连接 */
  NATURAL_JOIN,
  /** 左外连接 */
  LEFT_OUTER_JOIN,
  /** 右外连接 */
  RIGHT_OUTER_JION,
  /** 全外连接 */
  FULL_OUTER_JOIN;

  @Override
  public String literal() {
    return name();
  }

}
