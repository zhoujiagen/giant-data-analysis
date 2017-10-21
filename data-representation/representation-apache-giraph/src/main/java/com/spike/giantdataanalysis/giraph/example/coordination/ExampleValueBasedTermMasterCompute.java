package com.spike.giantdataanalysis.giraph.example.coordination;

import org.apache.giraph.master.DefaultMasterCompute;
import org.apache.hadoop.io.DoubleWritable;

/**
 * 基于聚合器值终止的MasterCompute协同实现.
 * @author zhoujiagen
 */
public class ExampleValueBasedTermMasterCompute extends DefaultMasterCompute {
  /** 总误差聚合器名称 */
  public static final String TOTAL_ERROR_AGGREGATOR_NAME = "error.total";
  /** 平均误差聚合器名称 */
  public static final String AVG_ERROR_AGGREGATOR_NAME = "error.avg";
  /** 终止标记值 */
  public static final double TERM_VALUE = 0.001d;

  @Override
  public void compute() {
    // 设置平均无擦汗聚合器的值
    double totalError =
        super.<DoubleWritable> getAggregatedValue(TOTAL_ERROR_AGGREGATOR_NAME).get();
    super.setAggregatedValue(AVG_ERROR_AGGREGATOR_NAME,
      new DoubleWritable(totalError / super.getTotalNumVertices()));

    // 聚合器的值小于该值时终止计算
    if (totalError < TERM_VALUE) {
      super.haltComputation();
    }
  }
}
