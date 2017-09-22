package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class RComplement extends RoleConstructor {

  private final RoleConstructor r;

  public RComplement(RoleConstructor r) {
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.not.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }

}