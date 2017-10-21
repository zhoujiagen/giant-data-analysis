package com.spike.giantdataanalysis.etl.process;

/**
 * 记录行解析器
 * @param <T> 记录实体类型
 */
public interface LineParser<T> {

  /**
   * 解析行记录
   * @param line
   * @param sep 记录分隔符
   * @return
   */
  T parse(String line, String sep);
}
