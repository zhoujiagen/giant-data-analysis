package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Xid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("XA ROLLBACK ").append(xid.literal());
    return sb.toString();
  }
}
