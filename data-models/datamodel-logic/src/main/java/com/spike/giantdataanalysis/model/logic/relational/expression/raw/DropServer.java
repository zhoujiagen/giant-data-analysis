package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

/**
 * <pre>
 dropServer
    : DROP SERVER ifExists? uid
    ;
 * </pre>
 */
public class DropServer implements DdlStatement {
  public final IfExists ifExists;
  public final Uid uid;

  DropServer(IfExists ifExists, Uid uid) {
    Preconditions.checkArgument(uid != null);

    this.ifExists = ifExists;
    this.uid = uid;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP SERVER ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    sb.append(uid.literal());
    return sb.toString();
  }
}
