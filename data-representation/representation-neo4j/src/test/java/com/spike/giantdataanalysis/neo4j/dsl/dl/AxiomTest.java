package com.spike.giantdataanalysis.neo4j.dsl.dl;

import org.junit.Test;

import com.spike.giantdataanalysis.neo4j.dsl.dl.axiom.ABoxConceptAssertion;
import com.spike.giantdataanalysis.neo4j.dsl.dl.axiom.ABoxRoleAssertion;
import com.spike.giantdataanalysis.neo4j.dsl.dl.axiom.TBoxConceptDefitnion;
import com.spike.giantdataanalysis.neo4j.dsl.dl.axiom.TBoxGeneralInclusion;
import com.spike.giantdataanalysis.neo4j.dsl.dl.axiom.TBoxPrimitiveRoleIntroduction;
import com.spike.giantdataanalysis.neo4j.dsl.dl.axiom.TBoxRoleDefinition;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.CIndividual;
import com.spike.giantdataanalysis.neo4j.dsl.dl.concept.ConceptConstructor;
import com.spike.giantdataanalysis.neo4j.dsl.dl.role.RoleConstructor;

public class AxiomTest {

  @Test
  public void TBoxConceptDefitnion() {
    TBoxConceptDefitnion axiom =
        new TBoxConceptDefitnion(ConceptConstructor.atomic("A"), ConceptConstructor.atomic("C"));
    System.out.println(axiom); // (define-concept A C)
  }

  @Test
  public void TBoxGeneralInclusion() {
    TBoxGeneralInclusion axiom =
        new TBoxGeneralInclusion(ConceptConstructor.atomic("C"), ConceptConstructor.atomic("D"));
    System.out.println(axiom); // (implies C D)
  }

  @Test
  public void TBoxPrimitiveConceptIntroduction() {
    TBoxGeneralInclusion axiom =
        new TBoxGeneralInclusion(ConceptConstructor.atomic("C"), ConceptConstructor.atomic("D"));
    System.out.println(axiom); // (define-primitive-concept C D)
  }

  @Test
  public void TBoxPrimitiveRoleIntroduction() {
    TBoxPrimitiveRoleIntroduction axiom =
        new TBoxPrimitiveRoleIntroduction(RoleConstructor.atomic("R"), RoleConstructor.atomic("S"));
    System.out.println(axiom); // (define-primitive-role R S)

  }

  @Test
  public void TBoxRoleDefinition() {
    TBoxRoleDefinition axiom =
        new TBoxRoleDefinition(RoleConstructor.atomic("R"), RoleConstructor.atomic("S"));
    System.out.println(axiom); // (define-role R S)
  }

  @Test
  public void ABoxConceptAssertion() {
    ABoxConceptAssertion axiom =
        new ABoxConceptAssertion(CIndividual.I("a"), ConceptConstructor.atomic("C"));
    System.out.println(axiom); // (instance a C)
  }

  @Test
  public void ABoxRoleAssertion() {
    ABoxRoleAssertion axiom =
        new ABoxRoleAssertion(CIndividual.I("a"), CIndividual.I("b"), RoleConstructor.atomic("R"));
    System.out.println(axiom); // (related a b R)
  }

}
