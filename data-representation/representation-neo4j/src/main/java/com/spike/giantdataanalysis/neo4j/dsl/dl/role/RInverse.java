package com.spike.giantdataanalysis.neo4j.dsl.dl.role;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class RInverse extends RoleConstructor {

  private final RoleConstructor r;

  public RInverse(RoleConstructor r) {
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.inverse.name());
    sb.append(DLConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }

}