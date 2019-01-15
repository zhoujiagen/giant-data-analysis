package com.spike.giantdataanalysis.commons.reference;

import java.io.Serializable;

/**
 * 被引用对象
 * @author zhoujiagen
 */
public class ExampleReferenceBean implements Serializable {
  private static final long serialVersionUID = 1494830518491188839L;

  private String information;

  public ExampleReferenceBean(String information) {
    super();
    this.information = information;
  }

  public String getInformation() {
    return information;
  }

  public void setInformation(String information) {
    this.information = information;
  }

  @Override
  public String toString() {
    return "Bean [information=" + information + "]";
  }

}
