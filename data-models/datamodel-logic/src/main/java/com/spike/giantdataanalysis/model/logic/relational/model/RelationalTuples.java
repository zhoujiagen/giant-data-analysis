package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/** 元组列表. */
public abstract class RelationalTuples {

  RelationalTuples() {
  }

  public abstract List<RelationalAttribute> attributes();

  public abstract List<List<Object>> values();

  public boolean isValid() {
    try {
      Preconditions.checkState(attributes() != null && !attributes().isEmpty());
      Preconditions.checkState(values() != null && values().size() > 0);
      int size = attributes().size();
      for (List<Object> value : values()) {
        Preconditions.checkArgument(size == value.size());
      }
      for (List<Object> value : values()) {
        for (int i = 0; i < size; i++) {
          Class<?> reprClass = attributes().get(i).dataType().reprClass;
          Preconditions.checkArgument(reprClass.isInstance(value.get(i)));
        }
      }

      return true;
    } catch (IllegalStateException e) {
      return false;
    }
  }

  public RelationalTuples project(List<RelationalAttribute> attributes) {

    List<List<Object>> resultValues = Lists.newArrayList();
    int attribuetSize = attributes().size();
    List<Integer> positions = Lists.newArrayList();
    for (int i = 0; i < attribuetSize; i++) {
      for (RelationalAttribute attribute : attributes) {
        if (attributes().get(i).compareTo(attribute) == 0) {
          positions.add(i);
        }
      }
    }

    for (List<Object> value : values()) {
      List<Object> toResult = Lists.newArrayList();
      for (Integer position : positions) {
        toResult.add(value.get(position));
      }
      resultValues.add(toResult);
    }

    return RelationalModelFactory.makeTuples(attributes, resultValues);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(Joiner.on(", ").join(attributes()));
    builder.append(System.lineSeparator());
    for (List<Object> value : values()) {
      builder.append(Joiner.on(" | ").join(value));
      builder.append(System.lineSeparator());
    }
    return builder.toString();
  }

  public static final RelationalTuples EMPTY = new RelationalTuples() {

    @Override
    public List<RelationalAttribute> attributes() {
      return Lists.newArrayList();
    }

    @Override
    public List<List<Object>> values() {
      return Lists.newArrayList();
    }
  };
}