package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class RRestriction extends RoleConstructor {

  private final RoleConstructor r;

  public RRestriction(RoleConstructor r) {
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.restrict.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }

}