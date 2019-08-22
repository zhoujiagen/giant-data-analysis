package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 更名操作.
 */
public class RelationalRenameOperation implements RelationalOperation {
  public final RelationalRelation relation;
  // 新关系的名称
  public final String newRelationName;
  // 新关系中属性的名称, 为空时为原关系中属性名称, 不为空时为原关系中属性的新名称
  public final List<String> newAttributeNames;

  public RelationalRenameOperation(RelationalRelation relation, String newRelationName,
      List<String> newAttributeNames) {
    Preconditions.checkArgument(relation != null);
    Preconditions.checkArgument(StringUtils.isNotBlank(newRelationName));
    if (newAttributeNames != null) {
      Preconditions.checkArgument(newAttributeNames.size() == relation.attributes.size());
    }

    this.relation = relation;
    this.newRelationName = newRelationName;
    this.newAttributeNames = newAttributeNames;
  }

  @Override
  public RelationalAlgebraOperationEnum operationType() {
    return RelationalAlgebraOperationEnum.RENAME;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("[").append(newRelationName).append("(")
        .append(Joiner.on(" ").join(newAttributeNames)).append(")").append("]");
    sb.append("(").append(relation.literal()).append(")");
    return sb.toString();
  }

  @Override
  public RelationalRelation result(String alias) {
    List<RelationalAttribute> attributes = Lists.newArrayList();
    if (newAttributeNames != null) {
      int size = newAttributeNames.size();
      for (int i = 0; i < size; i++) {
        attributes.add(relation.attributes.get(i).copy(newAttributeNames.get(i)));
      }
    } else {
      attributes.addAll(relation.attributes);
    }
    return RelationalModelFactory.makeRelation(alias, attributes);
  }

}