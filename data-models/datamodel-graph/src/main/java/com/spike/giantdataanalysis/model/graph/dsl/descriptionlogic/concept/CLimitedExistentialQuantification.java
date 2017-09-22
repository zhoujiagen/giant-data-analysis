package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class CLimitedExistentialQuantification extends ConceptConstructor {

  private final RoleConstructor r;
  private final ConceptConstructor c = CTop.V();

  public CLimitedExistentialQuantification(RoleConstructor r) {
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.some.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(c);

    return sb.toString();
  }
}