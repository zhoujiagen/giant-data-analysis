package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 差表达式. */
public class RelationalAlgebraDifferenceExpression
    extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraDifferenceExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}