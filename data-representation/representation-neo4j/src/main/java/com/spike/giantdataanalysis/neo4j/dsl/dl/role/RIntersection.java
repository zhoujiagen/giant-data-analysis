package com.spike.giantdataanalysis.neo4j.dsl.dl.role;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

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

    sb.append(DLOps.and.name());
    sb.append(DLConstants.BLANK);
    sb.append(Joiner.on(DLConstants.BLANK).join(roles));

    return sb.toString();
  }

}