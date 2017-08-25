package com.spike.giantdataanalysis.neo4j.dsl.dl.axiom;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;

public abstract class AssertionalAxiom {

  public abstract String syntax();

  @Override
  public String toString() {
    // 两边加()
    return DLConstants.SYMBOL_BRACKET_LEFT + syntax() + DLConstants.SYMBOL_BRACKET_RIGHT;
  }

}