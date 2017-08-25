package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class CNegation extends ConceptConstructor {

  private final ConceptConstructor concept;

  public CNegation(ConceptConstructor concept) {
    super();
    this.concept = concept;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.not.name());
    sb.append(DLConstants.BLANK);
    sb.append(concept);

    return sb.toString();
  }

}