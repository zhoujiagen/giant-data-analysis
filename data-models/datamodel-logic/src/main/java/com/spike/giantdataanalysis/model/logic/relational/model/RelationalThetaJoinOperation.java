package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.REScope;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * Theta连接操作.
 */
public class RelationalThetaJoinOperation extends RelationalJoinOperation {

  public final Expression condition;
  // may be a higher scope than 'expression'
  public final REScope interpreteScope;

  public RelationalThetaJoinOperation(Expression condition, RelationalRelation first,
      RelationalRelation second, REScope interpreteScope) {
    super(first, second);
    Preconditions.checkArgument(condition != null);

    this.condition = condition;
    this.interpreteScope = interpreteScope;
  }

  public RelationalThetaJoinOperation(Expression condition, REScope interpreteScope,
      RelationalRelation... relations) {
    super(relations);
    Preconditions.checkArgument(condition != null);

    this.condition = condition;
    this.interpreteScope = interpreteScope;
  }

  public RelationalThetaJoinOperation(Expression condition, REScope interpreteScope,
      List<RelationalRelation> relations) {
    super(relations);
    Preconditions.checkArgument(condition != null);

    this.condition = condition;
    this.interpreteScope = interpreteScope;

  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.THETA_JOIN;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("[").append(condition.literal()).append("]");
    sb.append(super.literal());
    return sb.toString();
  }

  @Override
  public RelationalRelation result(String alias) {
    // TODO Implement RelationalOperation.result
    // TODO(zhoujiagen) do after extract attribute on conditions, example see P.47
    return null;
  }

}