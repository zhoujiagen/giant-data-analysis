package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 交表达式. */
public class RelationalAlgebraIntersectionExpression
    extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraIntersectionExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}