package com.spike.giantdataanalysis.neo4j.dsl.dl.role;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.ConceptConstructor;

public class RIdentity extends RoleConstructor {

  private final ConceptConstructor c;

  public RIdentity(ConceptConstructor c) {
    this.c = c;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.identity.name());
    sb.append(DLConstants.BLANK);
    sb.append(c);

    return sb.toString();
  }
}