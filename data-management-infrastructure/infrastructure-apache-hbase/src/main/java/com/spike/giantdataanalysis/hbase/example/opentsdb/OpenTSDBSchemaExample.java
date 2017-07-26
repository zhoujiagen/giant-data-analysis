package com.spike.giantdataanalysis.hbase.example.opentsdb;

import static com.spike.giantdataanalysis.hbase.support.MoreBytes.FACTOR_16;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.FACTOR_2;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.FACTOR_4;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.FACTOR_8;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.b0;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.b1;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.bitCopy;
import static com.spike.giantdataanalysis.hbase.support.MoreBytes.fromBitArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.hbase.example.client.basic.BaseExample;
import com.spike.giantdataanalysis.hbase.example.domain.OpenTSDBTable;

public class OpenTSDBSchemaExample extends BaseExample {

  private static final Logger LOG = LoggerFactory.getLogger(OpenTSDBSchemaExample.class);
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  // metric: UID => name
  private Map<String, String> metricUIDHex2NameMap = Maps.newHashMap();
  // tagk: UID => name
  private Map<String, String> tagkUIDHexNameMap = Maps.newHashMap();
  // tagv: UID => name
  private Map<String, String> tagvUIDHexNameMap = Maps.newHashMap();

  public static void main(String[] args) throws Exception {
    OpenTSDBSchemaExample example = new OpenTSDBSchemaExample();
    example.execute();
  }

  public OpenTSDBSchemaExample() {
  }

  @Override
  protected void doExecute() throws IOException {
    this.scan_tsdb_uid();

    this.scan_tsdb();
  }

  // 全表扫描tsdb-uid表
  void scan_tsdb_uid() throws IOException {
    Table t_tsdb = connection.getTable(TableName.valueOf(OpenTSDBTable.T_TSDB_UID));

    Scan scan = new Scan()//
        .addFamily(OpenTSDBTable.CF_ID.getBytes());

    try (ResultScanner resultScanner = t_tsdb.getScanner(scan);) {
      Iterator<Result> iter = resultScanner.iterator();
      Result result = null;
      while (iter.hasNext()) {

        result = iter.next();
        CellScanner cellScanner = result.cellScanner();
        if (cellScanner != null) {
          Cell cell = null;
          while (cellScanner.advance()) {
            cell = cellScanner.current();

            String c = Bytes.toStringBinary(CellUtil.cloneQualifier(cell));
            if (OpenTSDBTable.C_METRICS.equals(c)) {
              metricUIDHex2NameMap.put(Bytes.toHex(CellUtil.cloneValue(cell)),
                Bytes.toStringBinary(CellUtil.cloneRow(cell)));
            } else if (OpenTSDBTable.C_TAGK.equals(c)) {
              tagkUIDHexNameMap.put(Bytes.toHex(CellUtil.cloneValue(cell)),
                Bytes.toStringBinary(CellUtil.cloneRow(cell)));
            } else if (OpenTSDBTable.C_TAGV.equals(c)) {
              tagvUIDHexNameMap.put(Bytes.toHex(CellUtil.cloneValue(cell)),
                Bytes.toStringBinary(CellUtil.cloneRow(cell)));
            }
          }
        }

      }
    }

    LOG.info("\n Metric UID\n" + metricUIDHex2NameMap);
    LOG.info("\n tagk UID\n" + tagkUIDHexNameMap);
    LOG.info("\n tagv UID\n" + tagvUIDHexNameMap);
  }

  // 扫描tsdb表
  void scan_tsdb() throws IOException {

    Table t_tsdb = connection.getTable(TableName.valueOf(OpenTSDBTable.T_TSDB));

    Scan scan = new Scan()//
        .addFamily(OpenTSDBTable.CF_T.getBytes());

    // 分页过滤
    // 这里只限定页大小, 需要客户端维护起始键
    PageFilter pageFilter = new PageFilter(100);
    scan.setFilter(pageFilter);

    try (ResultScanner resultScanner = t_tsdb.getScanner(scan);) {
      Iterator<Result> iter = resultScanner.iterator();
      Result result = null;
      while (iter.hasNext()) {

        result = iter.next();
        CellScanner cellScanner = result.cellScanner();
        if (cellScanner != null) {
          while (cellScanner.advance()) {
            LOG.info("\n" + this.renderCell(cellScanner.current()));
          }
        }
      }
    }
  }

  // Cell => String
  private String renderCell(Cell cell) {
    if (cell == null) return "";

    StringBuffer sb = new StringBuffer();
    // 3B, 4B, 3B...
    byte[] row = CellUtil.cloneRow(cell);
    byte[] cf = CellUtil.cloneFamily(cell);
    // 2/4B
    byte[] c = CellUtil.cloneQualifier(cell);
    // signed number
    byte[] value = CellUtil.cloneValue(cell);

    // (1) row
    sb.append("ROW: ");
    int rowLength = row.length;
    byte[] metricuid = Bytes.copy(row, 0, 3);
    sb.append("[" + metricUIDHex2NameMap.get(Bytes.toHex(metricuid)) + "]"); // metric uid
    byte[] ts = Bytes.copy(row, 3, 4);
    // 存储的是new Date().getTime() hex的后4字节, 按秒计
    // 这里前加4个字节
    long tsInMs = Bytes.toLong(Bytes.padHead(ts, 4)) * 1000;
    sb.append("[" + sdf.format(new Date(tsInMs)) + "]"); // ts
    // tagk, tagv pairs
    int rowByteIndex = 7;
    int i = 1;
    while ((rowByteIndex + 3) <= rowLength) {
      if (i % 2 != 0) { // tagk
        sb.append("[" + tagkUIDHexNameMap.get(Bytes.toHex(Bytes.copy(row, rowByteIndex, 3))) + "]");
      } else { // tagv
        sb.append("[" + tagvUIDHexNameMap.get(Bytes.toHex(Bytes.copy(row, rowByteIndex, 3))) + "]");
      }
      rowByteIndex += 3;

      i++;
    }
    sb.append("\n");

    // (2) c
    Map<Byte, NumberType> typeMap = new HashMap<>();
    typeMap.put(b0, NumberType.INTEGER);
    typeMap.put(b1, NumberType.FLOAT);
    byte _value_1B = fromBitArray(b0, b0, b0);
    byte _value_2B = fromBitArray(b0, b1, b1);
    byte _value_4B = fromBitArray(b1, b1, b0);
    byte _value_8B = fromBitArray(b0, b0, b1);
    Map<Byte, Integer> lenMap = new HashMap<>();
    lenMap.put(_value_1B, 1);
    lenMap.put(_value_2B, 2);
    lenMap.put(_value_4B, 4);
    lenMap.put(_value_8B, 8);

    sb.append("CF: ").append(Bytes.toString(cf)).append("\n");
    int cSize = c.length;
    byte allOne4Bit = fromBitArray(b1, b1, b1, b1);
    int cIndex = 0;
    List<NumberType> valueNumberTypeList = new ArrayList<>(); // 值的类型
    List<Integer> valueSizeList = new ArrayList<>(); // 值的字节数列表
    byte first4Bit = b0;

    NumberType numberType = null;
    Integer valueSize = null;

    while (cIndex <= cSize) {
      first4Bit = bitCopy(c[cIndex], 4, 0, 4);

      if (first4Bit != allOne4Bit) {// 2B
        int _len = 2;
        if (cIndex + _len - 1 > cSize) break;

        byte _1 = c[cIndex];
        byte _2 = c[cIndex + 1];
        long deltaSeconds = bitCopy(_2, 4, 0, 4) + _1 * FACTOR_4;
        byte flag_format = bitCopy(_2, 3, 0, 1);
        byte flag_len = bitCopy(_2, 0, 0, 3);

        numberType = typeMap.get(flag_format);
        valueSize = lenMap.get(flag_len);
        valueNumberTypeList.add(numberType);
        valueSizeList.add(valueSize);
        sb.append("[S:" + deltaSeconds + "]");
        sb.append("[Type:" + numberType + "]");
        sb.append("[Len:" + valueSize + "]");

        cIndex += 2;

      } else {// 4B

        int _len = 4;
        if (cIndex + _len - 1 > cSize) break;

        byte _1 = c[cIndex];
        byte _2 = c[cIndex + 1];
        byte _3 = c[cIndex + 2];
        byte _4 = c[cIndex + 3];

        long deltaMiliSeconds = bitCopy(_4, 6, 0, 2) + _3 * FACTOR_2 //
            + _2 * (FACTOR_2 * FACTOR_8) + bitCopy(_1, 0, 0, 4) * (FACTOR_2 * FACTOR_16);
        byte flag_format = bitCopy(_4, 3, 0, 1);
        byte flag_len = bitCopy(_4, 0, 0, 3);

        numberType = typeMap.get(flag_format);
        valueSize = lenMap.get(flag_len);
        valueNumberTypeList.add(numberType);
        valueSizeList.add(valueSize);
        sb.append("[MS:" + deltaMiliSeconds + "]");
        sb.append("[Type:" + numberType + "]");
        sb.append("[Len:" + valueSize + "]");

        cIndex += 4;
      }
    }
    sb.append("\n");

    // (3) value
    sb.append("VALUE: ");
    byte[] tempValue = null;
    for (int valueIdx = 0, len = valueSizeList.size(); valueIdx < len; valueIdx++) {
      sb.append("|");
      int _valueSize = valueSizeList.get(valueIdx);
      NumberType _valueType = valueNumberTypeList.get(valueIdx);
      tempValue = Bytes.copy(value, valueIdx, _valueSize);

      if (_valueSize == 1) {// byte
        if (NumberType.INTEGER.equals(_valueType)) {
          sb.append(Bytes.toInt(tempValue));
        }
      } else if (_valueSize == 2) { // short
        if (NumberType.INTEGER.equals(_valueType)) {
          sb.append(Bytes.toShort(tempValue));
        }
      } else if (_valueSize == 4) {// int/float
        if (NumberType.INTEGER.equals(_valueType)) {
          sb.append(Bytes.toInt(tempValue));
        } else if (NumberType.FLOAT.equals(_valueType)) {
          sb.append(Bytes.toFloat(tempValue));
        }
      } else if (_valueSize == 8) {
        if (NumberType.INTEGER.equals(_valueType)) {
          sb.append(Bytes.toLong(tempValue));
        } else if (NumberType.FLOAT.equals(_valueType)) {
          sb.append(Bytes.toDouble(tempValue));
        }
      }
      sb.append("|");
    }

    return sb.toString();
  }

  /**
   * 数值类型: 整数, 浮点数
   */
  public static enum NumberType {
    INTEGER, FLOAT
  }

}
