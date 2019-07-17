package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;

/**
 * <pre>
 xaRollbackWork
    : XA ROLLBACK xid
    ;
 * </pre>
 */
public class XaRollbackWork implements ReplicationStatement {
  public final Xid xid;

  XaRollbackWork(Xid xid) {
    Preconditions.checkArgument(xid != null);

    this.xid = xid;
  }

}
