package com.spike.giantdataanalysis.coordination.exception;

/**
 * 协同异常.
 * @author zhoujiagen
 */
public class CoordinationException extends RuntimeException {
  private static final long serialVersionUID = 2180639260225399523L;

  public static CoordinationException newException(String message) {
    return new CoordinationException(message);
  }

  public static CoordinationException newException(String message, Throwable cause) {
    return new CoordinationException(message, cause);
  }

  public static CoordinationException newException(Throwable cause) {
    return new CoordinationException(cause);
  }

  public CoordinationException(String message) {
    super(message);
  }

  public CoordinationException(String message, Throwable cause) {
    super(message, cause);
  }

  public CoordinationException(Throwable cause) {
    super(cause);
  }
}
