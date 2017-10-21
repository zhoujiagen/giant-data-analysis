package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class CIntersection extends ConceptConstructor {

  private final ConceptConstructor[] concepts;

  public CIntersection(ConceptConstructor... concepts) {
    Preconditions.checkArgument(concepts != null && concepts.length > 0,
      "Argument concepts should not be empty");
    this.concepts = concepts;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.and.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(Joiner.on(DescriptionLogicConstants.BLANK).join(concepts));

    return sb.toString();
  }

}