package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 关系代数中二元表达式. */
public class RelationalAlgebraBinaryOperandExpression implements RelationalAlgebraExpression {
  protected final RelationalAlgebraExpression first;
  protected final RelationalAlgebraExpression second;

  RelationalAlgebraBinaryOperandExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    this.first = first;
    this.second = second;
  }

  public RelationalAlgebraExpression first() {
    return first;
  }

  public RelationalAlgebraExpression second() {
    return second;
  }
}