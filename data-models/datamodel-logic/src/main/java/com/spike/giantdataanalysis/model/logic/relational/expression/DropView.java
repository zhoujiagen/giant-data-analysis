package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonExpressons.IfExists;

/**
 * <pre>
 dropView
    : DROP VIEW ifExists?
      fullId (',' fullId)* dropType=(RESTRICT | CASCADE)?
    ;
 * </pre>
 */
public class DropView implements DdlStatement {
  public final IfExists ifExists;
  public final List<FullId> fullIds;
  public final DropTypeEnum dropType;

  DropView(IfExists ifExists, List<FullId> fullIds, DropTypeEnum dropType) {
    Preconditions.checkArgument(fullIds != null && fullIds.size() > 0);

    this.ifExists = ifExists;
    this.fullIds = fullIds;
    this.dropType = dropType;
  }

}
