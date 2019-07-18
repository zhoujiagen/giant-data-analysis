package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;

/**
 * <pre>
 xaPrepareStatement
    : XA PREPARE xid
    ;
 * </pre>
 */
public class XaPrepareStatement implements ReplicationStatement {

  public final Xid xid;

  XaPrepareStatement(Xid xid) {
    Preconditions.checkArgument(xid != null);

    this.xid = xid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
