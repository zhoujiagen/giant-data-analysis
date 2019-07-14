package com.spike.giantdataanalysis.communication.example.grpc;

import org.junit.BeforeClass;
import org.junit.Test;

import com.spike.giantdataanalysis.communication.example.util.StaticSequencer;

/**
 * @author zhoujiagen@gmail.com
 */
public class TestStaticSequencer {

  private static StaticSequencer staticSequencer;

  @BeforeClass
  public static void setUpBeforeClass() {
    staticSequencer = new StaticSequencer(StaticSequencer.SequenceCategory.NUMBER, 10, "0");
  }

  @Test
  public void test() {
    System.out.println(staticSequencer.current());
    System.out.println(staticSequencer.next(1));
    System.out.println(staticSequencer.current());
    System.out.println(staticSequencer.next(2));
    System.out.println(staticSequencer.current());
  }
}
