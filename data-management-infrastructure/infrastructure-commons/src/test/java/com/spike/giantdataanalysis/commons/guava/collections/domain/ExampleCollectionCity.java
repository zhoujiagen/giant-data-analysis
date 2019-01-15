package com.spike.giantdataanalysis.commons.guava.collections.domain;

public class ExampleCollectionCity {
  private ExampleCollectionClimate climate;
  private long population;
  private double averageRainfall;

  public ExampleCollectionCity(ExampleCollectionClimate climate, long population,
      double averageRainfall) {
    this.climate = climate;
    this.population = population;
    this.averageRainfall = averageRainfall;
  }

  public ExampleCollectionClimate getClimate() {
    return climate;
  }

  public void setClimate(ExampleCollectionClimate climate) {
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

  @Override
  public String toString() {
    return "City [climate=" + climate + ", population=" + population + ", averageRainfall="
        + averageRainfall + "]";
  }

}