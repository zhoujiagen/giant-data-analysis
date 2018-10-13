package com.spike.giantdataanalysis.commons.lang;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 函数式风格工具类
 * @author zhoujiagen
 */
public class FPStyleUtils {
  /** 函数接口 */
  private interface Function<I, O> {

    /** 副作用输出 */
    public void output();

    /** 值输出 */
    public O output(I input);
  }

  /** 只执行副作用的函数 */
  public static abstract class SideEffectFunction<I, O> implements Function<I, O> {
    @Override
    public O output(I input) {
      throw new UnsupportedOperationException();
    }
  }

  /** 常规函数，有返回值 */
  public static abstract class RegularFunction<I, O> implements Function<I, O> {
    @Override
    public void output() {
      throw new UnsupportedOperationException();
    }
  }

  /** id函数，直接返回输入 */
  public static class IDFunction<I> extends RegularFunction<I, I> {
    @Override
    public I output(I input) {
      return input;
    }
  }

  /**
   * map
   * @param collection
   * @param function
   * @return
   */
  public static <E, O> Collection<O> map(Collection<E> collection, Function<E, O> function) {
    Collection<O> result = new ArrayList<O>();

    for (E e : collection) {
      result.add(function.output(e));
    }

    return result;
  }

  /**
   * flatMap
   * @param collection
   * @param function
   * @return
   */
  public static <E, O> Collection<O> flatMap(Collection<E> collection,
      Function<E, ? extends Collection<O>> function) {
    Collection<O> result = new ArrayList<O>();

    for (E e : collection) {
      result.addAll(function.output(e));
    }

    return result;
  }

}
