package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.google.common.base.Preconditions;

/**
 * 实例(Individual)定义.
 */
public final class CIndividual {

  private final String name;

  private CIndividual(String name) {
    this.name = name;
  }

  public static CIndividual I(String name) {
    Preconditions.checkArgument(name != null && !"".equals(name.trim()),
      "Argument name should not be empty");
    return new CIndividual(name);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CIndividual other = (CIndividual) obj;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }

}
