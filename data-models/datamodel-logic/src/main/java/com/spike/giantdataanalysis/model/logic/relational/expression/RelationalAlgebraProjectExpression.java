package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;

/** 投影表达式. */
public class RelationalAlgebraProjectExpression implements RelationalAlgebraExpression {
  final RelationalAlgebraExpression first;
  final List<RelationalAttribute> attributes;

  RelationalAlgebraProjectExpression(RelationalAlgebraExpression first,
      List<RelationalAttribute> attributes) {
    this.first = first;
    this.attributes = attributes;
  }

  public RelationalAlgebraExpression first() {
    return first;
  }

  public List<RelationalAttribute> attributes() {
    return attributes;
  }
}