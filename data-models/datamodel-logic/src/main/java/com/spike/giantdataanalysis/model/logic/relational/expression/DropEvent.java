package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

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

}
