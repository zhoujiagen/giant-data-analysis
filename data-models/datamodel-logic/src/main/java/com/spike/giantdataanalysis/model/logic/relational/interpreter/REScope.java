package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 作用域.
 */
public class REScope implements Comparable<REScope> {

  public static final String ROOT_NAME = "<ROOT>";
  public static final REScope ROOT = new REScope(null, ROOT_NAME);
  public static final String SEP = "$";

  /** chain of names. */
  public final String name;
  /** parent scope. */
  public final REScope parent;
  /** child scopes. */
  public final List<REScope> children = Lists.newArrayList();

  public REScope(REScope parent, String shortName) {
    Preconditions.checkArgument(shortName != null);

    this.parent = parent;
    this.name = this.newName(parent, shortName);
    if (parent != null) {
      parent.children.add(this);
    }
  }

  public String newName(REScope parent, String shortName) {
    if (parent == null) {
      return shortName;
    } else {
      return parent.name + SEP + shortName;
    }
  }

  public String shortName() {
    if (name.contains("$")) {
      return name.substring(name.lastIndexOf("$") + 1);
    } else {
      return name;
    }
  }

  @Override
  public int compareTo(REScope o) {
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