package com.spike.giantdataanalysis.model.logic.relational.core;

/**
 * 聚合操作符.
 */
public enum RelationalAggregateOperatorEnum implements RelationalAlgebraEnum {
  SUM, //
  AVG, //
  MAX, //
  MIN, //
  COUNT;

  @Override
  public String literal() {
    return name();
  }

}