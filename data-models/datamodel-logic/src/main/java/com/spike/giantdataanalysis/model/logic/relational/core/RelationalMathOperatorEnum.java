package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 数学运算操作符.
 * 
 * <pre>
mathOperator
    : '*' | '/' | '%' | DIV | MOD | '+' | '-' | '--'
 * </pre>
 */
public enum RelationalMathOperatorEnum implements RelationalAlgebraEnum {
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

  public static RelationalMathOperatorEnum of(String symbol) {
    if (symbol == null) {
      return null;
    }

    for (RelationalMathOperatorEnum e : RelationalMathOperatorEnum.values()) {
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