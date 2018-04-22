package com.spike.giantdataanalysis.task.store.exception;

/**
 * 任务存储异常.
 * @author zhoujiagen
 */
public class TaskStoreException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static TaskStoreException newException(String message) {
    return new TaskStoreException(message);
  }

  public static TaskStoreException newException(String message, Throwable cause) {
    return new TaskStoreException(message, cause);
  }

  public static TaskStoreException newException(Throwable cause) {
    return new TaskStoreException(cause);
  }

  public TaskStoreException(String message) {
    super(message);
  }

  public TaskStoreException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskStoreException(Throwable cause) {
    super(cause);
  }
}
