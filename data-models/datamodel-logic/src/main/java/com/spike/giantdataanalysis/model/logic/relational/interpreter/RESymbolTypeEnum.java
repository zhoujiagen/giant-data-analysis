package com.spike.giantdataanalysis.model.logic.relational.interpreter;

/** 符号类型. */
public enum RESymbolTypeEnum implements RelationalInterpreterEnum {
  CONSTANT, //
  DATABASE_NAME, //
  TABLE_NAME, //
  ATTRIBUTE_NAME, //
  FUNCTION_NAME//
  ;
  // others

  @Override
  public String literal() {
    return name();
  }
}