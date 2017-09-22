package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

public final class CTop extends ConceptConstructor {

  private static final CTop INSTANCE = new CTop();

  private CTop() {
  }

  public static CTop V() {
    return INSTANCE;
  }

  @Override
  public String syntax() {
    return "TOP";
  }

  @Override
  public String toString() {
    return syntax();
  }
}