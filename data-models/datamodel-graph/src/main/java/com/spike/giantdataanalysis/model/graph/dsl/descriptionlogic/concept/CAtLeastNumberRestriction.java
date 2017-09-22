package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class CAtLeastNumberRestriction extends ConceptConstructor {

  private final int n;
  private final RoleConstructor r;

  public CAtLeastNumberRestriction(int n, RoleConstructor r) {
    this.n = n;
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.at_least.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(n);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }
}