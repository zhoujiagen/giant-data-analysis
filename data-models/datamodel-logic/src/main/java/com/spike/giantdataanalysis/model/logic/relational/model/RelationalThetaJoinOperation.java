package com.spike.giantdataanalysis.model.logic.relational.model;

import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * Theta连接操作.
 */
public class RelationalThetaJoinOperation extends RelationalJoinOperation {

  public final Expression condition;

  public RelationalThetaJoinOperation(RelationalRelation first, RelationalRelation second,
      Expression condition) {
    super(first, second);
    this.condition = condition;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.THETA_JOIN;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("[").append(condition.literal()).append("]");
    sb.append("(").append(first.literal()).append(", ").append(second.literal()).append(")");
    return sb.toString();
  }

  @Override
  public RelationalRelation eval(RelationalEvaluationContext context)
      throws RelationalEvaluationError {
    // TODO Implement RelationalExpression.eval
    return null;
  }

  @Override
  public RelationalRelation result(String alias) {
    // TODO Implement RelationalOperation.result
    // TODO(zhoujiagen) do after extract attribute on conditions, example see P.47
    return null;
  }

}