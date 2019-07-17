package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;

/**
 * <pre>
 createProcedure
    : CREATE ownerStatement?
    PROCEDURE fullId
      '(' procedureParameter? (',' procedureParameter)* ')'
      routineOption*
    routineBody
    ;
 * </pre>
 */
public class CreateProcedure implements DdlStatement {
  public final OwnerStatement ownerStatement;
  public final FullId fullId;
  public final List<ProcedureParameter> procedureParameters;
  public final RoutineOption routineOption;
  public final RoutineBody routineBody;

  CreateProcedure(OwnerStatement ownerStatement, FullId fullId,
      List<ProcedureParameter> procedureParameters, RoutineOption routineOption,
      RoutineBody routineBody) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(routineBody != null);

    this.ownerStatement = ownerStatement;
    this.fullId = fullId;
    this.procedureParameters = procedureParameters;
    this.routineOption = routineOption;
    this.routineBody = routineBody;
  }

}
