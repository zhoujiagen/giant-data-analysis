package com.spike.giantdataanalysis.commons.guava.basicutilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * <pre>
 * {@link Objects}已被废弃(除hashCode)，使用{@link MoreObjects}
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestObjects {
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
      return 0;
    }

    @Override
    public String toString() {
      return MoreObjects//
          .toStringHelper(this)//
          .omitNullValues()//
          .add("", author)//
          .add("", publisher)//
          .add("isbn", isbn)//
          .add("", price)// primitive不为null
          .add("标题", title)//
          .toString();
    }

  }

  @Test
  public void toStringHelper() {
    Book book = new Book();
    book.title = "What is rational is real.";

    System.out.println(book.toString());
  }

  @Test
  public void _firstNonNull() {
    String a = null;
    String result = MoreObjects.firstNonNull(a, "b");

    assertEquals("b", result);
    System.out.println(result);
  }

  /**
   * <pre>
   * run 1:
   * 1163850873
   * 1560044649
   * 
   * run 2:
   * 202045549
   * 607375150
   * 
   * 使用{@link }
   * 
   * 单元素数组使用元素的实例标识
   * </pre>
   *
   * @see java.util.Arrays#hashCode(Object[])
   */
  @Test
  public void _hashCode() {
    Book book = new Book();
    book.title = "What is rational is real.";
    int result = Objects.hashCode(book);
    System.out.println(result);

    Person person = new Person();
    result = Objects.hashCode(person);
    System.out.println(result);
  }

}
