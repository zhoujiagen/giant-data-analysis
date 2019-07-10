package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;

/** 关系键 */
public abstract class RelationalRelationKey implements Comparable<RelationalRelationKey> {
  protected RelationalRelation relation;

  public abstract RelationalRelationKeyTypeEnum keyType();

  public abstract String name();

  public abstract List<RelationalAttribute> attributes();

  RelationalRelationKey() {
  }

  public RelationalRelation relation() {
    return relation;
  }

  public void setRelation(RelationalRelation relation) {
    this.relation = relation;
  }

  @Override
  public int compareTo(RelationalRelationKey o) {
    if (o == null) {

      return 1;

    } else {

      if (name() == null) {
        if (o.name() != null) {
          return -1;
        } else {
          return 0;
        }
      } else {
        if (o.name() != null) {
          // TODO(zhoujiagen) add more comparion
          return name().compareTo(o.name());
        } else {
          return 1;
        }
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (relation != null) {
      builder.append(relation.name());
      builder.append(".");
    }
    builder.append(name());
    builder.append(" ");
    builder.append(attributes());
    return builder.toString();
  }

}