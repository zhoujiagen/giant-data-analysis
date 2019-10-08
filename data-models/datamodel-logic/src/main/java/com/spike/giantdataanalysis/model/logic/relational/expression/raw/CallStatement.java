package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
callStatement
  : CALL fullId
    (
      '(' (constants | expressions)? ')'
    )?
  ;
 * </pre>
 */
public class CallStatement implements DmlStatement {
  public final FullId fullId;
  public final Constants constants;
  public final Expressions expressions;

  CallStatement(FullId fullId, Constants constants, Expressions expressions) {
    Preconditions.checkArgument(fullId != null);

    this.fullId = fullId;
    this.constants = constants;
    this.expressions = expressions;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CALL ").append(fullId.literal());
    if (constants != null) {
      sb.append("(").append(constants.literal()).append(")");
    } else if (expressions != null) {
      sb.append("(").append(expressions.literal()).append(")");
    } else {
      sb.append("()");
    }
    return sb.toString();
  }
}