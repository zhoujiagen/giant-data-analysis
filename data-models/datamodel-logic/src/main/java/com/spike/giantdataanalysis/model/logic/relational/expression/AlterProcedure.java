package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

/**
 * <pre>
 alterProcedure
    : ALTER PROCEDURE fullId routineOption*
    ;
 * </pre>
 */
public class AlterProcedure implements DdlStatement {
  public final FullId fullId;
  public final List<RoutineOption> routineOptions;

  AlterProcedure(FullId fullId, List<RoutineOption> routineOptions) {
    Preconditions.checkArgument(fullId != null);

    this.fullId = fullId;
    this.routineOptions = routineOptions;
  }

}
