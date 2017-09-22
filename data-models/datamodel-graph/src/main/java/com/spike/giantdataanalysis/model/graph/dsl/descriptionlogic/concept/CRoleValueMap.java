package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class CRoleValueMap extends ConceptConstructor {

  private final RoleConstructor r1;
  private final RoleConstructor r2;

  public CRoleValueMap(RoleConstructor r1, RoleConstructor r2) {
    this.r1 = r1;
    this.r2 = r2;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.subset.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r1);
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r2);

    return sb.toString();
  }

}