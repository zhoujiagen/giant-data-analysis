package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class CRoleValueMap extends ConceptConstructor {

  private final RoleConstructor r1;
  private final RoleConstructor r2;

  public CRoleValueMap(RoleConstructor r1, RoleConstructor r2) {
    this.r1 = r1;
    this.r2 = r2;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.subset.name());
    sb.append(DLConstants.BLANK);
    sb.append(r1);
    sb.append(DLConstants.BLANK);
    sb.append(r2);

    return sb.toString();
  }

}