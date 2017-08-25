package com.spike.giantdataanalysis.neo4j.dsl.dl.role;

import com.google.common.base.Joiner;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class RComposition extends RoleConstructor {

  private final RoleConstructor[] roles;

  public RComposition(RoleConstructor... roles) {
    this.roles = roles;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.compose.name());
    sb.append(DLConstants.BLANK);
    sb.append(Joiner.on(DLConstants.BLANK).join(roles));

    return sb.toString();
  }

}