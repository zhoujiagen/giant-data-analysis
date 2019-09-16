package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;

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
  public final List<RoutineOption> routineOptions;
  public final RoutineBody routineBody;

  CreateProcedure(OwnerStatement ownerStatement, FullId fullId,
      List<ProcedureParameter> procedureParameters, List<RoutineOption> routineOptions,
      RoutineBody routineBody) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(routineBody != null);

    this.ownerStatement = ownerStatement;
    this.fullId = fullId;
    this.procedureParameters = procedureParameters;
    this.routineOptions = routineOptions;
    this.routineBody = routineBody;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE ");
    if (ownerStatement != null) {
      sb.append(ownerStatement.literal()).append(" ");
    }
    sb.append("PROCEDURE ").append(fullId.literal()).append(" ");
    sb.append("(");
    if (CollectionUtils.isNotEmpty(procedureParameters)) {
      List<String> literals = Lists.newArrayList();
      for (ProcedureParameter procedureParameter : procedureParameters) {
        literals.add(procedureParameter.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }
    sb.append(") ");
    if (CollectionUtils.isNotEmpty(routineOptions)) {
      List<String> literals = Lists.newArrayList();
      for (RoutineOption routineOption : routineOptions) {
        literals.add(routineOption.literal());
      }
      sb.append(Joiner.on(" ").join(literals)).append(" ");
    }
    sb.append(routineBody.literal());
    return sb.toString();
  }
}
