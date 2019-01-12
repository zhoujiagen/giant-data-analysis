package com.spike.giantdataanalysis.communication.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Static Sequencer.
 */
public final class StaticSequencer {
  private static final Logger LOG = LoggerFactory.getLogger(StaticSequencer.class);

  private final SequenceCategory sequenceCategory;
  private final int length;
  private volatile String current;

  public StaticSequencer(SequenceCategory sequenceCategory, int length, String start) {
    super();
    this.sequenceCategory = sequenceCategory;
    this.length = length;
    this.current = Strings.padStart(start, length, sequenceCategory.padLeft);
  }

  public String current() {
    return current;
  }

  /**
   * Calculate next sequence with length <code>length</code> from <code>start</code> using step
   * <code>step</code>.
   * @param step
   * @return
   */
  public String next(final int step) {
    Preconditions.checkArgument(sequenceCategory != null);
    Preconditions.checkArgument(length > 0);
    String source = current;
    if (source == null) {
      source = new String(new char[] { sequenceCategory.padLeft });
    } else {
      for (char c : source.toCharArray()) {
        Preconditions.checkArgument(sequenceCategory.dict.indexOf(c) != -1);
        Preconditions.checkArgument(sequenceCategory.skip.indexOf(c) == -1);
      }
    }
    Preconditions.checkArgument(step > 0);

    char[] sourceArray = Strings.padStart(source, length, sequenceCategory.padLeft).toCharArray();
    int toAdd = step;
    // 从低位向高位处理
    for (int i = length - 1; i >= 0; i--) {
      toAdd = increase(sourceArray, i, sequenceCategory.dict, sequenceCategory.skip, toAdd);
      if (LOG.isDebugEnabled()) {
        LOG.debug("{}, index: {}, toAdd: {}", new String(sourceArray), i, toAdd);
      }
      if (toAdd == 0) {
        break;
      }
    }

    // 护卫语句
    String result = new String(sourceArray);
    Preconditions.checkState(result.length() == source.length());
    Preconditions.checkState(!result.equals(source));
    for (int i = 0; i < result.length(); i++) {
      char charInSource = source.charAt(i);
      char charInResult = result.charAt(i);
      // 第一个不相等的字符
      if (charInSource != charInResult) {
        Preconditions.checkState(//
          sequenceCategory.dict.indexOf(charInSource) < sequenceCategory.dict
              .indexOf(charInResult));
        break;
      }

    }

    current = result;
    return current;
  }

  /**
   * @param source OUT
   * @param index
   * @param dict
   * @param skip
   * @param toAdd
   * @return
   */
  private static int increase(char[] source, final int index, final String dict, final String skip,
      final int toAdd) {
    int incr = 0;

    final int dictLength = dict.length();

    int charIndex = dict.indexOf(source[index]); // 当前位置字符的索引
    int nextCharIndex = charIndex + toAdd;// 下一字符的索引

    // 头位置
    if (index == 0) {
      if (nextCharIndex >= dictLength) {
        throw new SequenceException("overflow");
      }
      char nextChar = dict.charAt(nextCharIndex);
      while (skip.indexOf(nextChar) != -1) {
        nextCharIndex += 1;
        if (nextCharIndex >= dictLength) {
          throw new SequenceException("overflow");
        }
        nextChar = dict.charAt(nextCharIndex);
      }
      source[index] = nextChar;
    }
    // 非头位置
    else {

      char nextChar = dict.charAt(nextCharIndex % dictLength);
      incr = nextCharIndex / dictLength; // 进位
      while (skip.indexOf(nextChar) != -1) {
        nextCharIndex += 1; // 下一个值
        nextChar = dict.charAt(nextCharIndex % dictLength);
        incr = nextCharIndex / dictLength; // 进位
      }
      source[index] = nextChar;
    }

    return incr;
  }

  public static class SequenceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static SequenceException newE() {
      return new SequenceException();
    }

    public static SequenceException newE(String message) {
      return new SequenceException(message);
    }

    public static SequenceException newE(String message, Throwable cause) {
      return new SequenceException(message, cause);
    }

    public static SequenceException newE(Throwable cause) {
      return new SequenceException(cause);
    }

    public static SequenceException newE(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
      return new SequenceException(message, cause, enableSuppression, writableStackTrace);
    }

    public SequenceException() {
      super();
    }

    public SequenceException(String message) {
      super(message);
    }

    public SequenceException(String message, Throwable cause) {
      super(message, cause);
    }

    public SequenceException(Throwable cause) {
      super(cause);
    }

    public SequenceException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
  }

  public static enum SequenceCategory {
    NUMBER("0123456789", "", '0'), //
    CHARACTER("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "", '0'), //
    NUMER_CHARACTER("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", "", '0'), //
    SPECIAL_CHARACTER("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ISOZ", '0'), //
    SPECIAL_NUMBER("0123456789", "0", '0'); //

    private String dict;
    private String skip;
    private char padLeft;

    SequenceCategory(String dict, String skip, char padLeft) {
      this.dict = dict;
      this.skip = skip;
      this.padLeft = padLeft;
    }

    public String dict() {
      return dict;
    }

    public String skip() {
      return skip;
    }

  }
}