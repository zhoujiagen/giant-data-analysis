package com.spike.giantdataanalysis.commons.guava.fp;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

/**
 * <pre>
 * {@link Suppliers}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestSuppliers {

  class InnerClass {
  }

  class MySupplier implements Supplier<Object> {
    @Override
    public Object get() {
      return new InnerClass();
    }
  }

  @Test
  public void _memoize() {
    MySupplier delegate = new MySupplier();
    System.out.println(delegate.get().hashCode());// 不一致
    System.out.println(delegate.get().hashCode());

    // 缓存
    Supplier<Object> wrapped = Suppliers.memoize(delegate);
    System.out.println(wrapped.get().hashCode());// 一致
    System.out.println(wrapped.get().hashCode());
  }

  @Test
  public void _memoizeWithExpiration() throws InterruptedException {
    MySupplier delegate = new MySupplier();
    System.out.println(delegate.get().hashCode());// 不一致
    System.out.println(delegate.get().hashCode());

    // 缓存3秒
    Supplier<Object> wrapped = Suppliers.memoizeWithExpiration(delegate, 3, TimeUnit.SECONDS);
    System.out.println(wrapped.get().hashCode());// 一致
    System.out.println(wrapped.get().hashCode());

    Thread.sleep(10 * 1000L);

    System.out.println(wrapped.get().hashCode());// 一致，但与休眠前的不一致
    System.out.println(wrapped.get().hashCode());
  }
}
