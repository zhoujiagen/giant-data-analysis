package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Xid;

/**
 * <pre>
 xaCommitWork
    : XA COMMIT xid (ONE PHASE)?
    ;
 * </pre>
 */
public class XaCommitWork implements ReplicationStatement {

  public final Xid xid;
  public final Boolean onePhase;

  XaCommitWork(Xid xid, Boolean onePhase) {
    Preconditions.checkArgument(xid != null);

    this.xid = xid;
    this.onePhase = onePhase;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("XA COMMIT ").append(xid.literal());
    if (Boolean.TRUE.equals(onePhase)) {
      sb.append(" ONE PHASE");
    }
    return sb.toString();
  }
}
