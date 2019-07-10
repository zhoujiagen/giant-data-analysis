package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraConditionalExpression.RelationalAlgebraPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraPrimitiveExpression.IntervalType;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraStatementExpression.SelectStatement;

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
public interface RelationalAlgebraExpressionAtom extends RelationalAlgebraPredicate {

  // constant #constantExpressionAtom
  public static class Constant implements RelationalAlgebraExpressionAtom {

    public static enum ConstantTypeEnum {
      STRING_LITERAL, //
      DECIMAL_LITERAL, //
      HEXADECIMAL_LITERAL, //
      BOOLEAN_LITERAL, //
      REAL_LITERAL, //
      BIT_STRING, //
      NULL_LITERAL;
    }

    final ConstantTypeEnum type;
    final String literal;

    final Boolean isNull; // maybe null

    Constant(ConstantTypeEnum type, String literal) {
      this.type = type;
      this.literal = literal;
      this.isNull = null;
    }

    Constant(ConstantTypeEnum type, String literal, Boolean isNull) {
      this.type = type;
      this.literal = literal;
      this.isNull = isNull;
    }

  }

  // fullColumnName #fullColumnNameExpressionAtom
  public static class FullColumnName implements RelationalAlgebraExpressionAtom {
    final FullColumnName fullColumnName;

    FullColumnName(FullColumnName fullColumnName) {
      this.fullColumnName = fullColumnName;
    }
  }

  // expressionAtom COLLATE collationName #collateExpressionAtom
  public static class CollateExpressionAtom implements RelationalAlgebraExpressionAtom {

  }

  // mysqlVariable #mysqlVariableExpressionAtom
  public static class MysqlVariable implements RelationalAlgebraExpressionAtom {
    final String localId;
    final String globalId;

    MysqlVariable(String localId, String globalId) {
      this.localId = localId;
      this.globalId = globalId;
    }
  }

  // unaryOperator expressionAtom #unaryExpressionAtom
  public static class UnaryExpressionAtom implements RelationalAlgebraExpressionAtom {
    final RelationalUnaryOperatorEnum unaryOperator;
    final RelationalAlgebraExpressionAtom expressionAtom;

    UnaryExpressionAtom(RelationalUnaryOperatorEnum unaryOperator,
        RelationalAlgebraExpressionAtom expressionAtom) {
      Preconditions.checkArgument(unaryOperator != null);
      Preconditions.checkArgument(expressionAtom != null);

      this.unaryOperator = unaryOperator;
      this.expressionAtom = expressionAtom;
    }
  }

  // BINARY expressionAtom #binaryExpressionAtom
  public static class RelationalAlgebraBinaryExpressionAtom
      implements RelationalAlgebraExpressionAtom {
    final RelationalAlgebraExpressionAtom expressionAtom;

    RelationalAlgebraBinaryExpressionAtom(RelationalAlgebraExpressionAtom expressionAtom) {
      Preconditions.checkArgument(expressionAtom != null);

      this.expressionAtom = expressionAtom;
    }
  }

  // '(' expression (',' expression)* ')' #nestedExpressionAtom
  public static class NestedExpressionAtom implements RelationalAlgebraExpressionAtom {
    final List<RelationalAlgebraConditionalExpression> expressions;

    NestedExpressionAtom(List<RelationalAlgebraConditionalExpression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }
  }

  // ROW '(' expression (',' expression)+ ')' #nestedRowExpressionAtom
  public static class NestedRowExpressionAtom implements RelationalAlgebraExpressionAtom {
    final List<RelationalAlgebraConditionalExpression> expressions;

    NestedRowExpressionAtom(List<RelationalAlgebraConditionalExpression> expressions) {
      Preconditions.checkArgument(expressions != null && expressions.size() > 0);

      this.expressions = expressions;
    }
  }

  // EXISTS '(' selectStatement ')' #existsExpessionAtom
  public static class ExistsExpessionAtom implements RelationalAlgebraExpressionAtom {
    final SelectStatement selectStatement;

    ExistsExpessionAtom(SelectStatement selectStatement) {
      Preconditions.checkArgument(selectStatement != null);

      this.selectStatement = selectStatement;
    }
  }

  // '(' selectStatement ')' #subqueryExpessionAtom
  public static class SubqueryExpessionAtom implements RelationalAlgebraExpressionAtom {
    final SelectStatement selectStatement;

    SubqueryExpessionAtom(SelectStatement selectStatement) {
      Preconditions.checkArgument(selectStatement != null);

      this.selectStatement = selectStatement;
    }
  }

  // INTERVAL expression intervalType #intervalExpressionAtom
  public static class IntervalExpressionAtom implements RelationalAlgebraExpressionAtom {
    final RelationalAlgebraConditionalExpression expression;
    final IntervalType intervalType;

    IntervalExpressionAtom(RelationalAlgebraConditionalExpression expression,
        IntervalType intervalType) {
      Preconditions.checkArgument(expression != null);
      Preconditions.checkArgument(intervalType != null);

      this.expression = expression;
      this.intervalType = intervalType;
    }
  }

  // left=expressionAtom bitOperator right=expressionAtom #bitExpressionAtom
  public static class BitExpressionAtom implements RelationalAlgebraExpressionAtom {
    final RelationalAlgebraExpressionAtom left;
    final RelationalBitOperatorEnum bitOperator;
    final RelationalAlgebraExpressionAtom right;

    BitExpressionAtom(RelationalAlgebraExpressionAtom left, RelationalBitOperatorEnum bitOperator,
        RelationalAlgebraExpressionAtom right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(bitOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.bitOperator = bitOperator;
      this.right = right;
    }
  }

  // left=expressionAtom mathOperator right=expressionAtom #mathExpressionAtom
  public static class MathExpressionAtom implements RelationalAlgebraExpressionAtom {
    final RelationalAlgebraExpressionAtom left;
    final RelationalMathOperatorEnum mathOperator;
    final RelationalAlgebraExpressionAtom right;

    MathExpressionAtom(RelationalAlgebraExpressionAtom left,
        RelationalMathOperatorEnum mathOperator, RelationalAlgebraExpressionAtom right) {
      Preconditions.checkArgument(left != null);
      Preconditions.checkArgument(mathOperator != null);
      Preconditions.checkArgument(right != null);

      this.left = left;
      this.mathOperator = mathOperator;
      this.right = right;
    }
  }

}