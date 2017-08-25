package com.spike.giantdataanalysis.neo4j.dsl.dl.axiom;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.ConceptConstructor;

public class TBoxPrimitiveConceptIntroduction extends TerminologicalAxiom {

  // 定义概念A为被蕴含于C
  private final ConceptConstructor A;
  private final ConceptConstructor C;

  public TBoxPrimitiveConceptIntroduction(ConceptConstructor A, ConceptConstructor C) {
    this.A = A;
    this.C = C;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DLOps.define_primitive_concept.Name());
    sb.append(DLConstants.BLANK);
    sb.append(A);
    sb.append(DLConstants.BLANK);
    sb.append(C);

    return sb.toString();
  }

}
