package com.spike.giantdataanalysis.neo4j.dsl.dl.axiom;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.ConceptConstructor;

public class TBoxGeneralInclusion extends TerminologicalAxiom {

  // 声明概念C为被蕴含于D
  private final ConceptConstructor C;
  private final ConceptConstructor D;

  public TBoxGeneralInclusion(ConceptConstructor C, ConceptConstructor D) {
    this.C = C;
    this.D = D;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.implies.Name());
    sb.append(DLConstants.BLANK);
    sb.append(C);
    sb.append(DLConstants.BLANK);
    sb.append(D);

    return sb.toString();
  }
}
