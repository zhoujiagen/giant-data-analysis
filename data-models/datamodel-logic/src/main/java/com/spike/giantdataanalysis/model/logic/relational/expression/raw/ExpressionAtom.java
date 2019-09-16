package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement.IntervalType;

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
public interface ExpressionAtom extends RelationalAlgebraExpression {

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

    UnaryExpressionAtom(RelationalUnaryOperatorEnum unaryOperator, ExpressionAtom expressionAtom) {
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
    public final DdlStatement.IntervalType intervalType;

    IntervalExpressionAtom(Expression expression, DdlStatement.IntervalType intervalType) {
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