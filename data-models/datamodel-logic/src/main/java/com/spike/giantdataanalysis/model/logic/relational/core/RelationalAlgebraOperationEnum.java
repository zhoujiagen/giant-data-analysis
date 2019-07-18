package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 关系代数运算.
 */
public enum RelationalAlgebraOperationEnum implements RelationalAlgebraEnum {
  SELECT("选择", 2), //
  PROJECT("投影", 2), //
  // GENERALIZED_PROJECTION("广义投影", 2), //
  INTERSECTION("交", 2), //
  UNION("并", 2), //
  DIFFERENCE("差", 2), //
  CARTESIAN_PRODUCT("笛卡尔积", 2), //
  RENAME("更名", 2), //
  ASSIGNMENT("赋值", 2), //
  NATURAL_JOIN("自然连接", 2), //
  THETA_JOIN("θ连接", 3), //
  LEFT_OUTER_JOIN("左外连接", 2), //
  RIGHT_OUTER_JION("右外连接", 2), //
  FULL_OUTER_JOIN("全外连接", 2), //
  AGGREGATE("聚集", 2);

  public final String description;
  public final int operandArity;

  RelationalAlgebraOperationEnum(String description, int operandArity) {
    this.description = description;
    this.operandArity = operandArity;
  }

}
