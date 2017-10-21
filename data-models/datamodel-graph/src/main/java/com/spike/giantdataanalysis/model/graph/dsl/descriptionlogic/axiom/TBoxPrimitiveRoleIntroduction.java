package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.axiom;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class TBoxPrimitiveRoleIntroduction extends TerminologicalAxiom {

  // 定义关系R为被蕴含于S
  private final RoleConstructor R;
  private final RoleConstructor S;

  public TBoxPrimitiveRoleIntroduction(RoleConstructor R, RoleConstructor S) {
    this.R = R;
    this.S = S;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.define_primitive_role.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(R);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(S);

    return sb.toString();
  }

}