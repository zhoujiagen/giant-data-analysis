package com.spike.giantdataanalysis.model.logic.relational.expression;

/** 更名表达式. */
public class RelationalAlgebraRenameExpression extends RelationalAlgebraBinaryOperandExpression {

  RelationalAlgebraRenameExpression(RelationalAlgebraExpression first,
      RelationalAlgebraExpression second) {
    super(first, second);
  }
}