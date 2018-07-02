package com.spike.text.lucene.util;

/**
 * Lucene Application Exception
 * @author zhoujiagen<br/>
 *         Sep 2, 2015 11:07:18 PM
 */
public class LuceneAppException extends RuntimeException {
  private static final long serialVersionUID = -3617327630888865430L;

  public LuceneAppException() {
    super();
  }

  public LuceneAppException(String message) {
    super(message);
  }

  public LuceneAppException(String message, Throwable cause) {
    super(message, cause);
  }

  protected LuceneAppException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
