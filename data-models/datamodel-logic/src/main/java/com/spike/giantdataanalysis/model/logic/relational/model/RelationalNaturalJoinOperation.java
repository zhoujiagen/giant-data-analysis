package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationContext;
import com.spike.giantdataanalysis.model.logic.relational.RelationalEvaluationError;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 自然连接操作.
 */
public class RelationalNaturalJoinOperation extends RelationalJoinOperation {

  public RelationalNaturalJoinOperation(RelationalRelation first, RelationalRelation second) {
    super(first, second);
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.NATURAL_JOIN;
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
    // example see P.46
    List<RelationalAttribute> attributes = Lists.newArrayList();
    List<String> firstAttributeNames = Lists.newArrayList();
    List<String> secondAttributeNames = Lists.newArrayList();
    for (RelationalAttribute attribute : first.attributes) {
      firstAttributeNames.add(attribute.name);
    }
    for (RelationalAttribute attribute : second.attributes) {
      secondAttributeNames.add(attribute.name);
    }
    Triple<List<Integer>, List<Integer>, List<Integer>> compareResult =
        RelationalUtils.compareCollection(firstAttributeNames, secondAttributeNames);

    for (Integer index : compareResult.getLeft()) {
      attributes.add(first.attributes.get(index));
    }
    for (Integer index : compareResult.getMiddle()) {
      attributes.add(first.attributes.get(index));
    }
    for (Integer index : compareResult.getRight()) {
      attributes.add(second.attributes.get(index));
    }

    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}