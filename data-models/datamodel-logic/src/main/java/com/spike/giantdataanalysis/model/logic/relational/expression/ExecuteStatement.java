package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UserVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 executeStatement
    : EXECUTE uid (USING userVariables)?
    ;
 * </pre>
 */
public class ExecuteStatement implements PreparedStatement {
  public final Uid uid;
  public final UserVariables userVariables;

  ExecuteStatement(Uid uid, UserVariables userVariables) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
    this.userVariables = userVariables;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
