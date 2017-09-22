package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.google.common.base.Joiner;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class RComposition extends RoleConstructor {

  private final RoleConstructor[] roles;

  public RComposition(RoleConstructor... roles) {
    this.roles = roles;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.compose.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(Joiner.on(DescriptionLogicConstants.BLANK).join(roles));

    return sb.toString();
  }

}