package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

/**
 * <pre>
 dropProcedure
    : DROP PROCEDURE ifExists? fullId
    ;
 * </pre>
 */
public class DropProcedure implements DdlStatement {
  public final IfExists ifExists;
  public final FullId fullId;

  DropProcedure(IfExists ifExists, FullId fullId) {
    Preconditions.checkArgument(fullId != null);

    this.ifExists = ifExists;
    this.fullId = fullId;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("DROP PROCEDURE ");
    if (ifExists != null) {
      sb.append(ifExists.literal()).append(" ");
    }
    sb.append(fullId.literal());
    return sb.toString();
  }
}
