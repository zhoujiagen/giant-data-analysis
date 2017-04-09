package com.spike.giantdataanalysis.zookeeper.support;

/**
 * 自定义ZooKeeper应用的异常
 * @author zhoujiagen
 */
public class ZKAppException extends Exception {
  private static final long serialVersionUID = 1L;

  private String errorCode = "10000";

  /**
   * @param message
   */
  public ZKAppException(String message) {
    super(message);
  }

  /**
   * @param errorCode
   * @param message
   */
  public ZKAppException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  /**
   * @param message
   * @param cause
   */
  public ZKAppException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param errorCode
   * @param message
   * @param cause
   */
  public ZKAppException(String errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  /**
   * @param cause
   */
  public ZKAppException(Throwable cause) {
    super(cause);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String toString() {
    return "ZKAppException [errorCode=" + errorCode + ", message=" + getMessage() + "]";
  }

}
