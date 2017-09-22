package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;

public abstract class AssertionalAxiom {

  public abstract String syntax();

  @Override
  public String toString() {
    // 两边加()
    return DescriptionLogicConstants.SYMBOL_BRACKET_LEFT + syntax()
        + DescriptionLogicConstants.SYMBOL_BRACKET_RIGHT;
  }

}