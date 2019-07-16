package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 聚集表达式. */
public class RelationalAlgebraAggregateExpression extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraAggregateExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}