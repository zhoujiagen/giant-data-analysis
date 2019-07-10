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
public interface RelationalAlgebraConditionalExpression {

  // expressions : expression (',' expression)*
  public static class RelationalAlgebraConditionalExpressions {
    final List<RelationalAlgebraConditionalExpression> expressions;

    RelationalAlgebraConditionalExpressions(
        List<RelationalAlgebraConditionalExpression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }
  }

  // notOperator=(NOT | '!') expression #notExpression
  public static class RelationalAlgebraNotConditionalExpression
      implements RelationalAlgebraExpression {
    final RelationalAlgebraConditionalExpression expression;

    RelationalAlgebraNotConditionalExpression(RelationalAlgebraConditionalExpression expression) {
      this.expression = expression;
    }
  }

  // expression logicalOperator expression #logicalExpression
  public static class RelationalAlgebraLogicalConditionalExpression
      implements RelationalAlgebraConditionalExpression {
    final RelationalAlgebraConditionalExpression first;
    final RelationalLogicalOperatorEnum operator;
    final RelationalAlgebraConditionalExpression second;

    RelationalAlgebraLogicalConditionalExpression(RelationalAlgebraConditionalExpression first,
        RelationalLogicalOperatorEnum operator, RelationalAlgebraConditionalExpression second) {
      this.first = first;
      this.operator = operator;
      this.second = second;
    }
  }

  // predicate IS NOT? testValue=(TRUE | FALSE | UNKNOWN) #isExpression
  public static class RelationalAlgebraIsExpressionConditionalExpression
      implements RelationalAlgebraConditionalExpression {
    public enum TestValue {
      TRUE, FALSE, UNKNOWN
    }

    final RelationalAlgebraPredicate predicate;
    final Boolean not;
    final TestValue testValue;

    RelationalAlgebraIsExpressionConditionalExpression(RelationalAlgebraPredicate predicate,
        Boolean not, TestValue testValue) {
      this.predicate = predicate;
      this.not = not;
      this.testValue = testValue;
    }
  }

  // TODO(zhoujiagen) restart here!!!
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
  public static interface RelationalAlgebraPredicate
      extends RelationalAlgebraConditionalExpression {
  }

  // predicate NOT? IN '(' (selectStatement | expressions) ')' #inPredicate
  public static class RelationalAlgebraInPredicate implements RelationalAlgebraPredicate {
    final RelationalAlgebraPredicate predicate;
    final Boolean not;
    final SelectStatement selectStatement;
    final RelationalAlgebraConditionalExpressions expressions;

    RelationalAlgebraInPredicate(RelationalAlgebraPredicate predicate, Boolean not,
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
  public static class RelationalAlgebraIsNullPredicate implements RelationalAlgebraPredicate {
    final RelationalAlgebraPredicate predicate;
    final Boolean notNull;

    RelationalAlgebraIsNullPredicate(RelationalAlgebraPredicate predicate, Boolean notNull) {
      Preconditions.checkArgument(predicate != null);

      this.predicate = predicate;
      this.notNull = notNull;
    }
  }

  // left=predicate comparisonOperator right=predicate #binaryComparasionPredicate
  public static class RelationalAlgebraBinaryComparasionPredicate
      implements RelationalAlgebraPredicate {
    final RelationalAlgebraPredicate left;
    final RelationalComparisonOperatorEnum comparisonOperator;
    final RelationalAlgebraPredicate right;

    RelationalAlgebraBinaryComparasionPredicate(RelationalAlgebraPredicate left,
        RelationalComparisonOperatorEnum comparisonOperator, RelationalAlgebraPredicate right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(comparisonOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.comparisonOperator = comparisonOperator;
      this.right = right;
    }
  }

  // predicate comparisonOperator quantifier=(ALL | ANY | SOME) '(' selectStatement ')'
  // #subqueryComparasionPredicate
  public static class RelationalAlgebraSubqueryComparasionPredicatePredicate
      implements RelationalAlgebraPredicate {
    public static enum QuantifierEnum {
      ALL, ANY, SOME
    }

    final RelationalAlgebraPredicate predicate;
    final RelationalComparisonOperatorEnum comparisonOperator;
    final QuantifierEnum quantifier;
    final SelectStatement selectStatement;

    RelationalAlgebraSubqueryComparasionPredicatePredicate(RelationalAlgebraPredicate predicate,
        RelationalComparisonOperatorEnum comparisonOperator, QuantifierEnum quantifier,
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
  public static class RelationalAlgebraBetweenPredicate implements RelationalAlgebraPredicate {
    final RelationalAlgebraPredicate first;
    final Boolean not;
    final RelationalAlgebraPredicate second;
    final RelationalAlgebraPredicate third;

    RelationalAlgebraBetweenPredicate(RelationalAlgebraPredicate first, Boolean not,
        RelationalAlgebraPredicate second, RelationalAlgebraPredicate third) {
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
  public static class RelationalAlgebraSoundsLikePredicatePredicate
      implements RelationalAlgebraPredicate {

    final RelationalAlgebraPredicate first;
    final RelationalAlgebraPredicate second;

    RelationalAlgebraSoundsLikePredicatePredicate(RelationalAlgebraPredicate first,
        RelationalAlgebraPredicate second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.second = second;
    }
  }

  // predicate NOT? LIKE predicate (ESCAPE STRING_LITERAL)? #likePredicate
  public static class RelationalAlgebraLikePredicate implements RelationalAlgebraPredicate {
    final RelationalAlgebraPredicate first;
    final Boolean not;
    final RelationalAlgebraPredicate second;
    final String stringLiteral;

    RelationalAlgebraLikePredicate(RelationalAlgebraPredicate first, Boolean not,
        RelationalAlgebraPredicate second, String stringLiteral) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.not = not;
      this.second = second;
      this.stringLiteral = stringLiteral;
    }
  }

  // predicate NOT? regex=(REGEXP | RLIKE) predicate #regexpPredicate
  public static class RelationalAlgebraRegexpPredicate implements RelationalAlgebraPredicate {
    public static enum RegexType {
      REGEXP, RLIKE
    }

    final RelationalAlgebraPredicate first;
    final Boolean not;
    final RegexType regex;
    final RelationalAlgebraPredicate second;

    RelationalAlgebraRegexpPredicate(RelationalAlgebraPredicate first, Boolean not, RegexType regex,
        RelationalAlgebraPredicate second) {
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
      implements RelationalAlgebraPredicate {
    final String localId;
    final RelationalAlgebraExpressionAtom expressionAtom;

    RelationalAlgebraExpressionAtomPredicate(String localId,
        RelationalAlgebraExpressionAtom expressionAtom) {
      Preconditions.checkArgument(expressionAtom != null);

      this.localId = localId;
      this.expressionAtom = expressionAtom;
    }
  }
}