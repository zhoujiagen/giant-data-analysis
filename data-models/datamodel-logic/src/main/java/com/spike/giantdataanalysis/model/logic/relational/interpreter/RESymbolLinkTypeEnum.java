package com.spike.giantdataanalysis.model.logic.relational.interpreter;

/** 符号间链接类型. */
public enum RESymbolLinkTypeEnum implements RelationalInterpreterEnum {
  ALIAS_OF, //
  ARGUMENT_OF, //
  ATTRIBUTE_OF, //
  TABLE_OF_DATABASE;
  // others

  @Override
  public String literal() {
    return name();
  }

}