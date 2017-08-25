package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

public final class CBottom extends ConceptConstructor {

  private static final CBottom INSTANCE = new CBottom();

  private CBottom() {
  }

  public static CBottom V() {
    return INSTANCE;
  }

  @Override
  public String syntax() {
    return "BOTTOM";
  }

  @Override
  public String toString() {
    return syntax();
  }
}