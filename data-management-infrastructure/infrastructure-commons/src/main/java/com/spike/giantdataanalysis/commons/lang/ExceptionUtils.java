package com.spike.giantdataanalysis.commons.lang;

/**
 * <pre>
 * 异常工具类
 * </pre>
 *
 * @author zhoujiagen
 */
public final class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {

  /**
   * <pre>
   * 非法参数异常
   * </pre>
   * 
   * @param message
   * @return
   */
  public static final IllegalArgumentException argumentException(String message) {
    return new IllegalArgumentException(message);
  }

  /**
   * <pre>
   * 非法状态异常
   * </pre>
   * 
   * @param message
   * @return
   */
  public static IllegalStateException stateException(String message) {
    return new IllegalStateException(message);
  }

  /**
   * <pre>
   * 不支持的操作异常
   * </pre>
   * 
   * @param message
   * @return
   */
  public static UnsupportedOperationException unsupport(String message) {
    return new UnsupportedOperationException(message);
  }

}
