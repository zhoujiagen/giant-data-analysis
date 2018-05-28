package com.spike.giantdataanalysis.benchmark.client.raw.exception;

public class BenchmarkException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static BenchmarkException newException(String message) {
    return new BenchmarkException(message);
  }

  public static BenchmarkException newException(String message, Throwable cause) {
    return new BenchmarkException(message, cause);
  }

  public static BenchmarkException newException(Throwable cause) {
    return new BenchmarkException(cause);
  }

  public BenchmarkException(String message) {
    super(message);
  }

  public BenchmarkException(String message, Throwable cause) {
    super(message, cause);
  }

  public BenchmarkException(Throwable cause) {
    super(cause);
  }

}
