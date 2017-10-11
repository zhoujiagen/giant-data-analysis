package com.spike.giantdataanalysis.titan.support;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.schema.TitanManagement;

/**
 * 事务性图Schema操作基类.
 */
public abstract class TitanAppGraphSchemaOperation implements TitanAppGraphOperation {

  @Override
  public void execute(final TitanGraph graph) throws TitanAppException {
    try {
      TitanManagement tm = graph.openManagement();

      this.operation(tm);

      tm.commit();
    } catch (Exception e) {
      throw TitanAppException.newException(e);
    }
  }

  /**
   * 定义Schema操作.
   * @param tm
   * @throws TitanAppException
   */
  public abstract void operation(final TitanManagement tm) throws TitanAppException;
}