package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Xid;

/**
 * <pre>
 xaRecoverWork
    : XA RECOVER (CONVERT xid)?
    ;
 * </pre>
 */
public class XaRecoverWork implements ReplicationStatement {
  public final Xid xid;

  XaRecoverWork(Xid xid) {
    this.xid = xid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("XA RECOVER");
    if (xid != null) {
      sb.append(" CONVERT ").append(xid.literal());
    }
    return sb.toString();
  }
}
