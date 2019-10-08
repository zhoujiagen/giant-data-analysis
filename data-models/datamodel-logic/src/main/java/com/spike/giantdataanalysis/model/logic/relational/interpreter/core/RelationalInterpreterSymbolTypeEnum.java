package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

/** 符号类型. */
public enum RelationalInterpreterSymbolTypeEnum implements RelationalInterpreterEnum {
  CONSTANT, //
  LOCAL_ID, //
  MYSQL_VARIABLE, //
  DATABASE_NAME, //
  TABLE_NAME, //
  ATTRIBUTE_NAME, //
  FUNCTION_ATTRIBUTE_NAME, // 函数结果作为属性
  ALL_ATTRIBUTE_NAME, // *
  FUNCTION_NAME, //
  OPERATOR_NAME, //
  COLLATION_NAME, //

  SPECIFIER_SELECT, // SELECT限定符
  SPECIFIER_ORDER, // ORDER的限定符
  SPEFICIER_GROUP_BY, // GROUP BY的限定符
  SPEFICIER_JOIN, // LEFT, RIGHT
  SPEFICIER_TEST_VALUE, // NOT
  SPEFICIER_AGG, // AggregateWindowedFunction.AggregatorEnum
  QUALIFIER_PREDICATE, // SOME, ANY, ALL
  REGEX_TYPE, // RegexpPredicate.RegexType
  INTERVAL_TYPE, // DdlStatement.IntervalType

  FUNCTION_CALL, // 函数调用
  EXPRESSION, // 谓词表达式
  EXPRESSION_ATOM, // 原子谓词表达式

  // others
  ;

  @Override
  public String literal() {
    return name();
  }
}