package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic;

import org.junit.Test;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RComplement;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RComposition;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RIdentity;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RIntersection;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RInverse;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RReflexiveTransitiveClosure;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RTransitiveClosure;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RUnion;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RUniversalRole;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class RoleConstructorTest {

  @Test
  public void RoleConstructor() {
    RoleConstructor friend_of = RoleConstructor.atomic("friend-of");
    System.out.println(friend_of); // friend-of
  }

  @Test
  public void RUniversalRole() {
    RUniversalRole top = RUniversalRole.V();
    System.out.println(top); // top
  }

  @Test
  public void RComplement() {
    RComplement r = new RComplement(RoleConstructor.atomic("R"));
    System.out.println(r);// (not R)
  }

  @Test
  public void RComposition() {
    RComposition r = new RComposition(RoleConstructor.atomic("R1"), RoleConstructor.atomic("R2"));
    System.out.println(r);// (compose R1 R2)
  }

  @Test
  public void RIdentity() {
    ConceptConstructor C = ConceptConstructor.atomic("C");
    RIdentity r = new RIdentity(C);
    System.out.println(r); // (identity C)
  }

  @Test
  public void RIntersection() {
    RIntersection r = new RIntersection(RoleConstructor.atomic("R1"), RoleConstructor.atomic("R2"));
    System.out.println(r);// (and R1 R2)
  }

  @Test
  public void RInverse() {
    RInverse r = new RInverse(RoleConstructor.atomic("R"));
    System.out.println(r); // (inverse R)
  }

  @Test
  public void RReflexiveTransitiveClosure() {
    RReflexiveTransitiveClosure r = new RReflexiveTransitiveClosure(RoleConstructor.atomic("R"));
    System.out.println(r); // (transitive-reflexive-closure R)
  }

  @Test
  public void RRestriction() {
    RRestriction r = new RRestriction(RoleConstructor.atomic("R"));
    System.out.println(r); // (restrict R)
  }

  @Test
  public void RTransitiveClosure() {
    RTransitiveClosure r = new RTransitiveClosure(RoleConstructor.atomic("R"));
    System.out.println(r); // (transitive-closure R)

  }

  @Test
  public void RUnion() {
    RUnion r = new RUnion(RoleConstructor.atomic("R1"), RoleConstructor.atomic("R2"));
    System.out.println(r); // (or R1 R2)
  }

  @Test
  public void complex_example() {
    RoleConstructor r = new RUnion(//
        new RIntersection(RoleConstructor.atomic("R1"), RoleConstructor.atomic("R2")), //
        new RComplement(RoleConstructor.atomic("R3")), //
        new RRestriction(RoleConstructor.atomic("R"))//
    );
    // (or (and R1 R2) (not R3) (restrict R))
    System.out.println(r);
  }

}
