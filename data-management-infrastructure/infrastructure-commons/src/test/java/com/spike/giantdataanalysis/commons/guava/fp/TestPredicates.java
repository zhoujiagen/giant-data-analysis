package com.spike.giantdataanalysis.commons.guava.fp;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * <pre>
 * {@link Predicates}的单元测试
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestPredicates {

  enum Climate {
    TEMPERATE, OTHER
  }

  class City {
    Climate climate;
    long population;
    double averageRainfall;

    public City(Climate climate, long population, double averageRainfall) {
      this.climate = climate;
      this.population = population;
      this.averageRainfall = averageRainfall;
    }

    public Climate getClimate() {
      return climate;
    }

    public void setClimate(Climate climate) {
      this.climate = climate;
    }

    public long getPopulation() {
      return population;
    }

    public void setPopulation(long population) {
      this.population = population;
    }

    public double getAverageRainfall() {
      return averageRainfall;
    }

    public void setAverageRainfall(double averageRainfall) {
      this.averageRainfall = averageRainfall;
    }

  }

  /**
   * <pre>
   * 一个谓词实现
   * </pre>
   *
   * @author zhoujiagen
   */
  class SmallPolulationPredicate implements Predicate<City> {
    @Override
    public boolean apply(City input) {
      // 小于一万人
      return input.getPopulation() < 10000L;
    }

    @Override
    public String toString() {
      return "(City.getPopulation() < 10000L)";
    }
  }

  class TemperateClimatePredicate implements Predicate<City> {
    @Override
    public boolean apply(City input) {
      return Climate.TEMPERATE.equals(input.getClimate());
    }

    @Override
    public String toString() {
      return "(Climate.TEMPERATE.equals(City.getClimate()))";
    }
  }

  class LowRainfallPredicate implements Predicate<City> {
    @Override
    public boolean apply(City input) {
      return input.getAverageRainfall() < 45.7D;
    }

    @Override
    public String toString() {
      return "(City.getAverageRainfall() < 45.7D)";
    }
  }

  SmallPolulationPredicate smallPolulationPredicate = new SmallPolulationPredicate();
  TemperateClimatePredicate temperateClimatePredicate = new TemperateClimatePredicate();
  LowRainfallPredicate lowRainfallPredicate = new LowRainfallPredicate();

  @Test
  public void _and() {
    Predicate<City> smallAndDry = Predicates.and(smallPolulationPredicate, lowRainfallPredicate);
    System.out.println(smallAndDry);
  }

  @Test
  public void _or() {
    Predicate<City> smallOrDry = Predicates.or(smallPolulationPredicate, lowRainfallPredicate);
    System.out.println(smallOrDry);
  }

  @Test
  public void _not() {
    Predicate<City> notSmall = Predicates.not(smallPolulationPredicate);
    System.out.println(notSmall);
  }

  @Test
  public void complexPredicates() {

    // ((not small) and dry) or lowRainfall
    Predicate<City> notSmallButDryOrLowRainfall = //
        Predicates.or(//
          Predicates.and(//
            Predicates.not(smallPolulationPredicate), //
            temperateClimatePredicate), //
          lowRainfallPredicate//
        );

    System.out.println(notSmallButDryOrLowRainfall);
  }

  @Test
  public void _compose() {

    Predicate<City> predicate = smallPolulationPredicate;

    Map<String, City> map = new HashMap<String, City>();
    map.put("a", new City(Climate.OTHER, 20000L, 56.7));
    map.put("b", new City(Climate.TEMPERATE, 5000L, 56.7));
    map.put("c", new City(Climate.OTHER, 25000L, 56.7));
    Function<String, City> function = Functions.forMap(map);

    // 应用顺序：function -> predicate
    Predicate<String> result = Predicates.compose(predicate, function);
    System.out.println(result);

    // 应用谓词
    boolean applyResult = result.apply("a");
    Assert.assertFalse(applyResult);

    applyResult = result.apply("b");
    Assert.assertTrue(applyResult);
  }
}
