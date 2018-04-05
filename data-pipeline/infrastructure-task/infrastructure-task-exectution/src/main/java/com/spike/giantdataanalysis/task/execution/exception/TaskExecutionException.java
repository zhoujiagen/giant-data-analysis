package com.spike.giantdataanalysis.task.execution.exception;

/**
 * 任务执行异常.
 * @author zhoujiagen
 */
public class TaskExecutionException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static TaskExecutionException newException(String message) {
    return new TaskExecutionException(message);
  }

  public static TaskExecutionException newException(String message, Throwable cause) {
    return new TaskExecutionException(message, cause);
  }

  public static TaskExecutionException newException(Throwable cause) {
    return new TaskExecutionException(cause);
  }

  public TaskExecutionException(String message) {
    super(message);
  }

  public TaskExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskExecutionException(Throwable cause) {
    super(cause);
  }
}
