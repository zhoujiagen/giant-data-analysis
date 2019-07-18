package com.spike.giantdataanalysis.model.logic.relational.expression;

import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/** 关系代数基本表达式. */
public class RelationalAlgebraBasicExpression implements RelationalAlgebraExpression {
  protected final RelationalTuples tuples;

  RelationalAlgebraBasicExpression(RelationalTuples tuples) {
    this.tuples = tuples;
  }

  public RelationalTuples tuples() {
    return tuples;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}