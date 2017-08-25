package com.spike.giantdataanalysis.neo4j.dsl.dl.axiom;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.CIndividual;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.ConceptConstructor;

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

    sb.append(DLOps.instance.Name());
    sb.append(DLConstants.BLANK);
    sb.append(a);
    sb.append(DLConstants.BLANK);
    sb.append(C);

    return sb.toString();
  }

}
