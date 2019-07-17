package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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

}
