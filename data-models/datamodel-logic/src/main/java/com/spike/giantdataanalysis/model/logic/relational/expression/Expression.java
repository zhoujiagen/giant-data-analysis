package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.NullNotnull;

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
    final Expression expression;

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
    final Expression first;
    final RelationalLogicalOperatorEnum operator;
    final Expression second;

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

    final PredicateExpression predicate;
    final Boolean not;
    final IsExpression.TestValue testValue;

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
    final PredicateExpression predicate;
    final Boolean not;
    final SelectStatement selectStatement;
    final Expressions expressions;

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
    final PredicateExpression predicate;
    final NullNotnull nullNotnull;

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
    final PredicateExpression left;
    final RelationalComparisonOperatorEnum comparisonOperator;
    final PredicateExpression right;

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
      sb.append(left);
      sb.append(" ").append(comparisonOperator.symbol);
      sb.append(" ").append(right);
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

    final PredicateExpression predicate;
    final RelationalComparisonOperatorEnum comparisonOperator;
    final SubqueryComparasionPredicate.QuantifierEnum quantifier;
    final SelectStatement selectStatement;

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
    final PredicateExpression first;
    final Boolean not;
    final PredicateExpression second;
    final PredicateExpression third;

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

    final PredicateExpression first;
    final PredicateExpression second;

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
    final PredicateExpression first;
    final Boolean not;
    final PredicateExpression second;
    final String stringLiteral;

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

    final PredicateExpression first;
    final Boolean not;
    final RegexpPredicate.RegexType regex;
    final PredicateExpression second;

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
    final String localId;
    final ExpressionAtom expressionAtom;

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
      sb.append(expressionAtom);
      return sb.toString();
    }

  }

  /**
   * 原子表达式:
   * 
   * <pre>
  expressionAtom
      : constant                                                      #constantExpressionAtom
      | fullColumnName                                                #fullColumnNameExpressionAtom
      | functionCall                                                  #functionCallExpressionAtom
      | expressionAtom COLLATE collationName                          #collateExpressionAtom
      | mysqlVariable                                                 #mysqlVariableExpressionAtom
      | unaryOperator expressionAtom                                  #unaryExpressionAtom
      | BINARY expressionAtom                                         #binaryExpressionAtom
      | '(' expression (',' expression)* ')'                          #nestedExpressionAtom
      | ROW '(' expression (',' expression)+ ')'                      #nestedRowExpressionAtom
      | EXISTS '(' selectStatement ')'                                #existsExpessionAtom
      | '(' selectStatement ')'                                       #subqueryExpessionAtom
      | INTERVAL expression intervalType                              #intervalExpressionAtom
      | left=expressionAtom bitOperator right=expressionAtom          #bitExpressionAtom
      | left=expressionAtom mathOperator right=expressionAtom         #mathExpressionAtom
      ;
   * </pre>
   */
  public interface ExpressionAtom extends Expression {

    public static class Collate implements ExpressionAtom {
      public final ExpressionAtom expressionAtom;
      public final CollationName collationName;

      Collate(ExpressionAtom expressionAtom, CollationName collationName) {
        Preconditions.checkArgument(expressionAtom != null);
        Preconditions.checkArgument(collationName != null);

        this.expressionAtom = expressionAtom;
        this.collationName = collationName;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append(expressionAtom.literal());
        sb.append(" COLLATE ");
        sb.append(collationName.literal());
        return sb.toString();
      }

    }

    public static class UnaryExpressionAtom implements ExpressionAtom {
      public final RelationalUnaryOperatorEnum unaryOperator;
      public final ExpressionAtom expressionAtom;

      UnaryExpressionAtom(RelationalUnaryOperatorEnum unaryOperator,
          ExpressionAtom expressionAtom) {
        Preconditions.checkArgument(unaryOperator != null);
        Preconditions.checkArgument(expressionAtom != null);

        this.unaryOperator = unaryOperator;
        this.expressionAtom = expressionAtom;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append(unaryOperator.symbol).append(" ");
        sb.append(expressionAtom.literal());
        return sb.toString();
      }

    }

    public static class BinaryExpressionAtom implements ExpressionAtom {
      public final ExpressionAtom expressionAtom;

      BinaryExpressionAtom(ExpressionAtom expressionAtom) {
        Preconditions.checkArgument(expressionAtom != null);

        this.expressionAtom = expressionAtom;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append("BINARY ").append(expressionAtom.literal());
        return sb.toString();
      }

    }

    public static class NestedExpressionAtom implements ExpressionAtom {
      public final List<Expression> expressions;

      NestedExpressionAtom(List<Expression> expressions) {
        Preconditions.checkArgument(expressions != null && expressions.size() > 0);

        this.expressions = expressions;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        List<String> literals = Lists.newArrayList();
        for (Expression expression : expressions) {
          literals.add(expression.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
        sb.append(")");
        return sb.toString();

      }

    }

    public static class NestedRowExpressionAtom implements ExpressionAtom {
      public final List<Expression> expressions;

      NestedRowExpressionAtom(List<Expression> expressions) {
        Preconditions.checkArgument(expressions != null && expressions.size() > 0);

        this.expressions = expressions;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROW(");
        List<String> literals = Lists.newArrayList();
        for (Expression expression : expressions) {
          literals.add(expression.literal());
        }
        sb.append(Joiner.on(", ").join(literals));
        sb.append(")");
        return sb.toString();
      }

    }

    public static class ExistsExpessionAtom implements ExpressionAtom {
      public final SelectStatement selectStatement;

      ExistsExpessionAtom(SelectStatement selectStatement) {
        Preconditions.checkArgument(selectStatement != null);

        this.selectStatement = selectStatement;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append("EXISTS(").append(selectStatement.literal()).append(")");
        return sb.toString();
      }

    }

    public static class SubqueryExpessionAtom implements ExpressionAtom {
      public final SelectStatement selectStatement;

      SubqueryExpessionAtom(SelectStatement selectStatement) {
        Preconditions.checkArgument(selectStatement != null);

        this.selectStatement = selectStatement;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(selectStatement.literal()).append(")");
        return sb.toString();
      }

    }

    public static class IntervalExpressionAtom implements ExpressionAtom {
      public final Expression expression;
      public final IntervalType intervalType;

      IntervalExpressionAtom(Expression expression, IntervalType intervalType) {
        Preconditions.checkArgument(expression != null);
        Preconditions.checkArgument(intervalType != null);

        this.expression = expression;
        this.intervalType = intervalType;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append("INTERVAL ");
        sb.append(expression.literal()).append(" ");
        sb.append(intervalType.literal());
        return sb.toString();
      }

    }

    public static class BitExpressionAtom implements ExpressionAtom {
      public final ExpressionAtom left;
      public final RelationalBitOperatorEnum bitOperator;
      public final ExpressionAtom right;

      BitExpressionAtom(ExpressionAtom left, RelationalBitOperatorEnum bitOperator,
          ExpressionAtom right) {
        Preconditions.checkArgument(left != null);
        Preconditions.checkArgument(bitOperator != null);
        Preconditions.checkArgument(right != null);

        this.left = left;
        this.bitOperator = bitOperator;
        this.right = right;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append(left.literal()).append(" ");
        sb.append(bitOperator.symbol).append(" ");
        sb.append(right.literal());
        return sb.toString();
      }

    }

    public static class MathExpressionAtom implements ExpressionAtom {
      public final ExpressionAtom left;
      public final RelationalMathOperatorEnum mathOperator;
      public final ExpressionAtom right;

      MathExpressionAtom(ExpressionAtom left, RelationalMathOperatorEnum mathOperator,
          ExpressionAtom right) {
        Preconditions.checkArgument(left != null);
        Preconditions.checkArgument(mathOperator != null);
        Preconditions.checkArgument(right != null);

        this.left = left;
        this.mathOperator = mathOperator;
        this.right = right;
      }

      @Override
      public String literal() {
        StringBuilder sb = new StringBuilder();
        sb.append(left.literal()).append(" ");
        sb.append(mathOperator.symbol).append(" ");
        sb.append(right.literal());
        return sb.toString();
      }

    }

  }
}