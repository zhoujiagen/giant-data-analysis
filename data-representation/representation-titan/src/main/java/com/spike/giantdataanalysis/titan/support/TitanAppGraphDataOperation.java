package com.spike.giantdataanalysis.titan.support;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;

/**
 * 事务性图数据操作基类.
 */
public abstract class TitanAppGraphDataOperation implements TitanAppGraphOperation {

  @Override
  public void execute(final TitanGraph graph) throws TitanAppException {
    try {
      TitanTransaction tx = graph.newTransaction();

      this.operation(tx);

      tx.commit();
    } catch (Exception e) {
      throw TitanAppException.newException(e);
    }
  }

  /**
   * 定义数据操作.
   * @param tx
   * @throws TitanAppException
   */
  public abstract void operation(final TitanTransaction tx) throws TitanAppException;
}