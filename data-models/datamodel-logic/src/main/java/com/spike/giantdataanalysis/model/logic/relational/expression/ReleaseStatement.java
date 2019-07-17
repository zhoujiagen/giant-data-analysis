package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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

}
