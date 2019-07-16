package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 连接表达式. */
public class RelationalAlgebraJoinExpression extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraJoinExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}