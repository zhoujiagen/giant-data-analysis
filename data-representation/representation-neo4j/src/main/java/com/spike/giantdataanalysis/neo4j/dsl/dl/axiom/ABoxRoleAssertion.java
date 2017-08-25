package com.spike.giantdataanalysis.neo4j.dsl.dl.axiom;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.CIndividual;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

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

    sb.append(DLOps.related.Name());
    sb.append(DLConstants.BLANK);
    sb.append(a);
    sb.append(DLConstants.BLANK);
    sb.append(b);
    sb.append(DLConstants.BLANK);
    sb.append(R);

    return sb.toString();
  }

}