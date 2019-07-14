package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
 * expressions : expression (',' expression)*
 * </pre>
 */
public class Expressions implements RelationalAlgebraExpression {
  final List<Expression> expressions;

  Expressions(List<Expression> expressions) {
    Preconditions.checkArgument(expressions != null && expressions.size() > 0);

    this.expressions = expressions;
  }
}