package com.spike.giantdataanalysis.commons.guava.collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.commons.guava.collections.domain.ExampleCollectionPerson;

/**
 * <pre>
 * {@link FluentIterable}的单元测试
 * 
 * 流畅的迭代
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestFluentIterable {

  private ExampleCollectionPerson person1;
  private ExampleCollectionPerson person2;
  private ExampleCollectionPerson person3;
  private ExampleCollectionPerson person4;
  private List<ExampleCollectionPerson> personList;

  @Before
  public void setUp() {
    person1 = new ExampleCollectionPerson("Wilma", "Flintstone", 30, "F");
    person2 = new ExampleCollectionPerson("Fred", "Flintstone", 32, "M");
    person3 = new ExampleCollectionPerson("Betty", "Rubble", 31, "F");
    person4 = new ExampleCollectionPerson("Barney", "Rubble", 33, "M");

    personList = Lists.newArrayList(person1, person2, person3, person4);
  }

  @Test
  public void _filter() {
    // 过滤用谓词
    Predicate<ExampleCollectionPerson> predicate = new Predicate<ExampleCollectionPerson>() {
      @Override
      public boolean apply(ExampleCollectionPerson input) {
        return input.getAge() > 31;
      }
    };

    Iterable<ExampleCollectionPerson> personFilteredByAge =
        FluentIterable.from(personList).filter(predicate);

    System.out.println(personFilteredByAge);

    // 元素包含性
    assertThat(Iterables.contains(personFilteredByAge, person1), is(false));
    assertThat(Iterables.contains(personFilteredByAge, person2), is(true));
    assertThat(Iterables.contains(personFilteredByAge, person3), is(false));
    assertThat(Iterables.contains(personFilteredByAge, person4), is(true));
  }

  @Test
  public void _transform() {
    // 转换用函数
    Function<ExampleCollectionPerson, String> function =
        new Function<ExampleCollectionPerson, String>() {
          @Override
          public String apply(ExampleCollectionPerson input) {
            return Joiner.on("#").join(input.getLastName(), input.getFirstName(), input.getAge());
          }
        };

    List<String> transformedList = FluentIterable.from(personList).transform(function).toList();
    System.out.println(transformedList);

    // "Wilma", "Flintstone", 30, "F"
    assertThat(transformedList.get(0), is("Flintstone#Wilma#30"));
  }
}
