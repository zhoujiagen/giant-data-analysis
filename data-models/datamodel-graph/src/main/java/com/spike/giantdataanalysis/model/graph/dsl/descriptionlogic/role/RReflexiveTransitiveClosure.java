package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;

public class RReflexiveTransitiveClosure extends RoleConstructor {

  private final RoleConstructor r;

  public RReflexiveTransitiveClosure(RoleConstructor r) {
    this.r = r;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.transitive_reflexive_closure.Name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);

    return sb.toString();
  }

}