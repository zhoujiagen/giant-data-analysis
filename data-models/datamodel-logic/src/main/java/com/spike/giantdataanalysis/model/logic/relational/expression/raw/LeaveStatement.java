package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("LEAVE ").append(uid.literal());
    return sb.toString();
  }
}
