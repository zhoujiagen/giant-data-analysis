package com.spike.giantdataanalysis.model.logic.relational.model.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;

/**
 * 关系键
 */
public class RelationalRelationKey implements Literal, Comparable<RelationalRelationKey> {
  // 键所属关系
  private final RelationalRelation relation;
  // 键的类型
  private final RelationalRelationKeyTypeEnum keyType;
  // 键的名称
  private final String name;
  // 键的属性列表
  private final List<RelationalAttribute> attributes;

  RelationalRelationKey(RelationalRelation relation, RelationalRelationKeyTypeEnum keyType,
      String name, List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(relation != null);
    Preconditions.checkArgument(keyType != null);
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes));

    this.relation = relation;
    this.keyType = keyType;
    this.name = name;
    this.attributes = attributes;

    relation.addKeys(Lists.newArrayList(this));
  }

  @Override
  public int compareTo(RelationalRelationKey o) {
    if (o == null) {

      return 1;

    } else {

      if (name == null) {
        if (o.name != null) {
          return -1;
        } else {
          return 0;
        }
      } else {
        if (o.name != null) {
          // TODO(zhoujiagen) add more comparion
          return name.compareTo(o.name);
        } else {
          return 1;
        }
      }
    }
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    if (relation != null) {
      sb.append(relation.name);
      sb.append(".");
    }
    sb.append(name);
    sb.append(" ");
    sb.append(attributes);
    return sb.toString();
  }

  @Override
  public String toString() {
    return this.literal();
  }

  public RelationalRelation getRelation() {
    return relation;
  }

  public RelationalRelationKeyTypeEnum getKeyType() {
    return keyType;
  }

  public String getName() {
    return name;
  }

  public List<RelationalAttribute> getAttributes() {
    return attributes;
  }

}