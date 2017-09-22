package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class RIntersection extends RoleConstructor {

  private final RoleConstructor[] roles;

  public RIntersection(RoleConstructor... roles) {
    Preconditions.checkArgument(roles != null && roles.length > 0,
      "Argument roles should not be empty");
    this.roles = roles;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.and.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(Joiner.on(DescriptionLogicConstants.BLANK).join(roles));

    return sb.toString();
  }

}