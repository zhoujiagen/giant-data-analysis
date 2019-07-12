package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectStatement;

/**
 * 条件表达式:
 * 
 * <pre>
expression
    : notOperator=(NOT | '!') expression                            #notExpression
    | expression logicalOperator expression                         #logicalExpression
    | predicate IS NOT? testValue=(TRUE | FALSE | UNKNOWN)          #isExpression
    | predicate                                                     #predicateExpression
 * </pre>
 */
public interface RelationalAlgebraConditionalExpression extends RelationalAlgebraExpression {

  // expressions : expression (',' expression)*
  public static class RelationalAlgebraConditionalExpressions
      implements RelationalAlgebraExpression {
    final List<RelationalAlgebraConditionalExpression> expressions;

    RelationalAlgebraConditionalExpressions(
        List<RelationalAlgebraConditionalExpression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }
  }

  // notOperator=(NOT | '!') expression #notExpression
  public static class RelationalAlgebraNotExpression
      implements RelationalAlgebraConditionalExpression {
    final RelationalAlgebraConditionalExpression expression;

    RelationalAlgebraNotExpression(RelationalAlgebraConditionalExpression expression) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
    }
  }

  // expression logicalOperator expression #logicalExpression
  public static class RelationalAlgebraLogicalExpression
      implements RelationalAlgebraConditionalExpression {
    final RelationalAlgebraConditionalExpression first;
    final RelationalLogicalOperatorEnum operator;
    final RelationalAlgebraConditionalExpression second;

    RelationalAlgebraLogicalExpression(RelationalAlgebraConditionalExpression first,
        RelationalLogicalOperatorEnum operator, RelationalAlgebraConditionalExpression second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(operator != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.operator = operator;
      this.second = second;
    }
  }

  // predicate IS NOT? testValue=(TRUE | FALSE | UNKNOWN) #isExpression
  public static class RelationalAlgebraIsExpression
      implements RelationalAlgebraConditionalExpression {
    public enum TestValue {
      TRUE, FALSE, UNKNOWN
    }

    final RelationalAlgebraPredicateExpression predicate;
    final Boolean not;
    final RelationalAlgebraIsExpression.TestValue testValue;

    RelationalAlgebraIsExpression(RelationalAlgebraPredicateExpression predicate, Boolean not,
        RelationalAlgebraIsExpression.TestValue testValue) {
      Preconditions.checkArgument(predicate != null);
      Preconditions.checkArgument(testValue != null);

      this.predicate = predicate;
      this.not = not;
      this.testValue = testValue;
    }
  }

  /**
   * 谓词条件表达式.
   * 
   * <pre>
  predicate
    : predicate NOT? IN '(' (selectStatement | expressions) ')'     #inPredicate
    | predicate IS nullNotnull                                      #isNullPredicate
    | left=predicate comparisonOperator right=predicate             #binaryComparasionPredicate
    | predicate comparisonOperator
      quantifier=(ALL | ANY | SOME) '(' selectStatement ')'         #subqueryComparasionPredicate
    | predicate NOT? BETWEEN predicate AND predicate                #betweenPredicate
    | predicate SOUNDS LIKE predicate                               #soundsLikePredicate
    | predicate NOT? LIKE predicate (ESCAPE STRING_LITERAL)?        #likePredicate
    | predicate NOT? regex=(REGEXP | RLIKE) predicate               #regexpPredicate
    | (LOCAL_ID VAR_ASSIGN)? expressionAtom                         #expressionAtomPredicate
    ;
   * </pre>
   */
  public static interface RelationalAlgebraPredicateExpression
      extends RelationalAlgebraConditionalExpression {
  }

  // predicate NOT? IN '(' (selectStatement | expressions) ')' #inPredicate
  public static class RelationalAlgebraInPredicate implements RelationalAlgebraPredicateExpression {
    final RelationalAlgebraPredicateExpression predicate;
    final Boolean not;
    final SelectStatement selectStatement;
    final RelationalAlgebraConditionalExpressions expressions;

    RelationalAlgebraInPredicate(RelationalAlgebraPredicateExpression predicate, Boolean not,
        SelectStatement selectStatement, RelationalAlgebraConditionalExpressions expressions) {
      Preconditions.checkArgument(predicate != null);
      Preconditions.checkArgument(!(selectStatement == null && expressions == null));

      this.predicate = predicate;
      this.not = not;
      this.selectStatement = selectStatement;
      this.expressions = expressions;
    }
  }

  // predicate IS nullNotnull #isNullPredicate
  public static class RelationalAlgebraIsNullPredicate
      implements RelationalAlgebraPredicateExpression {
    final RelationalAlgebraPredicateExpression predicate;
    final Boolean notNull;

    RelationalAlgebraIsNullPredicate(RelationalAlgebraPredicateExpression predicate,
        Boolean notNull) {
      Preconditions.checkArgument(predicate != null);

      this.predicate = predicate;
      this.notNull = notNull;
    }
  }

  // left=predicate comparisonOperator right=predicate #binaryComparasionPredicate
  public static class RelationalAlgebraBinaryComparasionPredicate
      implements RelationalAlgebraPredicateExpression {
    final RelationalAlgebraPredicateExpression left;
    final RelationalComparisonOperatorEnum comparisonOperator;
    final RelationalAlgebraPredicateExpression right;

    RelationalAlgebraBinaryComparasionPredicate(RelationalAlgebraPredicateExpression left,
        RelationalComparisonOperatorEnum comparisonOperator,
        RelationalAlgebraPredicateExpression right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(comparisonOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.comparisonOperator = comparisonOperator;
      this.right = right;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(left);
      builder.append(" ").append(comparisonOperator.symbol);
      builder.append(" ").append(right);
      return builder.toString();
    }

  }

  // predicate comparisonOperator quantifier=(ALL | ANY | SOME) '(' selectStatement ')'
  // #subqueryComparasionPredicate
  public static class RelationalAlgebraSubqueryComparasionPredicate
      implements RelationalAlgebraPredicateExpression {
    public static enum QuantifierEnum {
      ALL, ANY, SOME
    }

    final RelationalAlgebraPredicateExpression predicate;
    final RelationalComparisonOperatorEnum comparisonOperator;
    final RelationalAlgebraSubqueryComparasionPredicate.QuantifierEnum quantifier;
    final SelectStatement selectStatement;

    RelationalAlgebraSubqueryComparasionPredicate(RelationalAlgebraPredicateExpression predicate,
        RelationalComparisonOperatorEnum comparisonOperator,
        RelationalAlgebraSubqueryComparasionPredicate.QuantifierEnum quantifier,
        SelectStatement selectStatement) {
      Preconditions.checkArgument(predicate != null);
      Preconditions.checkArgument(comparisonOperator != null);
      Preconditions.checkArgument(quantifier != null);
      Preconditions.checkArgument(selectStatement != null);

      this.predicate = predicate;
      this.comparisonOperator = comparisonOperator;
      this.quantifier = quantifier;
      this.selectStatement = selectStatement;
    }
  }

  // predicate NOT? BETWEEN predicate AND predicate #betweenPredicate
  public static class RelationalAlgebraBetweenPredicate
      implements RelationalAlgebraPredicateExpression {
    final RelationalAlgebraPredicateExpression first;
    final Boolean not;
    final RelationalAlgebraPredicateExpression second;
    final RelationalAlgebraPredicateExpression third;

    RelationalAlgebraBetweenPredicate(RelationalAlgebraPredicateExpression first, Boolean not,
        RelationalAlgebraPredicateExpression second, RelationalAlgebraPredicateExpression third) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);
      Preconditions.checkArgument(third != null);

      this.first = first;
      this.not = not;
      this.second = second;
      this.third = third;
    }
  }

  // predicate SOUNDS LIKE predicate #soundsLikePredicate
  public static class RelationalAlgebraSoundsLikePredicate
      implements RelationalAlgebraPredicateExpression {

    final RelationalAlgebraPredicateExpression first;
    final RelationalAlgebraPredicateExpression second;

    RelationalAlgebraSoundsLikePredicate(RelationalAlgebraPredicateExpression first,
        RelationalAlgebraPredicateExpression second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.second = second;
    }
  }

  // predicate NOT? LIKE predicate (ESCAPE STRING_LITERAL)? #likePredicate
  public static class RelationalAlgebraLikePredicate
      implements RelationalAlgebraPredicateExpression {
    final RelationalAlgebraPredicateExpression first;
    final Boolean not;
    final RelationalAlgebraPredicateExpression second;
    final String stringLiteral;

    RelationalAlgebraLikePredicate(RelationalAlgebraPredicateExpression first, Boolean not,
        RelationalAlgebraPredicateExpression second, String stringLiteral) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.not = not;
      this.second = second;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(first).append(" ");

      if (Boolean.TRUE.equals(not)) {
        builder.append("NOT ");
      }
      builder.append("LIKE ");
      builder.append(second).append(" ");
      if (stringLiteral != null) {
        builder.append("ESCAPE ").append(stringLiteral);
      }
      return builder.toString();
    }

  }

  // predicate NOT? regex=(REGEXP | RLIKE) predicate #regexpPredicate
  public static class RelationalAlgebraRegexpPredicate
      implements RelationalAlgebraPredicateExpression {
    public static enum RegexType {
      REGEXP, RLIKE
    }

    final RelationalAlgebraPredicateExpression first;
    final Boolean not;
    final RelationalAlgebraRegexpPredicate.RegexType regex;
    final RelationalAlgebraPredicateExpression second;

    RelationalAlgebraRegexpPredicate(RelationalAlgebraPredicateExpression first, Boolean not,
        RelationalAlgebraRegexpPredicate.RegexType regex,
        RelationalAlgebraPredicateExpression second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(regex != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.not = not;
      this.regex = regex;
      this.second = second;
    }
  }

  // (LOCAL_ID VAR_ASSIGN)? expressionAtom #expressionAtomPredicate
  public static class RelationalAlgebraExpressionAtomPredicate
      implements RelationalAlgebraPredicateExpression {
    final String localId;
    final RelationalAlgebraExpressionAtom expressionAtom;

    RelationalAlgebraExpressionAtomPredicate(String localId,
        RelationalAlgebraExpressionAtom expressionAtom) {
      Preconditions.checkArgument(expressionAtom != null);

      this.localId = localId;
      this.expressionAtom = expressionAtom;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      if (localId != null) {
        builder.append(localId).append(" := ");
      }
      builder.append(expressionAtom);
      return builder.toString();
    }

  }
}