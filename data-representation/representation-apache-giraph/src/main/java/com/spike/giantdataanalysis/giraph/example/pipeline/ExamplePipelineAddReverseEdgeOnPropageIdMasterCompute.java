package com.spike.giantdataanalysis.giraph.example.pipeline;

import org.apache.giraph.master.DefaultMasterCompute;

public class ExamplePipelineAddReverseEdgeOnPropageIdMasterCompute extends DefaultMasterCompute {

  @Override
  public void compute() {
    // 按超级步序号选择计算类
    if (super.getSuperstep() == 0l) {
      super.setComputation(ExamplePipelineProgateId.class);
    } else {
      super.setComputation(ExamplePipelineAddReverseEdge.class);
    }
  }
}
