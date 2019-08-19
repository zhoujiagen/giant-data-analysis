package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpression;

/**
 * 表达式解释异常.
 */
public class REInterpreterError extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static REInterpreterError make() {
    return new REInterpreterError();
  }

  public static REInterpreterError make(String message) {
    return new REInterpreterError(message);
  }

  public static REInterpreterError make(RelationalAlgebraExpression expression) {
    return new REInterpreterError(
        expression.getClass().getName() + ": \n" + expression.literal());
  }

  REInterpreterError() {
    super();
  }

  REInterpreterError(String message) {
    super(message);
  }

  REInterpreterError(String message, Throwable cause) {
    super(message, cause);
  }

  REInterpreterError(Throwable cause) {
    super(cause);
  }

  protected REInterpreterError(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}