package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.spike.giantdataanalysis.commons.guava.collections.domain.ExampleCollectionBook;

/**
 * <pre>
 * {@link Maps}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestMaps {

  List<ExampleCollectionBook> books;

  @Before
  public void setUp() {
    books = Lists.newArrayList();

    ExampleCollectionBook book =
        new ExampleCollectionBook(null, "title1", "publisher1", "isbn1", 0.1D);
    books.add(book);
    book = new ExampleCollectionBook(null, "title2", "publisher2", "isbn2", 0.2D);
    books.add(book);
    book = new ExampleCollectionBook(null, "title3", "publisher3", "isbn3", 0.3D);
    books.add(book);
    book = new ExampleCollectionBook(null, "title4", "publisher4", "isbn4", 0.4D);
    books.add(book);
  }

  /**
   * <pre>
   * 将Book列表转换为key=isbn, value=Book的Map
   * </pre>
   */
  @Test
  public void _uniqueIndex() {

    // Map的键生成函数
    Function<ExampleCollectionBook, String> keyFunction =
        new Function<ExampleCollectionBook, String>() {
          @Override
          public String apply(ExampleCollectionBook input) {
            return input.getIsbn();
          }
        };

    Map<String, ExampleCollectionBook> result = Maps.uniqueIndex(books, keyFunction);

    Assert.assertThat(result.size(), Is.is(4));
    System.out.println(result);
  }

  /**
   * <pre>
   *  由Book列表转换为key=isbn, value=Book的Map
   * </pre>
   */
  @Test
  public void _asMap() {
    Function<ExampleCollectionBook, String> transformFunction =
        new Function<ExampleCollectionBook, String>() {
          @Override
          public String apply(ExampleCollectionBook input) {
            return input.getIsbn();
          }
        };
    Set<String> isbnSet = FluentIterable.from(books).transform(transformFunction).toSet();

    Function<String, ExampleCollectionBook> function =
        new Function<String, ExampleCollectionBook>() {
          @Override
          public ExampleCollectionBook apply(final String isbn) {
            return FluentIterable.from(books).filter(new Predicate<ExampleCollectionBook>() {
              @Override
              public boolean apply(ExampleCollectionBook input) {
                return input.getIsbn().equals(isbn);
              }
            }).first().get();
          }
        };

    Map<String, ExampleCollectionBook> result = Maps.asMap(isbnSet, function);

    System.out.println(result);
  }

  @Test
  public void _toMap() {
    List<String> keys = Lists.newArrayList("1", "2", "3");
    Function<String, Integer> valueFunction = new Function<String, Integer>() {
      @Override
      public Integer apply(String input) {
        return Integer.parseInt(input) + 1;
      }
    };

    ImmutableMap<String, Integer> result = Maps.toMap(keys, valueFunction);

    System.out.println(result);
  }

  @Test
  public void _transformFromEntries() {
    Function<ExampleCollectionBook, String> keyFunction =
        new Function<ExampleCollectionBook, String>() {
          @Override
          public String apply(ExampleCollectionBook input) {
            return input.getIsbn();
          }
        };

    Map<String, ExampleCollectionBook> isbnBookMap = Maps.uniqueIndex(books, keyFunction);
    System.out.println(isbnBookMap);

    EntryTransformer<String, ExampleCollectionBook, ExampleCollectionBook> transformer =
        new EntryTransformer<String, ExampleCollectionBook, ExampleCollectionBook>() {

          @Override
          public ExampleCollectionBook transformEntry(String key, ExampleCollectionBook value) {
            ExampleCollectionBook result = value;
            result.setPrice(value.getPrice() + 1D); // 涨价
            return result;
          }
        };

    Map<String, ExampleCollectionBook> result = Maps.transformEntries(isbnBookMap, transformer);
    System.out.println(result);
  }

}
