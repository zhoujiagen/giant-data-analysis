package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class CDisAggreement extends ConceptConstructor {

  private final CIndividual u1;
  private final CIndividual u2;

  public CDisAggreement(CIndividual u1, CIndividual u2) {
    this.u1 = u1;
    this.u2 = u2;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.not_same_as.Name());
    sb.append(DLConstants.BLANK);
    sb.append(u1);
    sb.append(DLConstants.BLANK);
    sb.append(u2);

    return sb.toString();
  }
}