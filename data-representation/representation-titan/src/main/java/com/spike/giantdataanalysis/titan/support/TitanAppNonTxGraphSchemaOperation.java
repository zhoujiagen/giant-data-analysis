package com.spike.giantdataanalysis.titan.support;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.schema.TitanManagement;

/**
 * 非事务性图Schema操作基类.
 */
public abstract class TitanAppNonTxGraphSchemaOperation extends TitanAppGraphSchemaOperation {

  @Override
  public void execute(final TitanGraph graph) throws TitanAppException {
    try {
      TitanManagement tm = graph.openManagement();
      this.operation(tm);
    } catch (Exception e) {
      throw TitanAppException.newException(e);
    }
  }
}