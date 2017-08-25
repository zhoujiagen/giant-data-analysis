package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class CRoleFiller extends ConceptConstructor {

  private final RoleConstructor r;
  private final CIndividual[] is;

  public CRoleFiller(RoleConstructor r, CIndividual... is) {
    this.r = r;
    this.is = is;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.fillers.name());
    sb.append(DLConstants.BLANK);
    sb.append(r);
    for (CIndividual i : is) {
      sb.append(DLConstants.BLANK);
      sb.append(i);
    }

    return sb.toString();
  }

}