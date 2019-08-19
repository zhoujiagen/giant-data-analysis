package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 投影操作.
 */
public class RelationalProjectOperation implements RelationalOperation {
  public final RelationalRelation relation;
  public final List<RelationalAttribute> attributes;

  public RelationalProjectOperation(RelationalRelation relation,
      List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(relation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes));

    this.relation = relation;
    this.attributes = attributes;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.PROJECT;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("[").append(MoreLiterals.literalAttributes(attributes)).append("]");
    sb.append("(").append(relation.literal()).append(")");
    return sb.toString();
  }

  @Override
  public String toString() {
    return this.literal();
  }

  @Override
  public RelationalRelation eval(RelationalEvaluationContext context)
      throws RelationalEvaluationError {
    // TODO Implement RelationalExpression.eval
    return null;
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}