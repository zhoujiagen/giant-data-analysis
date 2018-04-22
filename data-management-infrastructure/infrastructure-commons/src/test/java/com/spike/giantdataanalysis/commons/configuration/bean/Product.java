package com.spike.giantdataanalysis.commons.configuration.bean;

public class Product {
  public String sku;
  public Integer quantity;
  public String description;
  public Float price;

  @Override
  public String toString() {
    return "Product [sku=" + sku + ", quantity=" + quantity + ", description=" + description
        + ", price=" + price + "]";
  }

}
