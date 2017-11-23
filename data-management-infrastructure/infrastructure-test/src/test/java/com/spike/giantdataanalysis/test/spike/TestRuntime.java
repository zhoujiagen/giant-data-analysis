package com.spike.giantdataanalysis.test.spike;

// REF: https://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html
public class TestRuntime {

  public static void main(String[] args) {
    Runtime runtime = Runtime.getRuntime();

    // 跟踪指令
    runtime.traceInstructions(true);
    // 跟踪方法调用
    runtime.traceMethodCalls(true);

    // 加载动态库
    // runtime.load("/home/avh/lib/libX11.so");

    // 处理器
    System.out.println("availableProcessors=" + runtime.availableProcessors());

    // 内存
    System.out.println("maxMemory=" + runtime.maxMemory());// 单位: 字节
    System.out.println("totalMemory=" + runtime.totalMemory());
    System.out.println("freeMemory=" + runtime.freeMemory());

    // GC
    runtime.runFinalization();
    runtime.gc();

    // 退出
    runtime.addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        System.err.println("in shutdown hook");
      }
    }));
    // System.exit(-1);
    runtime.halt(-1);
  }
}
