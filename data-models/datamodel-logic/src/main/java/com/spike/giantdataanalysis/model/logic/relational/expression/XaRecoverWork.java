package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;

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
}
