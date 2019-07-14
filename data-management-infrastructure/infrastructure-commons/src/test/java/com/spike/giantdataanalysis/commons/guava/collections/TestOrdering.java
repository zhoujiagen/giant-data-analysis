package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.spike.giantdataanalysis.commons.guava.collections.domain.ExampleCollectionCity;
import com.spike.giantdataanalysis.commons.guava.collections.domain.ExampleCollectionClimate;

/**
 * <pre>
 * {@link Ordering}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestOrdering {

  private ExampleCollectionCity city1 =
      new ExampleCollectionCity(ExampleCollectionClimate.TEMPERATE, 10000L, .5);
  private ExampleCollectionCity city2 =
      new ExampleCollectionCity(ExampleCollectionClimate.TEMPERATE, 10000L, .6);
  private ExampleCollectionCity city3 =
      new ExampleCollectionCity(ExampleCollectionClimate.TEMPERATE, 2000L, .2);
  private ExampleCollectionCity city4 =
      new ExampleCollectionCity(ExampleCollectionClimate.TEMPERATE, 5000L, .4);

  private List<ExampleCollectionCity> cities;
  private List<ExampleCollectionCity> citiesWithNull;

  class CityByPopulation implements Comparator<ExampleCollectionCity> {
    @Override
    public int compare(ExampleCollectionCity city1, ExampleCollectionCity city2) {
      return Long.compare(city1.getPopulation(), city2.getPopulation());
    }
  }

  class CityByAverageRainfall implements Comparator<ExampleCollectionCity> {
    @Override
    public int compare(ExampleCollectionCity city1, ExampleCollectionCity city2) {
      return Double.compare(city1.getAverageRainfall(), city2.getAverageRainfall());
    }
  }

  @Before
  public void setUp() {
    cities = Lists.newArrayList(city1, city2, city3, city4);
    citiesWithNull = Lists.newArrayList(city1, city2, city3, city4, null);
  }

  @Test
  public void _create() {
    // 从Comparator实现上生成
    Ordering<ExampleCollectionCity> cityOrdering = Ordering.from(new CityByPopulation());

    // 获取最小的
    ExampleCollectionCity minCity = cityOrdering.min(cities);
    Assert.assertEquals(city3.getPopulation(), minCity.getPopulation());
    System.out.println(minCity);
  }

  @Test(expected = NullPointerException.class)
  public void _nullFirst() {
    Ordering<ExampleCollectionCity> cityOrdering = Ordering.from(new CityByPopulation());
    // 会抛出空指针
    ExampleCollectionCity minCity = cityOrdering.min(citiesWithNull);

    Ordering<ExampleCollectionCity> nullCityFirstOrdering =
        Ordering.from(new CityByPopulation()).nullsFirst();
    minCity = nullCityFirstOrdering.min(citiesWithNull);
    // 空优先
    Assert.assertNull(minCity);
  }

  @Test
  public void _secondarySorting() {
    Ordering<ExampleCollectionCity> compoundSorting =
        Ordering.from(new CityByPopulation()).compound(new CityByAverageRainfall());

    ExampleCollectionCity maxCity = compoundSorting.max(cities);

    Assert.assertEquals(city2.getAverageRainfall(), maxCity.getAverageRainfall(), 0.01);
  }

  @Test
  public void topAndBottom() {
    Ordering<ExampleCollectionCity> cityOrdering = Ordering.from(new CityByPopulation());
    List<ExampleCollectionCity> top2Cities = cityOrdering.greatestOf(cities, 2);
    Assert.assertEquals(city2.getPopulation(), top2Cities.get(0).getPopulation());
    Assert.assertEquals(city1.getPopulation(), top2Cities.get(1).getPopulation());

    List<ExampleCollectionCity> bottom2Cities = cityOrdering.leastOf(cities, 2);
    Assert.assertEquals(city3.getPopulation(), bottom2Cities.get(0).getPopulation());
    Assert.assertEquals(city4.getPopulation(), bottom2Cities.get(1).getPopulation());

  }

}
