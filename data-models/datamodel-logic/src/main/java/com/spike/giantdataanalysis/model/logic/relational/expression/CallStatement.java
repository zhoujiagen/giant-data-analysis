package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

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

}