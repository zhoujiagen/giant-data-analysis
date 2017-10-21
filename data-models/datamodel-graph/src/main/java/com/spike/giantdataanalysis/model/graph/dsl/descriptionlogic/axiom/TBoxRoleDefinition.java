package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class TBoxRoleDefinition extends TerminologicalAxiom {

  // 定义关系R为等价于S
  private final RoleConstructor R;
  private final RoleConstructor S;

  public TBoxRoleDefinition(RoleConstructor R, RoleConstructor S) {
    this.R = R;
    this.S = S;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.define_role.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(R);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(S);

    return sb.toString();
  }

}
