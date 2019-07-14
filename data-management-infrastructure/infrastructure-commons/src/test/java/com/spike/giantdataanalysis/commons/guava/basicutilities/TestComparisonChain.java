package com.spike.giantdataanalysis.commons.guava.basicutilities;

import com.google.common.collect.ComparisonChain;

/**
 * <pre>
 * {@link ComparisonChain} 用于帮助实现compareTo方式
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestComparisonChain {

  class Person {
  }

  class Book implements Comparable<Book> {

    Person author;
    String title;
    String publisher;
    String isbn;
    double price;

    @Override
    public int compareTo(Book o) {
      return ComparisonChain//
          .start()//
          .compare(title, o.title)//
          .compare(publisher, o.publisher)//
          .result();
    }
  }
}
