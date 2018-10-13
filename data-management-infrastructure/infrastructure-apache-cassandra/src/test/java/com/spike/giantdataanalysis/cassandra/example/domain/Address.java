package com.spike.giantdataanalysis.cassandra.example.domain;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

@UDT(keyspace = "my_keyspace", name = "address")
public class Address {

  private String street;
  private String city;
  private String state;
  @Field(name = "zip_code")
  private int zip_code;

  public Address() {
  }

  public Address(String street, String city, String state, int zip_code) {
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip_code = zip_code;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public int getZip_code() {
    return zip_code;
  }

  public void setZip_code(int zip_code) {
    this.zip_code = zip_code;
  }

  @Override
  public String toString() {
    return "Address [street=" + street + ", city=" + city + ", state=" + state + ", zip_code="
        + zip_code + "]";
  }
}
