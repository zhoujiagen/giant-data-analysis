package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.NullNotnull;

/**
 * Expressions, predicates
 * 
 * <pre>
expression
    : notOperator=(NOT | '!') expression                            #notExpression
    | expression logicalOperator expression                         #logicalExpression
    | predicate IS NOT? testValue=(TRUE | FALSE | UNKNOWN)          #isExpression
    | predicate                                                     #predicateExpression
 * </pre>
 */
public interface Expression extends RelationalAlgebraExpression {

  public static class NotExpression implements Expression {
    public final Expression expression;

    NotExpression(Expression expression) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("NOT ").append(expression.literal());
      return sb.toString();
    }
  }

  public static class LogicalExpression implements Expression {
    public final Expression first;
    public final RelationalLogicalOperatorEnum operator;
    public final Expression second;

    LogicalExpression(Expression first, RelationalLogicalOperatorEnum operator, Expression second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(operator != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.operator = operator;
      this.second = second;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(first.literal()).append(" ");
      sb.append(operator.symbol).append(" ");
      sb.append(second.literal());
      return sb.toString();
    }
  }

  public static class IsExpression implements Expression {
    public static enum TestValue implements RelationalAlgebraEnum {
      TRUE, FALSE, UNKNOWN;
      @Override
      public String literal() {
        return name();
      }
    }

    public final PredicateExpression predicate;
    public final Boolean not;
    public final IsExpression.TestValue testValue;

    IsExpression(PredicateExpression predicate, Boolean not, IsExpression.TestValue testValue) {
      Preconditions.checkArgument(predicate != null);
      Preconditions.checkArgument(testValue != null);

      this.predicate = predicate;
      this.not = not;
      this.testValue = testValue;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(predicate.literal()).append(" ");
      sb.append("IS ");
      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      sb.append(testValue.literal());
      return sb.toString();
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
  public static interface PredicateExpression extends Expression {
  }

  public static class InPredicate implements PredicateExpression {
    public final PredicateExpression predicate;
    public final Boolean not;
    public final SelectStatement selectStatement;
    public final Expressions expressions;

    InPredicate(PredicateExpression predicate, Boolean not, SelectStatement selectStatement,
        Expressions expressions) {
      Preconditions.checkArgument(predicate != null);
      Preconditions.checkArgument(!(selectStatement == null && expressions == null));

      this.predicate = predicate;
      this.not = not;
      this.selectStatement = selectStatement;
      this.expressions = expressions;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(predicate.literal()).append(" ");
      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      sb.append("IN (");
      if (selectStatement != null) {
        sb.append(selectStatement.literal());
      } else {
        sb.append(expressions.literal());
      }
      sb.append(")");
      return sb.toString();
    }
  }

  public static class IsNullPredicate implements PredicateExpression {
    public final PredicateExpression predicate;
    public final NullNotnull nullNotnull;

    IsNullPredicate(PredicateExpression predicate, NullNotnull nullNotnull) {
      Preconditions.checkArgument(predicate != null);

      this.predicate = predicate;
      this.nullNotnull = nullNotnull;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(predicate.literal()).append(" ");
      sb.append("IS ");
      sb.append(nullNotnull.literal());
      return sb.toString();
    }
  }

  public static class BinaryComparasionPredicate implements PredicateExpression {
    public final PredicateExpression left;
    public final RelationalComparisonOperatorEnum comparisonOperator;
    public final PredicateExpression right;

    BinaryComparasionPredicate(PredicateExpression left,
        RelationalComparisonOperatorEnum comparisonOperator, PredicateExpression right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(comparisonOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.comparisonOperator = comparisonOperator;
      this.right = right;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(left.literal());
      sb.append(" ").append(comparisonOperator.symbol);
      sb.append(" ").append(right.literal());
      return sb.toString();
    }

  }

  public static class SubqueryComparasionPredicate implements PredicateExpression {
    public static enum QuantifierEnum implements RelationalAlgebraEnum {
      ALL, ANY, SOME;
      @Override
      public String literal() {
        return name();
      }
    }

    public final PredicateExpression predicate;
    public final RelationalComparisonOperatorEnum comparisonOperator;
    public final SubqueryComparasionPredicate.QuantifierEnum quantifier;
    public final SelectStatement selectStatement;

    SubqueryComparasionPredicate(PredicateExpression predicate,
        RelationalComparisonOperatorEnum comparisonOperator,
        SubqueryComparasionPredicate.QuantifierEnum quantifier, SelectStatement selectStatement) {
      Preconditions.checkArgument(predicate != null);
      Preconditions.checkArgument(comparisonOperator != null);
      Preconditions.checkArgument(quantifier != null);
      Preconditions.checkArgument(selectStatement != null);

      this.predicate = predicate;
      this.comparisonOperator = comparisonOperator;
      this.quantifier = quantifier;
      this.selectStatement = selectStatement;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(predicate.literal()).append(" ");
      sb.append(comparisonOperator.symbol);
      sb.append(quantifier.literal());
      sb.append("(");
      sb.append(selectStatement.literal());
      sb.append(")");
      return sb.toString();
    }
  }

  public static class BetweenPredicate implements PredicateExpression {
    public final PredicateExpression first;
    public final Boolean not;
    public final PredicateExpression second;
    public final PredicateExpression third;

    BetweenPredicate(PredicateExpression first, Boolean not, PredicateExpression second,
        PredicateExpression third) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);
      Preconditions.checkArgument(third != null);

      this.first = first;
      this.not = not;
      this.second = second;
      this.third = third;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(first.literal()).append(" ");
      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      sb.append("BETWEEN ");
      sb.append(second.literal()).append(" ");
      sb.append(third.literal());
      return sb.toString();
    }
  }

  public static class SoundsLikePredicate implements PredicateExpression {

    public final PredicateExpression first;
    public final PredicateExpression second;

    SoundsLikePredicate(PredicateExpression first, PredicateExpression second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.second = second;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(first.literal());
      sb.append(" SOUNDS LIKE ");
      sb.append(second.literal());
      return sb.toString();
    }
  }

  public static class LikePredicate implements PredicateExpression {
    public final PredicateExpression first;
    public final Boolean not;
    public final PredicateExpression second;
    public final String stringLiteral;

    LikePredicate(PredicateExpression first, Boolean not, PredicateExpression second,
        String stringLiteral) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.not = not;
      this.second = second;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(first).append(" ");

      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      sb.append("LIKE ");
      sb.append(second).append(" ");
      if (stringLiteral != null) {
        sb.append("ESCAPE ").append(stringLiteral);
      }
      return sb.toString();
    }

  }

  public static class RegexpPredicate implements PredicateExpression {
    public static enum RegexType implements RelationalAlgebraEnum {
      REGEXP, RLIKE;
      @Override
      public String literal() {
        return name();
      }
    }

    public final PredicateExpression first;
    public final Boolean not;
    public final RegexpPredicate.RegexType regex;
    public final PredicateExpression second;

    RegexpPredicate(PredicateExpression first, Boolean not, RegexpPredicate.RegexType regex,
        PredicateExpression second) {
      Preconditions.checkArgument(first != null);
      Preconditions.checkArgument(regex != null);
      Preconditions.checkArgument(second != null);

      this.first = first;
      this.not = not;
      this.regex = regex;
      this.second = second;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(first.literal()).append(" ");
      if (Boolean.TRUE.equals(not)) {
        sb.append("NOT ");
      }
      sb.append(regex.literal()).append(" ");
      sb.append(second.literal());
      return sb.toString();
    }
  }

  public static class ExpressionAtomPredicate implements PredicateExpression {
    public final String localId;
    public final ExpressionAtom expressionAtom;

    ExpressionAtomPredicate(String localId, ExpressionAtom expressionAtom) {
      Preconditions.checkArgument(expressionAtom != null);

      this.localId = localId;
      this.expressionAtom = expressionAtom;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (localId != null) {
        sb.append(localId).append(" := ");
      }
      sb.append(expressionAtom.literal());
      return sb.toString();
    }

  }

}