package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpression;

/**
 * 表达式解释异常.
 */
public class RelationalExpressionInterpreterError extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static RelationalExpressionInterpreterError make() {
    return new RelationalExpressionInterpreterError();
  }

  public static RelationalExpressionInterpreterError make(String message) {
    return new RelationalExpressionInterpreterError(message);
  }

  public static RelationalExpressionInterpreterError make(RelationalAlgebraExpression expression) {
    return new RelationalExpressionInterpreterError(
        expression.getClass().getName() + ": \n" + expression.literal());
  }

  RelationalExpressionInterpreterError() {
    super();
  }

  RelationalExpressionInterpreterError(String message) {
    super(message);
  }

  RelationalExpressionInterpreterError(String message, Throwable cause) {
    super(message, cause);
  }

  RelationalExpressionInterpreterError(Throwable cause) {
    super(cause);
  }

  protected RelationalExpressionInterpreterError(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}