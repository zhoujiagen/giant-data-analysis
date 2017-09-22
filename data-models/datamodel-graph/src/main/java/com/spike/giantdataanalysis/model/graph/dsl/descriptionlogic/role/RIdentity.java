package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;

public class RIdentity extends RoleConstructor {

  private final ConceptConstructor c;

  public RIdentity(ConceptConstructor c) {
    this.c = c;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.identity.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(c);

    return sb.toString();
  }
}