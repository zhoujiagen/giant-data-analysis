package com.spike.giantdataanalysis.neo4j.dsl.dl.role;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class RTransitiveClosure extends RoleConstructor {

  private final RoleConstructor r;

  public RTransitiveClosure(RoleConstructor r) {
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.transitive_closure.Name());
    sb.append(DLConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }
}