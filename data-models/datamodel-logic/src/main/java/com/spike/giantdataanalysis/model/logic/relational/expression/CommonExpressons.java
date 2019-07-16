package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

/**
 * Common Expressons
 */
public interface CommonExpressons extends PrimitiveExpression {

  /**
   * <pre>
  defaultValue
    : NULL_LITERAL
    | unaryOperator? constant
    | currentTimestamp (ON UPDATE currentTimestamp)?
    ;
   * </pre>
   */
  public static class DefaultValue implements CommonExpressons {
    public static enum Type implements RelationalAlgebraEnum {
      NULL_LITERAL, CONSTANT, CURRENTTIMESTAMP
    }

    public final DefaultValue.Type type;
    public final RelationalUnaryOperatorEnum unaryOperator;
    public final Constant constant;
    public final List<CurrentTimestamp> currentTimestamps;

    DefaultValue(Type type, RelationalUnaryOperatorEnum unaryOperator, Constant constant,
        List<CurrentTimestamp> currentTimestamps) {
      Preconditions.checkArgument(type != null);
      switch (type) {
      case NULL_LITERAL:
        break;
      case CONSTANT:
        Preconditions.checkArgument(constant != null);
        break;
      case CURRENTTIMESTAMP:
        Preconditions.checkArgument(currentTimestamps != null && currentTimestamps.size() > 0);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }

      this.type = type;
      this.unaryOperator = unaryOperator;
      this.constant = constant;
      this.currentTimestamps = currentTimestamps;
    }
  }

  /**
   * <pre>
  currentTimestamp
    :
    (
      (CURRENT_TIMESTAMP | LOCALTIME | LOCALTIMESTAMP) ('(' decimalLiteral? ')')?
      | NOW '(' decimalLiteral? ')'
    )
    ;
   * </pre>
   */
  public static class CurrentTimestamp implements CommonExpressons {
    public static enum Type implements RelationalAlgebraEnum {
      CURRENT_TIMESTAMP, LOCALTIME, LOCALTIMESTAMP, NOW
    }

    public final CurrentTimestamp.Type type;
    public final DecimalLiteral decimalLiteral;

    CurrentTimestamp(CurrentTimestamp.Type type, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(type != null);

      this.type = type;
      this.decimalLiteral = decimalLiteral;
    }
  }

  /**
   * <pre>
  expressionOrDefault
    : expression | DEFAULT
    ;
   * </pre>
   */
  public static class ExpressionOrDefault implements CommonExpressons {
    public final Expression expression;

    ExpressionOrDefault(Expression expression, Boolean isDefault) {
      this.expression = expression;
    }
  }

  /**
   * <pre>
  ifExists
    : IF EXISTS;
   * </pre>
   */
  public static class IfExists implements CommonExpressons {
  }

  /**
   * <pre>
  ifNotExists
    : IF NOT EXISTS;
   * </pre>
   */
  public static class IfNotExists implements CommonExpressons {
  }
}
