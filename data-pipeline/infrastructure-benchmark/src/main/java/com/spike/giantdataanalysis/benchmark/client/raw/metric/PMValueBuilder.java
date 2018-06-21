package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import info.ganglia.gmetric4j.gmetric.GMetricType;

public class PMValueBuilder {

  private PMValue instance = new PMValue();

  /** 性能度量分组 */
  public PMValueBuilder group(String group) {
    instance.setGroup(group);
    return this;
  }

  /** 性能度量名称 */
  public PMValueBuilder metric(String metric) {
    instance.setMetric(metric);
    return this;
  }

  /** 性能度量值类型 */
  public PMValueBuilder valueType(GMetricType valueType) {
    instance.setValueType(valueType);
    return this;
  }

  /** 性能度量值 */
  public PMValueBuilder value(String value) {
    instance.setValue(value);
    return this;
  }

  /** 性能度量值的单位 */
  public PMValueBuilder unit(String unit) {
    instance.setUnit(unit);
    return this;
  }

  public PMValue build() {
    return instance;
  }

}
