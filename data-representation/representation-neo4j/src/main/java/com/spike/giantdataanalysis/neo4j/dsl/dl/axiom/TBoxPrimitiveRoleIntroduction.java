package com.spike.giantdataanalysis.neo4j.dsl.dl.axiom;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;
import com.spike.giantdataanalysis.neo4j.dsl.dl.DLOps;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

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

    sb.append(DLOps.define_primitive_role.Name());
    sb.append(DLConstants.BLANK);
    sb.append(R);
    sb.append(DLConstants.BLANK);
    sb.append(S);

    return sb.toString();
  }

}