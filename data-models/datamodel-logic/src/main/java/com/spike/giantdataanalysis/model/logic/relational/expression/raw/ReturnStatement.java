package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;

/**
 * <pre>
 returnStatement
    : RETURN expression
    ;
 * </pre>
 */
public class ReturnStatement implements CompoundStatement {
  public final Expression expression;

  ReturnStatement(Expression expression) {
    Preconditions.checkArgument(expression != null);

    this.expression = expression;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("RETURN ").append(expression.literal());
    return sb.toString();
  }
}
