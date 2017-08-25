package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class CAtMostNumberRestriction extends ConceptConstructor {

  private final int n;
  private final RoleConstructor r;

  public CAtMostNumberRestriction(int n, RoleConstructor r) {
    this.n = n;
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.at_most.Name());
    sb.append(DLConstants.BLANK);
    sb.append(n);
    sb.append(DLConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }

}