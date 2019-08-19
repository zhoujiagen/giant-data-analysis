package com.spike.giantdataanalysis.model.logic.relational.model;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 选择操作.
 */
public class RelationalSelectOperation implements RelationalOperation {
  public final RelationalRelation relation;
  public final Expression condition;

  public RelationalSelectOperation(RelationalRelation relation, Expression condition) {
    Preconditions.checkArgument(relation != null);

    this.relation = relation;
    this.condition = condition;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.SELECT;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("[");
    if (condition != null) {
      sb.append(condition.literal());
    }
    sb.append("]");
    sb.append("(").append(relation.literal()).append(")");
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
    return RelationalModelFactory.makeRelation(alias, relation.attributes);
  }

}