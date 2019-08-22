package com.spike.giantdataanalysis.model.logic.relational.interpreter;

/** 符号类型. */
public enum RESymbolTypeEnum implements RelationalInterpreterEnum {
  CONSTANT, //
  VARIABLE, //
  MYSQL_VARIABLE, //
  DATABASE_NAME, //
  TABLE_NAME, //
  ATTRIBUTE_NAME, //
  ALL_ATTRIBUTE_NAME, // *
  FUNCTION_NAME, //
  OPERATOR_NAME, //
  SPECIFIER_SELECT, // SELECT限定符
  SPECIFIER_ORDER, // ORDER的限定符
  SPEFICIER_GROUP_BY, // GROUP BY的限定符
  SPEFICIER_JOIN, // LEFT, RIGHT
  SPEFICIER_TEST_VALUE, // NOT
  SPEFICIER_AGG, // AggregateWindowedFunction.AggregatorEnum
  QUALIFIER_PREDICATE, // SOME, ANY, ALL
  REGEX_TYPE, // RegexpPredicate.RegexType
  INTERVAL_TYPE, // DdlStatement.IntervalType
  
  COLLATION_NAME;
  // others

  @Override
  public String literal() {
    return name();
  }
}