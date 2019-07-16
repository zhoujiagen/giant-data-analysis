package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.spike.giantdataanalysis.model.logic.relational.model.RelationalAttribute;

/** 选择表达式. */
public class RelationalAlgebraSelectExpression implements RelationalAlgebraExpression {
  final RelationalAlgebraExpression first;
  final List<RelationalAttribute> attributes;

  RelationalAlgebraSelectExpression(RelationalAlgebraExpression first,
      List<RelationalAttribute> attributes) {
    this.first = first;
    this.attributes = attributes;
  }
}