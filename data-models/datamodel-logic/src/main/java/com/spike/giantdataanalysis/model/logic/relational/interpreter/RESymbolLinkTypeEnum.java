package com.spike.giantdataanalysis.model.logic.relational.interpreter;

/** 符号间链接类型. */
public enum RESymbolLinkTypeEnum implements RelationalInterpreterEnum {
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