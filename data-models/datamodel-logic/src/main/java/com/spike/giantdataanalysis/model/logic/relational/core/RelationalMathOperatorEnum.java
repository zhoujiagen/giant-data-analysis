package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 数学运算操作符.
 * 
 * <pre>
mathOperator
    : '*' | '/' | '%' | DIV | MOD | '+' | '-' | '--'
 * </pre>
 */
public enum RelationalMathOperatorEnum {
  MULTIPLE("*"), //
  DIVIDE("/"), //
  MOD1("%"), //
  DIV("DIV"), //
  MOD2("MOD"), //
  ADD("+"), //
  SUB("-"), //
  SUB_SUB("--");

  public final String symbol;

  RelationalMathOperatorEnum(String symbol) {
    this.symbol = symbol;
  }
}