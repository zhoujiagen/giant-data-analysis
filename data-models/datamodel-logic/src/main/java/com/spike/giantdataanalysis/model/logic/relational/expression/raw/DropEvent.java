package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
 dropEvent
    : DROP EVENT ifExists? fullId
    ;
 * </pre>
 */
public class DropEvent implements DdlStatement {
  public final IfExists ifExists;
  public final FullId fullId;

  DropEvent(IfExists ifExists, FullId fullId) {
    Preconditions.checkArgument(fullId != null);

    this.ifExists = ifExists;
    this.fullId = fullId;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP EVENT ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    sb.append(fullId.literal());
    return sb.toString();
  }
}
