package com.spike.giantdataanalysis.commons.lang;

import java.util.concurrent.TimeUnit;

import com.spike.giantdataanalysis.commons.lang.FPStyleUtils.RegularFunction;

/**
 * 模拟工具类
 * @see RandomUtils
 * @author zhoujiagen
 */
public final class SimulationUtils {
  private static final SimulationUtils INSTANCE = new SimulationUtils();

  private SimulationUtils() {
  }

  public static final SimulationUtils getInstance() {
    return INSTANCE;
  }

  /** 延迟 */
  public void delay(int value, TimeUnit timeUnit) {
    if (timeUnit == null || value <= 0) return;

    long result = timeUnit.toMillis(value);

    if (result <= 0L) return;

    try {
      Thread.sleep(result);
    } catch (InterruptedException e) {
      /* ignore */}
  }

  /**
   * 远程服务
   * 
   * <pre>
   * 用例：
   * 
   * 		Integer result = SimulationUtils.getInstance().RPC(new Function<Integer, Integer>() {
   * 			&#64;Override
   * 			public Integer input() {
   * 				return 1;
   * 			}
   * 
   * 			&#64;Override
   * 			public Integer output() {
   * 				return input() + 1;
   * 			}
   * 		});
   * 
   * 		System.out.println(result);
   * </pre>
   * 
   * @param simulation
   * @return
   */
  public <I, O> O RPC(RegularFunction<I, O> function, I input) {
    int delay = RandomUtils.nextInt(1, 10);
    LogUtils.debug("Delay " + delay + " seconds...");
    delay(delay, TimeUnit.SECONDS);

    return function.output(input);
  }

  /** 预期存在问题的远程服务 */
  public <I, O> O problemicRPC(RegularFunction<I, O> function, I input, Throwable expectThrowable)
      throws Throwable {
    if (RandomUtils.nextBoolean()) throw expectThrowable;

    return RPC(function, input);
  }

}
