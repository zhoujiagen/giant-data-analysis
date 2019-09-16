package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 iterateStatement
    : ITERATE uid
    ;
 * </pre>
 */
public class IterateStatement implements CompoundStatement {
  public final Uid uid;

  IterateStatement(Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("ITERATE ").append(uid.literal());
    return sb.toString();
  }
}
