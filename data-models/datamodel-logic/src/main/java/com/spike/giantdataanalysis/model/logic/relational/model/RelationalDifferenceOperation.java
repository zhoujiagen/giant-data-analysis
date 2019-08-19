package com.spike.giantdataanalysis.model.logic.relational.model;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 差操作.
 */
public class RelationalDifferenceOperation extends RelationalBinaryOperation {

  public RelationalDifferenceOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.DIFFERENCE;
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
    Preconditions
        .checkArgument(RelationalUtils.equals(first.attributes, second.attributes));

    String name = RelationalUtils.temporaryRelationName(first, second);
    return RelationalModelFactory.makeRelation(name, first.attributes);

  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, first.attributes);
  }

}