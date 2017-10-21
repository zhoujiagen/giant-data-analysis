package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class CValueRestriction extends ConceptConstructor {

  private final RoleConstructor r;
  private final ConceptConstructor c;

  public CValueRestriction(RoleConstructor r, ConceptConstructor c) {
    this.r = r;
    this.c = c;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.all.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(c);

    return sb.toString();
  }

}