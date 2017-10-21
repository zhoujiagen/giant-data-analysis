package com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.role;

import com.spike.giantdataanalysis.model.graph.dsl.descriptionlogic.DescriptionLogicConstants;

public abstract class RoleConstructor {

  public abstract String syntax();

  /**
   * 构造普通关系(原子关系).
   * @param name 普通关系名称
   * @return
   */
  public static final RoleConstructor atomic(final String name) {
    return new RoleConstructor() {
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