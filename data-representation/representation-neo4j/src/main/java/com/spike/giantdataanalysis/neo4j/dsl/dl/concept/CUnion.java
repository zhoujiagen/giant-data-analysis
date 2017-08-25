package com.spike.giantdataanalysis.neo4j.dsl.dl.concept;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;

public class CUnion extends ConceptConstructor {

  private final ConceptConstructor[] concepts;

  public CUnion(ConceptConstructor... concepts) {
    Preconditions.checkArgument(concepts != null && concepts.length > 0,
      "Argument concepts should not be empty");
    this.concepts = concepts;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.or.name());
    sb.append(DLConstants.BLANK);
    sb.append(Joiner.on(DLConstants.BLANK).join(concepts));

    return sb.toString();
  }

}