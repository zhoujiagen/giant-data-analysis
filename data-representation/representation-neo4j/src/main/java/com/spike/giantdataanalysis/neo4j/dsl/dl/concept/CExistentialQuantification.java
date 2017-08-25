package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class CExistentialQuantification extends ConceptConstructor {

  private final RoleConstructor r;
  private final ConceptConstructor c;

  public CExistentialQuantification(RoleConstructor r, ConceptConstructor c) {
    this.r = r;
    this.c = c;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.some.name());
    sb.append(DLConstants.BLANK);
    sb.append(r);
    sb.append(DLConstants.BLANK);
    sb.append(c);

    return sb.toString();
  }

}