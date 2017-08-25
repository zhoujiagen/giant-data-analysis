package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class CExactNumberRestriction extends ConceptConstructor {

  private final int n;
  private final RoleConstructor r;

  public CExactNumberRestriction(int n, RoleConstructor r) {
    this.n = n;
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.exactly.name());
    sb.append(DLConstants.BLANK);
    sb.append(n);
    sb.append(DLConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }

}