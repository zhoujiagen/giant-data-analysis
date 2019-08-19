package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import com.google.common.base.Preconditions;

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

  public REScope(REScope parent, String shortName) {
    Preconditions.checkArgument(shortName != null);

    this.parent = parent;
    this.name = this.newName(parent, shortName);
  }

  public String newName(REScope parent, String shortName) {
    if (parent == null) {
      return shortName;
    } else {
      return parent.name + SEP + shortName;
    }
  }

  @Override
  public int compareTo(REScope o) {
    if (o == null) {
      return 1;
    }
    return name.compareTo(o.name);
  }

}