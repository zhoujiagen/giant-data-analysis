package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

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
      NULL_LITERAL, CONSTANT, CURRENTTIMESTAMP;

      @Override
      public String literal() {
        return name();
      }
    }

    public final DefaultValue.Type type;
    public final RelationalUnaryOperatorEnum unaryOperator;
    public final Constant constant;
    public final List<CurrentTimestamp> currentTimestamps;

    DefaultValue(DefaultValue.Type type, RelationalUnaryOperatorEnum unaryOperator,
        Constant constant, List<CurrentTimestamp> currentTimestamps) {
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();

      switch (type) {
      case NULL_LITERAL:
        sb.append("NULL");
        break;
      case CONSTANT:
        if (unaryOperator != null) {
          sb.append(unaryOperator.symbol).append(" ");
        }
        sb.append(constant.literal());
        break;
      case CURRENTTIMESTAMP:
        sb.append(currentTimestamps.get(0).literal());
        if (currentTimestamps.size() > 1) {
          sb.append(" ON UPDATE ").append(currentTimestamps.get(1).literal());
        }
        break;
      default:
        break;
      }

      return sb.toString();
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
      CURRENT_TIMESTAMP, LOCALTIME, LOCALTIMESTAMP, NOW;

      @Override
      public String literal() {
        return name();
      }
    }

    public final CurrentTimestamp.Type type;
    public final DecimalLiteral decimalLiteral;

    CurrentTimestamp(CurrentTimestamp.Type type, DecimalLiteral decimalLiteral) {
      Preconditions.checkArgument(type != null);

      this.type = type;
      this.decimalLiteral = decimalLiteral;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (CurrentTimestamp.Type.NOW.equals(type)) {
        sb.append("NOW (");
        if (decimalLiteral != null) {
          sb.append(decimalLiteral.literal());
        }
        sb.append(")");
      } else {
        sb.append(type.literal());
        if (decimalLiteral != null) {
          sb.append("(").append(decimalLiteral.literal()).append(")");
        }
      }
      return sb.toString();
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

    @Override
    public String literal() {
      if (expression == null) {
        return "DEFAULT";
      } else {
        return expression.literal();
      }
    }
  }

  /**
   * <pre>
  ifExists
    : IF EXISTS;
   * </pre>
   */
  public static class IfExists implements CommonExpressons {

    IfExists() {
    }

    @Override
    public String literal() {
      return "IF EXISTS";
    }
  }

  /**
   * <pre>
  ifNotExists
    : IF NOT EXISTS;
   * </pre>
   */
  public static class IfNotExists implements CommonExpressons {

    IfNotExists() {
    }

    @Override
    public String literal() {
      return "IF NOT EXISTS";
    }
  }
}
