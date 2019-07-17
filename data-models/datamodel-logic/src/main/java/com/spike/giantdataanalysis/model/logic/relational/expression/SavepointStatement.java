package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
}
