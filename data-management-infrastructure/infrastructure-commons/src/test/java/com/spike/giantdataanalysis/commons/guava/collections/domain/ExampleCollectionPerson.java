package com.spike.giantdataanalysis.commons.guava.collections.domain;

import com.google.common.base.MoreObjects;

public class ExampleCollectionPerson {
  private String firstName;
  private String lastName;
  private int age;
  private String sex;

  public ExampleCollectionPerson() {
  }

  public ExampleCollectionPerson(String firstName, String lastName, int age, String sex) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.sex = sex;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)//
        .omitNullValues()//
        .add("姓名", firstName + " " + lastName)//
        .add("年龄", age)//
        .add("性别", sex)//
        .toString();
  }

}
