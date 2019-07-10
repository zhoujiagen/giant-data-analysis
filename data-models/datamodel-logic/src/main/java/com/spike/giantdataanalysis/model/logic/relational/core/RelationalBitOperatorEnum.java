package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 位操作符.
 * 
 * <pre>
bitOperator
    : '<' '<' | '>' '>' | '&' | '^' | '|'
 * </pre>
 */
public enum RelationalBitOperatorEnum {
  LEFT_SHIFT("<<"), //
  RIGHT_SHIFT(">>"), //
  AND("&"), //
  XOR("^"), //
  OR("|");

  public final String symbol;

  RelationalBitOperatorEnum(String symbol) {
    this.symbol = symbol;
  }
}