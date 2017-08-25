package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class COneOf extends ConceptConstructor {

  private final CIndividual[] is;

  public COneOf(CIndividual... is) {
    this.is = is;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.one_of.Name());
    for (CIndividual i : is) {
      sb.append(DLConstants.BLANK);
      sb.append(i);
    }

    return sb.toString();
  }

}