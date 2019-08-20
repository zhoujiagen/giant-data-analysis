package com.spike.giantdataanalysis.model.logic.relational.interpreter;

/** 符号类型. */
public enum RESymbolTypeEnum implements RelationalInterpreterEnum {
  CONSTANT, //
  VARIABLE, //
  MYSQL_VARIABLE, //
  DATABASE_NAME, //
  TABLE_NAME, //
  ATTRIBUTE_NAME, //
  ALL_ATTRIBUTE_NAME, // *
  FUNCTION_NAME, //
  OPERATOR_NAME, //
  COLLATION_NAME;
  // others

  @Override
  public String literal() {
    return name();
  }
}