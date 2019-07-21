package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 关系键类型.
 */
public enum RelationalRelationKeyTypeEnum implements RelationalAlgebraEnum {

  PRIMARY, //
  UNIQUE;//

  @Override
  public String literal() {
    return name();
  }
}
