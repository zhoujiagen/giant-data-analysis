package com.spike.giantdataanalysis.text.elasticsearch.test;

import java.util.List;

import org.elasticsearch.test.ESTestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * <pre>
 * 运行配置参考: http://stackoverflow.com/questions/27163046/assertions-mismatch-ea-was-not-specified-but-dtests-asserts-true 
 * 
 * (1) eclipse preference -> junit -> Add -ea checkbox enable.
 * (2) right click on the eclipse project -> run as -> run configure -> arguments tab -> add the -ea option in vm arguments
 * 
 * java命令行参数(-ea) http://docs.oracle.com/javase/7/docs/technotes/tools/windows/java.html
 * 开启断言; 断言默认是关闭的.
 * </pre>
 */
public class SampleESUnitTest extends ESTestCase {

  @Test
  public void dummy() {
    int actual = 2;
    Assert.assertEquals(2, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void dummyWithError() {
    throw new UnsupportedOperationException();
  }

  /** called when a test fails, supplying the errors it generated */
  protected void afterIfFailed(List<Throwable> errors) {
    System.out.println("FAIL");
    System.err.println(errors);
  }

  /** called after a test is finished, but only if succesfull */
  protected void afterIfSuccessful() throws Exception {
    System.out.println("SUCCESS");
  }

}
