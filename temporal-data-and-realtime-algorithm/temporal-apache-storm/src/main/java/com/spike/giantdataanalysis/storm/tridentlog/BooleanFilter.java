package com.spike.giantdataanalysis.storm.tridentlog;

import storm.trident.operation.BaseFilter;
import storm.trident.tuple.TridentTuple;

public class BooleanFilter extends BaseFilter {
  private static final long serialVersionUID = -54192826658438415L;

  @Override
  public boolean isKeep(TridentTuple tuple) {
    return tuple.getBoolean(0);
  }

}
