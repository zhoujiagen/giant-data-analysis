package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

// TODO(zhoujiagen) 要解释什么呢???
public final class DescriptionLogicInterpretations {

  public static boolean same(ConceptConstructor C, ConceptConstructor D) {
    // 应用公理后怎么办???
    return C.toString().equals(D.toString());
  }

  public static boolean same(RoleConstructor R, RoleConstructor S) {
    // 应用公理后怎么办???
    return R.toString().equals(S.toString());
  }

}
