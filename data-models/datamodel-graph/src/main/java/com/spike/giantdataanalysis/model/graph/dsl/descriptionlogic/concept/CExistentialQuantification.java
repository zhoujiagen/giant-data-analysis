package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class CExistentialQuantification extends ConceptConstructor {

  private final RoleConstructor r;
  private final ConceptConstructor c;

  public CExistentialQuantification(RoleConstructor r, ConceptConstructor c) {
    this.r = r;
    this.c = c;
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