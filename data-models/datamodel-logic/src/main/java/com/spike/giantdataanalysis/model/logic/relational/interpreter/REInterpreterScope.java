package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 作用域.
 */
public class REInterpreterScope implements Comparable<REInterpreterScope> {

  public static final String ROOT_NAME = "<ROOT>";
  public static final REInterpreterScope ROOT = new REInterpreterScope(null, ROOT_NAME);
  public static final String SEP = "$";

  /** chain of names. */
  public final String name;
  /** parent scope. */
  public final REInterpreterScope parent;
  /** child scopes. */
  public final List<REInterpreterScope> children = Lists.newArrayList();

  public REInterpreterScope(REInterpreterScope parent, String shortName) {
    Preconditions.checkArgument(shortName != null);

    this.parent = parent;
    this.name = this.newName(parent, shortName);
    if (parent != null) {
      parent.children.add(this);
    }
  }

  public String newName(REInterpreterScope parent, String childName) {
    if (parent == null) {
      return childName;
    } else {
      return parent.name + SEP + childName;
    }
  }

  public String childName(String childName) {
    return this.name + SEP + childName;
  }

  public REInterpreterScope childScope(String childName) {
    String childScopeName = this.childName(childName);
    for (REInterpreterScope childScope : children) {
      if (childScope.name.endsWith(childScopeName)) {
        return childScope;
      }
    }

    throw REInterpreterError.make(childName + " Not Found!");
  }

  public String shortName() {
    if (name.contains("$")) {
      return name.substring(name.lastIndexOf("$") + 1);
    } else {
      return name;
    }
  }

  @Override
  public int compareTo(REInterpreterScope o) {
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