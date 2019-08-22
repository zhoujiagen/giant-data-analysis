package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 排序操作.
 */
public class RelationalSortingOperation implements RelationalOperation {

  public final RelationalRelation relation;
  public final List<RelationalAttribute> attributes;

  public RelationalSortingOperation(RelationalRelation relation,
      List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(relation != null);
    if (CollectionUtils.isNotEmpty(attributes)) {
      for (RelationalAttribute attribute : attributes) {
        Preconditions.checkArgument(RelationalUtils.contains(relation.attributes, attribute));
      }
    }

    this.relation = relation;
    this.attributes = attributes;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.SORT;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    List<String> attributeNames = Lists.newArrayList();
    for (RelationalAttribute attribute : attributes) {
      attributeNames.add(attribute.name);
    }
    sb.append("[").append(Joiner.on(", ").join(attributeNames)).append("]");
    sb.append("(").append(relation.name).append(")");
    return sb.toString();
  }

  @Override
  public RelationalRelation result(String alias) {
    return RelationalModelFactory.makeRelation(alias, relation.attributes);
  }

}
