package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicOps;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class CRoleFiller extends ConceptConstructor {

  private final RoleConstructor r;
  private final CIndividual[] is;

  public CRoleFiller(RoleConstructor r, CIndividual... is) {
    this.r = r;
    this.is = is;
  }

  @Override
  public String syntax() {
    StringBuilder sb = new StringBuilder();

    sb.append(DescriptionLogicOps.fillers.name());
    sb.append(DescriptionLogicConstants.BLANK);
    sb.append(r);
    for (CIndividual i : is) {
      sb.append(DescriptionLogicConstants.BLANK);
      sb.append(i);
    }

    return sb.toString();
  }

}