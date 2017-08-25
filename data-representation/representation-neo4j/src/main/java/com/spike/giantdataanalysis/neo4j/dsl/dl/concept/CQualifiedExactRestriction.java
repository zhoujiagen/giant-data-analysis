package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class CQualifiedExactRestriction extends ConceptConstructor {

  private final int n;
  private final RoleConstructor r;
  private final ConceptConstructor c;

  public CQualifiedExactRestriction(int n, RoleConstructor r, ConceptConstructor c) {
    this.n = n;
    this.r = r;
    this.c = c;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.exactly.name());
    sb.append(DLConstants.BLANK);
    sb.append(n);
    sb.append(DLConstants.BLANK);
    sb.append(r);
    sb.append(DLConstants.BLANK);
    sb.append(c);

    return sb.toString();
  }

}