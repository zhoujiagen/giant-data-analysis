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
public enum RelationalComparisonOperatorEnum {
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
}
