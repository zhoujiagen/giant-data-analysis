package com.spike.giantdataanalysis.commons.guava.concurrency;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Monitor;

/**
 * <pre>
 * {@link Monitor}的实例
 * </pre>
 *
 * @author zhoujiagen
 * @see Monitor#enter()
 * @see Monitor#enter(long, java.util.concurrent.TimeUnit)
 * @see Monitor#enterIf(com.google.common.util.concurrent.Monitor.Guard)
 * @see Monitor#enterWhen(com.google.common.util.concurrent.Monitor.Guard)
 * @see Monitor#tryEnter()
 * @see Monitor#tryEnterIf(com.google.common.util.concurrent.Monitor.Guard)
 */
class ExampleMonitor {
  /** 最大元素数量 */
  public static final int MAX_ITEMS = 10;

  private List<String> items = Lists.newArrayList();
  private Monitor monitor = new Monitor();

  private Monitor.Guard guard = new Monitor.Guard(monitor) {
    @Override
    public boolean isSatisfied() {
      return items.size() < MAX_ITEMS;
    }
  };

  public void addItem(String item) throws InterruptedException {
    Preconditions.checkNotNull(item);

    // guard条件满足时，进入Monitor块
    // 线程持有Monitor实例
    // 任一时间只有一个线程可以进入Monitor块
    monitor.enterWhen(guard);

    try {
      // 添加元素
      items.add(item);
    } finally {
      // 离开monitor快
      // 线程不再持有Monitor实例
      monitor.leave();
    }
  }

  public void removeItem(int index) {
    Preconditions.checkElementIndex(index, items.size());

    if (monitor.enterIf(guard)) {
      try {
        // 移除元素
        items.remove(index);
      } finally {
        monitor.leave();
      }
    }
  }

}
