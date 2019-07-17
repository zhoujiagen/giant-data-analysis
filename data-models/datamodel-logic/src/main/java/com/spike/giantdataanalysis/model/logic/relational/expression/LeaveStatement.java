package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

/**
 * <pre>
 leaveStatement
    : LEAVE uid
    ;
 * </pre>
 */
public class LeaveStatement implements CompoundStatement {
  public final Uid uid;

  LeaveStatement(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

}
