package com.spike.giantdataanalysis.commons.guava.fp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.google.common.base.Function;

public class TestDateFormatFunction {

  @Test
  public void test() {
    Date input = new Date();
    String result = new ExampleDateFormatFunction().apply(input);

    System.out.println(result);
  }

  @Test
  public void useAnoymousClass() {
    Function<Date, String> function = new Function<Date, String>() {
      @Override
      public String apply(Date input) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(input);
      }
    };

    Date input = new Date();

    String result = function.apply(input);

    System.out.println(result);
  }

}
