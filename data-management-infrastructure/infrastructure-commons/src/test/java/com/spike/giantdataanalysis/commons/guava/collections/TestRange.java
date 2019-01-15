package com.spike.giantdataanalysis.commons.guava.collections;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Range;
import com.spike.giantdataanalysis.commons.guava.collections.domain.ExampleCollectionPerson;

/**
 * <pre>
 * {@link Range}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestRange {

  @Test
  public void _create() {
    Range<Integer> range = Range.closed(1, 10);
    Assert.assertTrue(range.contains(1));
    Assert.assertTrue(range.contains(10));

    range = Range.openClosed(1, 10);
    Assert.assertFalse(range.contains(1));
    Assert.assertTrue(range.contains(10));

    range = Range.open(1, 10);
    Assert.assertFalse(range.contains(1));
    Assert.assertFalse(range.contains(10));
  }

  @Test
  public void rangeWithComparableClass() {
    Range<Integer> personAgeRange = Range.closed(35, 50);

    // Range is a Predicate
    Function<ExampleCollectionPerson, Integer> personToAgefunction =
        new Function<ExampleCollectionPerson, Integer>() {
          @Override
          public Integer apply(ExampleCollectionPerson input) {
            return input.getAge();
          }
        };
    Predicate<ExampleCollectionPerson> predicate =
        Predicates.compose(personAgeRange, personToAgefunction);

    ExampleCollectionPerson input = new ExampleCollectionPerson("firstName", "firstName", 36, "M");
    Assert.assertTrue(predicate.apply(input));

    input.setAge(34);
    Assert.assertFalse(predicate.apply(input));
  }

}
