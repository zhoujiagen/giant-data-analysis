package com.spike.giantdataanalysis.titan.support;

public class TitanAppException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static TitanAppException newException(String message) {
    return new TitanAppException(message);
  }

  public static TitanAppException newException(String message, Throwable cause) {
    return new TitanAppException(message, cause);
  }

  public static TitanAppException newException(Throwable cause) {
    return new TitanAppException(cause);
  }

  public TitanAppException(String message) {
    super(message);
  }

  public TitanAppException(String message, Throwable cause) {
    super(message, cause);
  }

  public TitanAppException(Throwable cause) {
    super(cause);
  }

}
