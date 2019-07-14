package com.spike.giantdataanalysis.model.logic.boole.core;

/**
 * 布尔代数运算.
 */
public enum BooleanAlgebraOperationEnum {
  AND("与"), //
  OR("或"), //
  NOT("非");

  public final String description;

  BooleanAlgebraOperationEnum(String description) {
    this.description = description;
  }
}
