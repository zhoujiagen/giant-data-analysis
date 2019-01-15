package com.spike.giantdataanalysis.commons.guava.fp;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;

/**
 * <pre>
 * {@link Functions}的单元测试f
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestFunctions {
  class City {
    private String name;
    private String zipCode;
    private int population;

    public City(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getZipCode() {
      return zipCode;
    }

    public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
    }

    public int getPopulation() {
      return population;
    }

    public void setPopulation(int population) {
      this.population = population;
    }

  }

  class State {
    private String name;
    private String code;
    private Set<City> mainCities = new HashSet<City>();

    public State() {
    }

    public State(String name, String code) {
      super();
      this.name = name;
      this.code = code;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public Set<City> getMainCities() {
      return mainCities;
    }

    public void setMainCities(Set<City> mainCities) {
      this.mainCities = mainCities;
    }

  }

  /**
   * <pre>
   * Map查找
   * </pre>
   */
  @Test
  public void forMap() {

    Map<String, State> map = new HashMap<String, State>();
    map.put("a", new State("a", "aa"));
    map.put("b", new State("b", "bb"));
    map.put("c", new State("c", "cc"));

    Function<String, State> function = Functions.forMap(map);

    State state = function.apply("a");
    Assert.assertEquals("aa", state.getCode());
  }

  /**
   * <pre>
   * 函数组合
   * </pre>
   */
  @Test
  public void compose() {
    // 在Map中每个entry上应用的函数
    class StateToCityString implements Function<State, String> {
      @Override
      public String apply(State input) {
        return Joiner.on(",").join(input.getMainCities());
      }
    }

    Map<String, State> map = new HashMap<String, State>();
    State state = new State("a", "aa");
    Set<City> cities = new HashSet<City>();
    cities.add(new City("a1"));
    cities.add(new City("a2"));
    state.setMainCities(cities);
    map.put("a", state);

    state = new State("b", "bb");
    cities = new HashSet<City>();
    cities.add(new City("b1"));
    cities.add(new City("b2"));
    cities.add(new City("b3"));
    state.setMainCities(cities);
    map.put("b", state);

    state = new State("c", "cc");
    cities = new HashSet<City>();
    cities.add(new City("c1"));
    state.setMainCities(cities);
    map.put("c", state);

    Function<String, State> function = Functions.forMap(map);

    StateToCityString stateToCityFunc = new StateToCityString();

    // 函数应用的顺序: function -> stateToCityFunc
    Function<String, String> composedFunc = Functions.compose(stateToCityFunc, function);
    String result = composedFunc.apply("a");

    assertEquals("a1,a2", result);
    System.out.println(result);
  }

}
