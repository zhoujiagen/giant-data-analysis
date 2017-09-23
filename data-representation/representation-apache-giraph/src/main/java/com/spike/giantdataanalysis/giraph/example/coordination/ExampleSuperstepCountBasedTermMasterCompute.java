package com.spike.giantdataanalysis.giraph.example.coordination;

import org.apache.giraph.master.DefaultMasterCompute;

/**
 * 基于超级步数量终止的MasterCompute协同实现.
 * @author zhoujiagen
 */
public class ExampleSuperstepCountBasedTermMasterCompute extends DefaultMasterCompute {
  @Override
  public void compute() {
    // 在执行了10个超级步后终止计算
    if (super.getSuperstep() == 10l) {
      super.haltComputation();
    }
  }
}
