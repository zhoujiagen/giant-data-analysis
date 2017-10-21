package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic;

/**
 * 一些操作符
 */
public enum DescriptionLogicOps {
  // ============ for Concept and Role
  and("and"), //
  or("or"), //
  not("not"), //
  // ============ for Concept
  all("all"), some("some"), //
  at_least("at-least"), //
  at_most("at-most"), //
  exactly("exactly"), //
  same_as("same-as"), //
  not_same_as("not-same-as"), subset("subset"), //
  fillers("fillers"), //
  one_of("one-of"), //
  // ============ for Role
  inverse("inverse"), //
  compose("compose"), //
  transitive_closure("transitive-closure"), //
  transitive_reflexive_closure("transitive-reflexive-closure"), //
  restrict("restrict"), //
  identity("identity"), //
  // ============ for Axiom
  define_concept("define-concept"), //
  define_primitive_concept("define-primitive-concept"), //
  implies("implies"), //
  define_role("define-role"), //
  define_primitive_role("define-primitive-role"), //
  instance("instance"), //
  related("related");

  private String symbol;

  DescriptionLogicOps(String symbol) {
    this.symbol = symbol;
  }

  public String Name() {
    return this.symbol;
  }

}
