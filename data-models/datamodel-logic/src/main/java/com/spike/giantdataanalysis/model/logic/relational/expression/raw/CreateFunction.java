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
 createFunction
    : CREATE ownerStatement?
    FUNCTION fullId
      '(' functionParameter? (',' functionParameter)* ')'
      RETURNS dataType
      routineOption*
    routineBody
    ;
 * </pre>
 */
public class CreateFunction implements DdlStatement {
  public final OwnerStatement ownerStatement;
  public final FullId fullId;
  public final List<FunctionParameter> functionParameters;
  public final DataType dataType;
  public final List<RoutineOption> routineOptions;
  public final RoutineBody routineBody;

  CreateFunction(OwnerStatement ownerStatement, FullId fullId,
      List<FunctionParameter> functionParameters, DataType dataType,
      List<RoutineOption> routineOptions, RoutineBody routineBody) {
    Preconditions.checkArgument(fullId != null);
    Preconditions.checkArgument(dataType != null);
    Preconditions.checkArgument(routineBody != null);

    this.ownerStatement = ownerStatement;
    this.fullId = fullId;
    this.functionParameters = functionParameters;
    this.dataType = dataType;
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
    sb.append("FUNCTION ").append(fullId.literal()).append("(");
    if (CollectionUtils.isNotEmpty(functionParameters)) {
      List<String> literals = Lists.newArrayList();
      for (FunctionParameter functionParameter : functionParameters) {
        literals.add(functionParameter.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
    }
    sb.append(") ");
    sb.append("RETURNS ").append(dataType.literal()).append(" ");
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
