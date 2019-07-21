package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 位操作符.
 * 
 * <pre>
bitOperator
    : '<' '<' | '>' '>' | '&' | '^' | '|'
 * </pre>
 */
public enum RelationalBitOperatorEnum implements RelationalAlgebraEnum {
  LEFT_SHIFT("<<"), //
  RIGHT_SHIFT(">>"), //
  AND("&"), //
  XOR("^"), //
  OR("|");

  public final String symbol;

  RelationalBitOperatorEnum(String symbol) {
    this.symbol = symbol;
  }

  public static RelationalBitOperatorEnum of(String symbol) {
    if (symbol == null) {
      return null;
    }

    for (RelationalBitOperatorEnum e : RelationalBitOperatorEnum.values()) {
      if (e.symbol.equals(symbol)) {
        return e;
      }
    }

    return null;
  }

  @Override
  public String literal() {
    return symbol;
  }
}