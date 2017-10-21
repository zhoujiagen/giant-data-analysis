package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.concept;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;

public abstract class ConceptConstructor {

  public abstract String syntax();

  /**
   * 构造普通概念(原子概念).
   * @param name 普通概念名称
   * @return
   */
  public static final ConceptConstructor atomic(final String name) {
    return new ConceptConstructor() {
      @Override
      public String syntax() {
        return name;
      }

      @Override
      public String toString() {
        // 两边不加()
        return syntax();
      }
    };
  }

  @Override
  public String toString() {
    // 两边加()
    return DescriptionLogicConstants.SYMBOL_BRACKET_LEFT + syntax()
        + DescriptionLogicConstants.SYMBOL_BRACKET_RIGHT;
  }
}