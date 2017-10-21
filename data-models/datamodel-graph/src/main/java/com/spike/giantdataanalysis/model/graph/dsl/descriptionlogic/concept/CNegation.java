package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class CNegation extends ConceptConstructor {

  private final ConceptConstructor concept;

  public CNegation(ConceptConstructor concept) {
    super();
    this.concept = concept;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.not.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(concept);

    return sb.toString();
  }

}