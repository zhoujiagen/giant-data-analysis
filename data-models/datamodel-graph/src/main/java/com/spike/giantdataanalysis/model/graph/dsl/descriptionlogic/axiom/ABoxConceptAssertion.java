package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CIndividual;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;

public class ABoxConceptAssertion extends AssertionalAxiom {

  private final CIndividual a;
  private final ConceptConstructor C;

  public ABoxConceptAssertion(CIndividual a, ConceptConstructor C) {
    this.a = a;
    this.C = C;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.instance.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(a);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(C);

    return sb.toString();
  }

}
