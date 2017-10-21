package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;

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

    sb.append(DescriptionLogicOps.implies.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(C);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(D);

    return sb.toString();
  }
}
