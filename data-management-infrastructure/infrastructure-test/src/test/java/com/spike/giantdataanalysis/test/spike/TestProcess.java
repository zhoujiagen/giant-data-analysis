package com.spike.giantdataanalysis.test.spike;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

// REF: https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html
public class TestProcess {

  public static void main(String[] args) throws IOException {
    ProcessBuilder pb = new ProcessBuilder("ls", "-al", ".");

    // 设置环境变量
    Map<String, String> env = pb.environment();
    env.put("VAR1", "myValue");
    env.remove("OTHERVAR");
    env.put("VAR2", env.get("VAR1") + "suffix");

    // 设置运行目录
    pb.directory(new File("."));

    // 重定向输出
    File log = new File("log");
    pb.redirectErrorStream(true);
    pb.redirectOutput(Redirect.appendTo(log));

    // 启动
    Process p = pb.start();

    assert pb.redirectInput() == Redirect.PIPE;
    assert pb.redirectOutput().file() == log;
    assert p.getInputStream().read() == -1;
  }
}
