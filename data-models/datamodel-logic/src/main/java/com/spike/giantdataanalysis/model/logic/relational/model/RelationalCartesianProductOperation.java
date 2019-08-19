package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 笛卡尔积操作.
 */
public class RelationalCartesianProductOperation extends RelationalBinaryOperation {

  public RelationalCartesianProductOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.CARTESIAN_PRODUCT;
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
    List<RelationalAttribute> attributes = Lists.newArrayList();
    for (RelationalAttribute attribute : first.attributes) {
      attributes.add(attribute.copy(first.name + "." + attribute.name));
    }
    for (RelationalAttribute attribute : second.attributes) {
      attributes.add(attribute.copy(second.name + "." + attribute.name));
    }
    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}