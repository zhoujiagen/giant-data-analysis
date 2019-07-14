package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import com.google.common.base.Preconditions;

/** 元组 */
public abstract class RelationalTuple {
  public abstract List<RelationalAttribute> attributes();

  public abstract List<Object> values();

  RelationalTuple() {
  }

  public boolean isValid() {
    try {
      Preconditions.checkState(attributes() != null && !attributes().isEmpty());
      Preconditions.checkState(values() != null && !values().isEmpty());
      Preconditions.checkState(attributes().size() == values().size());
      int size = attributes().size();

      for (int i = 0; i < size; i++) {
        Class<?> reprClass = attributes().get(i).dataType().reprClass;
        Preconditions.checkState(reprClass.isInstance(values().get(i)));
      }

      return true;
    } catch (IllegalStateException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(attributes());
    builder.append(System.lineSeparator());
    builder.append(values());
    return builder.toString();
  }
}