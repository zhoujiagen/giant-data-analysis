package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Expressions;

/**
 * <pre>
doStatement
  : DO expressions
  ;
 * </pre>
 */
public class DoStatement implements DmlStatement {
  public final Expressions expressions;

  DoStatement(Expressions expressions) {
    Preconditions.checkArgument(expressions != null);

    this.expressions = expressions;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DO ").append(expressions.literal());
    return sb.toString();
  }
}