package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Xid;

/**
 * <pre>
 xaEndTransaction
    : XA END xid (SUSPEND (FOR MIGRATE)?)?
    ;
 * </pre>
 */
public class XaEndTransaction implements ReplicationStatement {
  public final Xid xid;
  public final Boolean suspend;
  public final Boolean forMigrate;

  XaEndTransaction(Xid xid, Boolean suspend, Boolean forMigrate) {
    Preconditions.checkArgument(xid != null);

    this.xid = xid;
    this.suspend = suspend;
    this.forMigrate = forMigrate;
  }

}
