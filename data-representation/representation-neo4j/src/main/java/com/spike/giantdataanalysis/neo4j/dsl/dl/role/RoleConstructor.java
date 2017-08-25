package com.spike.giantdataanalysis.neo4j.dsl.dl.role;

import com.spike.giantdataanalysis.neo4j.dsl.dl.DLConstants;

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
    return DLConstants.SYMBOL_BRACKET_LEFT + syntax() + DLConstants.SYMBOL_BRACKET_RIGHT;
  }

}