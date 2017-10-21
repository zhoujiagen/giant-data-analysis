package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CIndividual;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class ABoxRoleAssertion extends AssertionalAxiom {

  private final CIndividual a;
  private final CIndividual b;
  private final RoleConstructor R;

  public ABoxRoleAssertion(CIndividual a, CIndividual b, RoleConstructor R) {
    this.a = a;
    this.b = b;
    this.R = R;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.related.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(a);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(b);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(R);

    return sb.toString();
  }

}