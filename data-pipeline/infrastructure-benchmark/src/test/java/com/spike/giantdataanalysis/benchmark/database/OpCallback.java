package com.spike.giantdataanalysis.benchmark.database;

import java.sql.ResultSet;

public interface OpCallback<T> {
  void sql(String sql);

  void status(boolean status);

  T rs(ResultSet rs);

  T data();
}