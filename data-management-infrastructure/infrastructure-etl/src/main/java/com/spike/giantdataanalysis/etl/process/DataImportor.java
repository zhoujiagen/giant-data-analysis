package com.spike.giantdataanalysis.etl.process;

import java.util.List;

import com.spike.giantdataanalysis.etl.exception.ETLException;

public interface DataImportor<T> {

  /**
   * 处理数据导入.
   * @param datas 数据列表
   * @return
   * @throws ETLException
   */
  boolean handle(List<T> datas) throws ETLException;

}
