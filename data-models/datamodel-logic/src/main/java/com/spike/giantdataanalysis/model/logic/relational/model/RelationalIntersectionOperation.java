package com.spike.giantdataanalysis.model.logic.relational.model;

import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 交操作.
 */
public class RelationalIntersectionOperation extends RelationalBinaryOperation {

  public RelationalIntersectionOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.INTERSECTION;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
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
    return RelationalModelFactory.makeRelation(alias, first.attributes);
  }

}