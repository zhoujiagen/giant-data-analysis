package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 一元操作符:
 * 
 * <pre>
unaryOperator
    : '!' | '~' | '+' | '-' | NOT
 * </pre>
 */
public enum RelationalUnaryOperatorEnum implements RelationalAlgebraEnum {
  NOT1("!"), //
  NOT2("~"), //
  NOT3("NOT"), //
  POSITIVE("+"), //
  NEGATIVE("-");
  public final String symbol;

  RelationalUnaryOperatorEnum(String symbol) {
    this.symbol = symbol;
  }

  public static RelationalUnaryOperatorEnum of(String symbol) {
    if (symbol == null) {
      return null;
    }

    for (RelationalUnaryOperatorEnum e : RelationalUnaryOperatorEnum.values()) {
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