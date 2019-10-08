package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 savepointStatement
    : SAVEPOINT uid
    ;
 * </pre>
 */
public class SavepointStatement implements TransactionStatement {

  public final Uid uid;

  SavepointStatement(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("SAVEPOINT ").append(uid.literal());
    return sb.toString();
  }
}
