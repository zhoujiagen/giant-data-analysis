package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;

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

}
