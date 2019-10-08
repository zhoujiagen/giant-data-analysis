package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 releaseStatement
    : RELEASE SAVEPOINT uid
    ;
 * </pre>
 */
public class ReleaseStatement implements TransactionStatement {
  public final Uid uid;

  ReleaseStatement(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("RELEASE SAVEPOINT ").append(uid.literal());
    return sb.toString();
  }
}
