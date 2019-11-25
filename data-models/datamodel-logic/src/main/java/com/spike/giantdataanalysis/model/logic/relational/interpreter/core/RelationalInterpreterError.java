package com.spike.giantdataanalysis.model.logic.relational.interpreter.core;

import com.spike.giantdataanalysis.model.logic.relational.expression.raw.RelationalAlgebraExpression;

/**
 * 解释异常.
 */
public class RelationalInterpreterError extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static RelationalInterpreterError make() {
    return new RelationalInterpreterError();
  }

  public static RelationalInterpreterError make(String message) {
    return new RelationalInterpreterError(message);
  }

  public static RelationalInterpreterError make(RelationalAlgebraExpression expression) {
    return new RelationalInterpreterError(
        expression.getClass().getName() + ": \n" + expression.literal());
  }

  RelationalInterpreterError() {
    super();
  }

  RelationalInterpreterError(String message) {
    super(message);
  }

  RelationalInterpreterError(String message, Throwable cause) {
    super(message, cause);
  }

  RelationalInterpreterError(Throwable cause) {
    super(cause);
  }

  protected RelationalInterpreterError(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}