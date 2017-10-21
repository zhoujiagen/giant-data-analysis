package com.spike.giantdataanalysis.etl.exception;

public class ETLException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static ETLException newException(String message) {
    return new ETLException(message);
  }

  public static ETLException newException(String message, Throwable cause) {
    return new ETLException(message, cause);
  }

  public static ETLException newException(Throwable cause) {
    return new ETLException(cause);
  }

  public ETLException(String message) {
    super(message);
  }

  public ETLException(String message, Throwable cause) {
    super(message, cause);
  }

  public ETLException(Throwable cause) {
    super(cause);
  }

}
