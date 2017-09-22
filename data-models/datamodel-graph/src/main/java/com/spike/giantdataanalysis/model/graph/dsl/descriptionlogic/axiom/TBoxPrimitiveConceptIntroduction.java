package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;

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

    sb.append(DescriptionLogicOps.define_primitive_concept.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(A);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(C);

    return sb.toString();
  }

}
