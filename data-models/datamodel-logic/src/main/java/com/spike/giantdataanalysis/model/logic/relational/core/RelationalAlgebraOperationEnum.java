package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 关系代数运算.
 * <p>
 * <li>参考: https://en.wikipedia.org/wiki/Relational_algebra
 * <li>符号: Greek and Coptic in https://en.wikipedia.org/wiki/List_of_Unicode_characters
 */
public enum RelationalAlgebraOperationEnum implements RelationalAlgebraEnum {
  SELECT("σ", 2), // 选择
  PROJECT("Π", 2), // 投影
  // GENERALIZED_PROJECTION("广义投影", 2), //

  INTERSECTION("∩", 2), // 交
  UNION("∪", 2), // 并
  DIFFERENCE("-", 2), // 差
  CARTESIAN_PRODUCT("×", 2), // 笛卡尔积, CROSS_PRODUCT

  RENAME("ρ", 2), // 更名

  NATURAL_JOIN("⋈", 2), // 自然连接
  THETA_JOIN("⋈θ", 3), // θ连接
  LEFT_OUTER_JOIN("⟕", 3), // 左外连接
  RIGHT_OUTER_JION("⟖", 3), // 右外连接
  FULL_OUTER_JOIN("⟗", 3), // 全外连接

  // AGGREGATE("∑", 2), // 聚集Д

  DUPLICATE_ELIMINATION("δ", 1), // 消除重复
  GROUP("γ", 2), // 分组
  SORT("τ", 2) // 排序
  ;

  public final String symbol;
  public final int operandArity;

  RelationalAlgebraOperationEnum(String symbol, int operandArity) {
    this.symbol = symbol;
    this.operandArity = operandArity;
  }

  @Override
  public String literal() {
    return symbol;
  }
}
