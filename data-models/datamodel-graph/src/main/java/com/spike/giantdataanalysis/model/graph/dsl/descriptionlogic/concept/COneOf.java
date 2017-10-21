package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class COneOf extends ConceptConstructor {

  private final CIndividual[] is;

  public COneOf(CIndividual... is) {
    this.is = is;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.one_of.Name());
    for (CIndividual i : is) {
      sb.append(DescriptionLogicConstants.BLANK);
      sb.append(i);
    }

    return sb.toString();
  }

}