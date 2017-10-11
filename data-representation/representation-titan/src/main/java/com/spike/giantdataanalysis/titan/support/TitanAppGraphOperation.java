package com.spike.giantdataanalysis.titan.support;

import com.thinkaurelius.titan.core.TitanGraph;

/**
 * 图操作, 可以是Schema或数据等.
 */
public interface TitanAppGraphOperation {
  void execute(final TitanGraph graph) throws TitanAppException;
}