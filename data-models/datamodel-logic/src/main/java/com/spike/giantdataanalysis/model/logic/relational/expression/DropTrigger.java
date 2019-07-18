package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

/**
 * <pre>
 dropTrigger
    : DROP TRIGGER ifExists? fullId
    ;
 * </pre>
 */
public class DropTrigger implements DdlStatement {
  public final IfExists ifExists;
  public final FullId fullId;

  DropTrigger(IfExists ifExists, FullId fullId) {
    Preconditions.checkArgument(fullId != null);

    this.ifExists = ifExists;
    this.fullId = fullId;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP TRIGGER ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    sb.append(fullId.literal());
    return sb.toString();
  }
}
