package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 作用域.
 */
public class RelationalInterpreterScope implements Comparable<RelationalInterpreterScope> {

  public static final String ROOT_NAME = "<ROOT>";
  public static final RelationalInterpreterScope ROOT =
      new RelationalInterpreterScope(null, ROOT_NAME);
  public static final String SEP = "$";

  /** chain of names. */
  public final String name;
  /** parent scope. */
  public final RelationalInterpreterScope parent;
  /** child scopes. */
  public final List<RelationalInterpreterScope> children = Lists.newArrayList();

  public RelationalInterpreterScope(RelationalInterpreterScope parent, String shortName) {
    Preconditions.checkArgument(shortName != null);

    this.parent = parent;
    this.name = this.newName(parent, shortName);
    if (parent != null) {
      parent.children.add(this);
    }
  }

  public String newName(RelationalInterpreterScope parent, String childName) {
    if (parent == null) {
      return childName;
    } else {
      return parent.name + SEP + childName;
    }
  }

  public String childName(String childName) {
    return this.name + SEP + childName;
  }

  public RelationalInterpreterScope childScope(String childName) {
    String childScopeName = this.childName(childName);
    for (RelationalInterpreterScope childScope : children) {
      if (childScope.name.endsWith(childScopeName)) {
        return childScope;
      }
    }

    throw RelationalInterpreterError.make(childName + " Not Found!");
  }

  public String shortName() {
    if (name.contains("$")) {
      return name.substring(name.lastIndexOf("$") + 1);
    } else {
      return name;
    }
  }

  @Override
  public int compareTo(RelationalInterpreterScope o) {
    if (o == null) {
      return 1;
    }
    return name.compareTo(o.name);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("(");
    builder.append(name);
    if (CollectionUtils.isNotEmpty(children)) {
      builder.append(": ");
      builder.append(Joiner.on(", ").join(children));
    }
    builder.append(")");
    return builder.toString();
  }

}