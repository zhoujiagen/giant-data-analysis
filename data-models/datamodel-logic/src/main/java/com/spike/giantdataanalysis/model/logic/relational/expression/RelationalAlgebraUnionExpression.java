package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 并表达式. */
public class RelationalAlgebraUnionExpression extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraUnionExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}