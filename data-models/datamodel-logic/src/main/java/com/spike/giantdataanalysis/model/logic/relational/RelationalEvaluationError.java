package com.spike.giantdataanalysis.model.logic.relational;

/**
 * 求值错误异常.
 */
public class RelationalEvaluationError extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static RelationalEvaluationError make() {
    return new RelationalEvaluationError();
  }

  public static RelationalEvaluationError make(String message) {
    return new RelationalEvaluationError(message);
  }

  RelationalEvaluationError() {
    super();
  }

  RelationalEvaluationError(String message) {
    super(message);
  }

  RelationalEvaluationError(String message, Throwable cause) {
    super(message, cause);
  }

  RelationalEvaluationError(Throwable cause) {
    super(cause);
  }

  protected RelationalEvaluationError(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}