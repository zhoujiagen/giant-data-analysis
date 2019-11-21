package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

/** 符号间链接类型. */
public enum RelationalInterpreterSymbolLinkTypeEnum implements RelationalInterpreterEnum {
  ALIAS_OF, //
  ARGUMENT_OF, //
  ATTRIBUTE_OF, //
  TABLE_OF_DATABASE, //
  TYPE, // Constant.Type
  SPECIFIER // 限定符
  ;
  // others

  @Override
  public String literal() {
    return name();
  }

}
