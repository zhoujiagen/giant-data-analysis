package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Expressions;

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

}