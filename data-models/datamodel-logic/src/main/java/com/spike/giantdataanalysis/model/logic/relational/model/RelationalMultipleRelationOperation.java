package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 关系涉及多关系的操作.
 */
public abstract class RelationalMultipleRelationOperation implements RelationalOperation {
  public final List<RelationalRelation> relations = Lists.newArrayList();

  public RelationalMultipleRelationOperation(RelationalRelation first, RelationalRelation second) {
    this.relations.add(first);
    this.relations.add(second);
  }

  public RelationalMultipleRelationOperation(RelationalRelation... relations) {
    Preconditions.checkArgument(relations != null && relations.length > 2);

    for (RelationalRelation relation : relations) {
      this.relations.add(relation);
    }
  }

  public RelationalMultipleRelationOperation(List<RelationalRelation> relations) {
    Preconditions.checkArgument(relations != null && relations.size() > 2);

    this.relations.addAll(relations);
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(operationType().symbol);
    sb.append("(");
    List<String> literals = Lists.newArrayList();
    for (RelationalRelation relation : this.relations) {
      literals.add(relation.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    sb.append(")");
    return sb.toString();
  }

}