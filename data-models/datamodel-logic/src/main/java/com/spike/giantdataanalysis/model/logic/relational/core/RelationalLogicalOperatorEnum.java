package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 逻辑操作符.
 * 
 * <pre>
logicalOperator
    : AND | '&' '&' | XOR | OR | '|' '|'
 * </pre>
 */
public enum RelationalLogicalOperatorEnum implements RelationalAlgebraEnum {
  AND1("AND"), //
  AND2("&&"), //
  XOR("XOR"), //
  OR1("OR"), //
  OR2("||");

  public final String symbol;

  RelationalLogicalOperatorEnum(String symbol) {
    this.symbol = symbol;
  }

  public static RelationalLogicalOperatorEnum of(String symbol) {
    if (symbol == null) {
      return null;
    }

    for (RelationalLogicalOperatorEnum e : RelationalLogicalOperatorEnum.values()) {
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
