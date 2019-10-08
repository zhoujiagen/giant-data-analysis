package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 rollbackStatement
    : ROLLBACK WORK? TO SAVEPOINT? uid
    ;
 * </pre>
 */
public class RollbackStatement implements TransactionStatement {
  public final Uid uid;

  RollbackStatement(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ROLLBACK WORK TO SAVEPOINT ").append(uid.literal());
    return sb.toString();
  }
}
