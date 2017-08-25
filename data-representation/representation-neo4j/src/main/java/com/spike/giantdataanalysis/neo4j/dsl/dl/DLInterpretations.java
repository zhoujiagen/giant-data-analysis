package com.spike.giantdataanalysis.neo4j.dsl.dl;

import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.ConceptConstructor;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

// TODO(zhoujiagen) 要解释什么呢???
public final class DLInterpretations {

  public static boolean same(ConceptConstructor C, ConceptConstructor D) {
    // 应用公理后怎么办???
    return C.toString().equals(D.toString());
  }

  public static boolean same(RoleConstructor R, RoleConstructor S) {
    // 应用公理后怎么办???
    return R.toString().equals(S.toString());
  }

}
