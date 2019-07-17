package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

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
}
