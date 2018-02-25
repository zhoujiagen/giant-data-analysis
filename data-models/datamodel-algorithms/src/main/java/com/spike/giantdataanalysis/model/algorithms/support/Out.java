package com.spike.giantdataanalysis.model.algorithms.support;

public final class Out {

  /** 数组的字符串表示. */
  public static String out(Object[] array) {
    if (array == null || array.length == 0) {
      return "";
    }

    // 原因是删除数组中元素后, 置为null
    // return Joiner.on(" ").join(array).toString();
    StringBuilder sb = new StringBuilder();
    for (Object e : array) {
      if (e == null) {
        sb.append("NULL ");
      } else {
        sb.append(e.toString() + " ");
      }
    }
    return sb.toString();
  }
}
