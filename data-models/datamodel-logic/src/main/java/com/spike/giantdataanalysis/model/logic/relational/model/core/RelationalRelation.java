package com.spike.giantdataanalysis.model.logic.relational.model.core;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;

/**
 * 关系: 原始的或临时的.
 */
public class RelationalRelation implements Literal {
  public static final String TEMPORARY_NAME_PREFIX = "$";
  public static final String TEMPORARY_NAME_SEP = "^";

  public static final String RELATION_NAME_DUAL = "DUAL";
  public static final RelationalRelation DUAL =
      new RelationalRelation(RELATION_NAME_DUAL, Lists.newArrayList(RelationalAttribute.EMPTY));

  // 关系的名称
  public final String name;
  // 关系的属性列表
  public final List<RelationalAttribute> attributes = Lists.newArrayList();
  // 关系的键列表
  public final List<RelationalRelationKey> keys = Lists.newArrayList();

  RelationalRelation(String name, List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(attributes));

    this.name = name;
    this.attributes.addAll(attributes);
  }

  public void addAttributes(List<RelationalAttribute> attributes) {
    if (CollectionUtils.isEmpty(attributes)) {
      return;
    }

    Set<String> attributeNames = Sets.newHashSet();
    for (RelationalAttribute attr : this.attributes) {
      attributeNames.add(attr.name);
    }

    for (RelationalAttribute inputAttribute : attributes) {
      if (attributeNames.contains(inputAttribute.name)) {
        continue;
      } else {
        this.attributes.add(inputAttribute);
        attributeNames.add(inputAttribute.name);
      }
    }
  }

  public void addKeys(List<RelationalRelationKey> keys) {
    if (CollectionUtils.isEmpty(keys)) {
      return;
    }

    Set<String> keyNames = Sets.newHashSet();
    for (RelationalRelationKey k : this.keys) {
      keyNames.add(k.getName());
    }

    for (RelationalRelationKey inputKey : keys) {
      if (keyNames.contains(inputKey.getName())) {
        continue;
      } else {
        this.keys.add(inputKey);
        keyNames.add(inputKey.getName());
      }
    }
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append("(");
    sb.append(attributes);
    sb.append(")");
    if (CollectionUtils.isNotEmpty(keys)) {
      sb.append(", keys: ");
      sb.append(keys);
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return this.literal();
  }
}