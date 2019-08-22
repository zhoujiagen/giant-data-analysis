package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 投影操作.
 */
public class RelationalProjectOperation implements RelationalOperation {
  public final RelationalRelation relation;
  // 投影属性
  public final List<String> attributeNames;

  public RelationalProjectOperation(RelationalRelation relation, List<String> attributeNames) {
    Preconditions.checkArgument(relation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributeNames));
    Preconditions.checkArgument(RelationalUtils.containsAll(relation.attributes, attributeNames));

    this.relation = relation;
    this.attributeNames = attributeNames;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.PROJECT;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("[").append(Joiner.on(", ").join(attributeNames)).append("]");
    sb.append("(").append(relation.literal()).append(")");
    return sb.toString();
  }

  @Override
  public String toString() {
    return this.literal();
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias,
      RelationalUtils.get(relation.attributes, attributeNames));
  }

}