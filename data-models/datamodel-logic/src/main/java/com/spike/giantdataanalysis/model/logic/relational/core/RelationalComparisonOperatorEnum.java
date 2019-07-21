package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 比较操作符:
 * 
 * <pre>
comparisonOperator
    : '=' | '>' | '<' | '<' '=' | '>' '='
    | '<' '>' | '!' '=' | '<' '=' '>'
 * </pre>
 */
public enum RelationalComparisonOperatorEnum implements RelationalAlgebraEnum {
  EQ("="), //
  GT(">"), //
  LT("<"), //
  LE("<="), //
  GE(">="), //
  NEQ1("<>"), //
  NEQ2("!="), //
  NULL_SAFE_EQ("<=>");//

  public final String symbol;

  RelationalComparisonOperatorEnum(String symbol) {
    this.symbol = symbol;
  }

  public static RelationalComparisonOperatorEnum of(String symbol) {
    if (symbol == null) {
      return null;
    }

    for (RelationalComparisonOperatorEnum e : RelationalComparisonOperatorEnum.values()) {
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
