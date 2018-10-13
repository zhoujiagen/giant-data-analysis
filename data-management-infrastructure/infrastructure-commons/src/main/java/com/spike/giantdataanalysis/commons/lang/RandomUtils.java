package com.spike.giantdataanalysis.commons.lang;

import java.util.UUID;

/**
 * <pre>
 * 随机性工具类
 * </pre>
 *
 * @author zhoujiagen
 */
public final class RandomUtils extends org.apache.commons.lang3.RandomUtils {

  /**
   * <pre>
   * 随机产生布尔值
   * </pre>
   * 
   * @return
   */
  public static boolean nextBoolean() {
    int nextInt = nextInt(1, 100);
    if (nextInt % 2 == 0) {
      return true;
    } else {
      return false;
    }
  }

  public enum IdPattern {
    LowerCase, UpperCase
  }

  /**
   * 返回随机的ID串，不指定时为小写
   * @param idPattern ID的格式
   * @return
   */
  public static String nextId(IdPattern idPattern) {
    String result = UUID.randomUUID().toString().replaceAll("\\-", "");

    if (idPattern == null) {
      idPattern = IdPattern.LowerCase;
    }

    if (IdPattern.LowerCase.equals(idPattern)) {
      return result.toLowerCase();
    } else if (IdPattern.UpperCase.equals(idPattern)) {
      return result.toUpperCase();
    }

    return result;
  }

}
