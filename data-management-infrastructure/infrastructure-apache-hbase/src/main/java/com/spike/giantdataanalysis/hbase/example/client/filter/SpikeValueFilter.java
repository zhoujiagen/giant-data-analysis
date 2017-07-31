package com.spike.giantdataanalysis.hbase.example.client.filter;

import java.io.IOException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 自定义值过滤器
 * <p>
 * 使用: 包含在HBASE_CLASSPATH中, 重启进程.
 */
public class SpikeValueFilter extends FilterBase {

  private byte[] value = null;

  public SpikeValueFilter() {
  }

  public SpikeValueFilter(byte[] value) {
    this.value = value;
  }

  @Override
  public ReturnCode filterKeyValue(Cell v) throws IOException {
    if (Bytes.compareTo(value, CellUtil.cloneValue(v)) == 0) {
      return ReturnCode.INCLUDE;
    }
    return ReturnCode.SKIP;
  }

}
