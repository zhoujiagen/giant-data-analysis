package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/** 关系 */
public class RelationalRelation {
  protected final String name;

  protected List<RelationalAttribute> attributes = Lists.newArrayList();
  protected List<RelationalRelationKey> keys = Lists.newArrayList();

  RelationalRelation(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  public List<RelationalAttribute> attributes() {
    return attributes;
  }

  public List<RelationalRelationKey> keys() {
    return keys;
  }

  public void addAttribute(RelationalAttribute attribute) {
    Preconditions.checkArgument(attribute != null);
    for (RelationalAttribute attr : attributes) {
      Preconditions.checkArgument(!attr.name().equals(attribute.name()));
    }

    attribute.setRelation(this);
    attributes.add(attribute);
  }

  public void addKey(RelationalRelationKey key) {
    Preconditions.checkArgument(key != null);
    for (RelationalRelationKey k : keys) {
      Preconditions.checkArgument(!k.name().equals(key.name()));
    }

    key.setRelation(this);
    keys.add(key);
  }

  public RelationalTuples asTuples(final List<List<Object>> values) {
    Preconditions.checkArgument(values != null && !values.isEmpty());
    int attributeSize = attributes.size();
    for (List<Object> value : values) {
      Preconditions.checkArgument(attributeSize == value.size());
      for (int i = 0; i < attributeSize; i++) {
        Preconditions.checkArgument(attributes.get(i).dataType.reprClass.isInstance(value.get(i)));
      }
    }

    return RelationalModelFactory.makeTuples(attributes, values);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(name());
    builder.append("");
    builder.append(System.lineSeparator());
    builder.append("keys: ");
    builder.append(keys());
    builder.append(System.lineSeparator());
    builder.append("attributes: ");
    builder.append(attributes());
    builder.append(System.lineSeparator());
    return builder.toString();
  }
}