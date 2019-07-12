package com.spike.giantdataanalysis.model.logic.relational;

import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraBasicExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraDifferenceExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraIntersectionExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraProjectExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionFactory.RelationalAlgebraUnionExpression;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalTuples;

/**
 * 关系代数表达式求值器.
 */
public final class RelationalAlgebraExpressionEvaluator {

  public static RelationalTuples eval(RelationalAlgebraExpression expression) {
    if (expression instanceof RelationalAlgebraBasicExpression) {
      return ((RelationalAlgebraBasicExpression) expression).tuples();
    }
    // Intersection
    else if (expression instanceof RelationalAlgebraIntersectionExpression) {
      RelationalAlgebraIntersectionExpression actualExpression =
          (RelationalAlgebraIntersectionExpression) expression;
      RelationalTuples firstTuples = eval(actualExpression.first());
      RelationalTuples secondTuples = eval(actualExpression.second());
      return RelationalModelUtils.evalIntersection(firstTuples, secondTuples);
    }
    // Union
    else if (expression instanceof RelationalAlgebraUnionExpression) {
      RelationalAlgebraUnionExpression actualExpression =
          (RelationalAlgebraUnionExpression) expression;
      RelationalTuples firstTuples = eval(actualExpression.first());
      RelationalTuples secondTuples = eval(actualExpression.second());
      return RelationalModelUtils.evalUnion(firstTuples, secondTuples);
    }
    // Difference
    else if (expression instanceof RelationalAlgebraDifferenceExpression) {
      RelationalAlgebraDifferenceExpression actualExpression =
          (RelationalAlgebraDifferenceExpression) expression;
      RelationalTuples firstTuples = eval(actualExpression.first());
      RelationalTuples secondTuples = eval(actualExpression.second());
      return RelationalModelUtils.evalDifference(firstTuples, secondTuples);
    }
    // Project
    else if (expression instanceof RelationalAlgebraProjectExpression) {
      RelationalAlgebraProjectExpression actualExpression =
          (RelationalAlgebraProjectExpression) expression;
      return RelationalModelUtils.evalProject(eval(actualExpression.first()),
        actualExpression.attributes());
    }

    //
    else {
      throw new UnsupportedOperationException();
    }
  }

}
