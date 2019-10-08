package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic;

import org.junit.Test;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CAggreement;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CAtLeastNumberRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CAtMostNumberRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CBottom;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CDisAggreement;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CExactNumberRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CExistentialQuantification;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CIndividual;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CIntersection;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CLimitedExistentialQuantification;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CNegation;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.COneOf;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CQualifiedAtLeastRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CQualifiedAtMostRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CQualifiedExactRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CRoleFiller;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CRoleValueMap;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CSameAs;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CTop;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CUnion;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.CValueRestriction;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept.ConceptConstructor;
import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role.RoleConstructor;

public class ConceptConstructorTest {

  @Test
  public void CTop() {
    System.out.println(CTop.V());// TOP
  }

  @Test
  public void CBottom() {
    System.out.println(CBottom.V()); // BOTTOM
  }

  @Test
  public void regularConcept() {
    System.out.println(ConceptConstructor.atomic("C1"));// C1
  }

  @Test
  public void CNegation() {
    ConceptConstructor C1 = ConceptConstructor.atomic("C1");

    CNegation c = new CNegation(C1);
    System.out.println(c); // (not C1)
  }

  @Test
  public void CUnion() {
    ConceptConstructor C1 = ConceptConstructor.atomic("C1");
    ConceptConstructor C2 = ConceptConstructor.atomic("C2");
    ConceptConstructor C3 = ConceptConstructor.atomic("C3");

    CUnion c = new CUnion(C1, C2, C3);
    System.out.println(c); // (or C1 C2 C3)
  }

  @Test
  public void CIntersection() {
    ConceptConstructor C1 = ConceptConstructor.atomic("C1");
    ConceptConstructor C2 = ConceptConstructor.atomic("C2");
    ConceptConstructor C3 = ConceptConstructor.atomic("C3");

    CIntersection c = new CIntersection(C1, C2, C3);
    System.out.println(c); // (and C1 C2 C3)
  }

  @Test
  public void CValueRestriction() {
    ConceptConstructor C = ConceptConstructor.atomic("C");
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    CValueRestriction c = new CValueRestriction(R, C);
    System.out.println(c); // (all friend-of C)
  }

  @Test
  public void CExistentialQuantification() {
    ConceptConstructor C = ConceptConstructor.atomic("C");
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    CExistentialQuantification c = new CExistentialQuantification(R, C);
    System.out.println(c); // (some friend-of C)
  }

  @Test
  public void CLimitedExistentialQuantification() {
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    CLimitedExistentialQuantification c = new CLimitedExistentialQuantification(R);
    System.out.println(c); // (some friend-of TOP)
  }

  @Test
  public void CAtLeastNumberRestriction() {
    int n = 2;
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    CAtLeastNumberRestriction c = new CAtLeastNumberRestriction(n, R);
    System.out.println(c); // (at-least 2 friend-of)
  }

  @Test
  public void CAtMostNumberRestriction() {
    int n = 2;
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    CAtMostNumberRestriction c = new CAtMostNumberRestriction(n, R);
    System.out.println(c); // (at-most 2 friend-of)
  }

  @Test
  public void CExactNumberRestriction() {
    int n = 2;
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    CExactNumberRestriction c = new CExactNumberRestriction(n, R);
    System.out.println(c); // (exactly 2 friend-of)
  }

  @Test
  public void CQualifiedAtLeastRestriction() {
    int n = 2;
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    ConceptConstructor C = ConceptConstructor.atomic("C");
    CQualifiedAtLeastRestriction c = new CQualifiedAtLeastRestriction(n, R, C);
    System.out.println(c); // (at-least 2 friend-of C)
  }

  @Test
  public void CQualifiedAtMostRestriction() {
    int n = 2;
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    ConceptConstructor C = ConceptConstructor.atomic("C");
    CQualifiedAtMostRestriction c = new CQualifiedAtMostRestriction(n, R, C);
    System.out.println(c); // (at-most 2 friend-of C)

  }

  @Test
  public void CQualifiedExactRestriction() {
    int n = 2;
    RoleConstructor R = RoleConstructor.atomic("friend-of");
    ConceptConstructor C = ConceptConstructor.atomic("C");
    CQualifiedExactRestriction c = new CQualifiedExactRestriction(n, R, C);
    System.out.println(c); // (exactly 2 friend-of C)
  }

  @Test
  public void CAggreement() {
    CIndividual u1 = CIndividual.I("u1");
    CIndividual u2 = CIndividual.I("u2");
    CAggreement c = new CAggreement(u1, u2);
    System.out.println(c); // (same-as u1 u2)
  }

  @Test
  public void CSameAs() {
    CIndividual u1 = CIndividual.I("u1");
    CIndividual u2 = CIndividual.I("u2");
    CSameAs c = new CSameAs(u1, u2);
    System.out.println(c); // (same-as u1 u2)
  }

  @Test
  public void CDisAggreement() {
    CIndividual u1 = CIndividual.I("u1");
    CIndividual u2 = CIndividual.I("u2");
    CDisAggreement c = new CDisAggreement(u1, u2);
    System.out.println(c); // (not-same-as u1 u2)
  }

  @Test
  public void CRoleValueMap() {
    RoleConstructor R1 = RoleConstructor.atomic("R1");
    RoleConstructor R2 = RoleConstructor.atomic("R2");
    CRoleValueMap c = new CRoleValueMap(R1, R2);
    System.out.println(c); // (subset R1 R2)
  }

  @Test
  public void CRoleFiller() {
    RoleConstructor R = RoleConstructor.atomic("R");
    CRoleFiller c =
        new CRoleFiller(R, CIndividual.I("I1"), CIndividual.I("I2"), CIndividual.I("I3"));
    System.out.println(c);// (fillers R I1 I2 I3)
  }

  @Test
  public void COneOf() {
    COneOf c = new COneOf(CIndividual.I("I1"), CIndividual.I("I2"), CIndividual.I("I3"));
    System.out.println(c); // (one-of I1 I2 I3)
  }

  @Test
  public void complex_example() {
    ConceptConstructor c = new CUnion(//
        new CValueRestriction(RoleConstructor.atomic("R1"), ConceptConstructor.atomic("C1")), //
        new CAtLeastNumberRestriction(2, RoleConstructor.atomic("R2")), //
        new CIntersection(//
            new CExistentialQuantification(RoleConstructor.atomic("R3"),
                ConceptConstructor.atomic("C3")), //
            new CNegation(ConceptConstructor.atomic("R4"))//
        )//
    );
    // (or (all R1 C1) (at-least 2 R2) (and (some R3 C3) (not R4)))
    System.out.println(c);
  }
}
