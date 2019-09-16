package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Xid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("XA END ").append(xid.literal());
    if (Boolean.TRUE.equals(suspend)) {
      sb.append(" SUSPEND");
      if (Boolean.TRUE.equals(forMigrate)) {
        sb.append(" FOR MIGRATE");
      }
    }
    return sb.toString();
  }
}
