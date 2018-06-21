package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import info.ganglia.gmetric4j.gmetric.GMetricType;

/** 性能度量值 */
public class PMValue {

  /** 性能度量分组 */
  private String group;
  /** 性能度量名称 */
  private String metric;
  /** 性能度量值类型 */
  private GMetricType valueType;
  /** 性能度量值 */
  private String value;
  /** 性能度量值的单位 */
  private String unit = "";

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getMetric() {
    return metric;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public GMetricType getValueType() {
    return valueType;
  }

  public void setValueType(GMetricType valueType) {
    this.valueType = valueType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public String toString() {
    return "PMValue [group=" + group + ", metric=" + metric + ", valueType=" + valueType
        + ", value=" + value + ", unit=" + unit + "]";
  }
}
