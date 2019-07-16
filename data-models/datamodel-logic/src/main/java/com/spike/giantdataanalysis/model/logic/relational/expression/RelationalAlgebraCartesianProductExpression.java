package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 笛卡尔积表达式. */
public class RelationalAlgebraCartesianProductExpression
    extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraCartesianProductExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}