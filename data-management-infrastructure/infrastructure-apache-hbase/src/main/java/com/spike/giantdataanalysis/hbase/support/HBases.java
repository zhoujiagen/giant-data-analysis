package com.spike.giantdataanalysis.hbase.support;

import java.io.IOException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.common.base.Strings;

public class HBases {

  // private static final Logger LOG = LoggerFactory.getLogger(HBases.class);

  /**
   * 释放资源
   * @param connection
   * @param admin
   * @param table
   * @throws IOException
   */
  public static void releaseResource(Connection connection, Admin admin, Table table)
      throws IOException {
    HBaseTables.releaseTable(table);
    HBaseAdmins.releaseAdmin(admin);
    HBaseConnections.releaseConnection(connection);
  }

  public static String asString(Result result, boolean pretty) throws IOException {

    StringBuffer sb = new StringBuffer();

    sb.append("\n");
    sb.append("==============================================").append("\n");
    if (result == null) {
      sb.append("Result is empty.").append("\n");
    } else {

      // List<Cell> cells = result.listCells();
      // Cell[] kvs = result.rawCells();
      CellScanner cellScanner = result.cellScanner();
      if (cellScanner != null) {
        while (cellScanner.advance()) {
          sb.append(asString(cellScanner.current(), pretty)).append("\n");
        }
      }
    }

    sb.append("==============================================");

    return sb.toString();
  }

  private static String asString(Cell cell, boolean pretty) {
    StringBuffer sb = new StringBuffer();

    sb.append(Strings.repeat("=", 6) + "\n");
    sb.append("ROW=").append(render(CellUtil.cloneRow(cell), pretty));
    sb.append("\n");
    sb.append("CF=").append(render(CellUtil.cloneFamily(cell), pretty));
    sb.append("\n");
    sb.append("C=").append(render(CellUtil.cloneQualifier(cell), pretty));
    sb.append("\n");
    sb.append("VALUE=").append(render(CellUtil.cloneValue(cell), pretty));
    sb.append("\n" + Strings.repeat("=", 6) + "\n");
    return sb.toString();
  }

  private static String render(byte[] bytes, boolean pretty) {
    if (bytes == null) return "";

    int maxSize = 30;
    int actutalSize = bytes.length;
    if (actutalSize > maxSize) {
      if (pretty) {
        return Bytes.toStringBinary(bytes);
      } else {
        return new String(Bytes.toHex(bytes, 0, maxSize)) + ", len=" + actutalSize;
      }

    } else {
      if (pretty) {
        return Bytes.toStringBinary(bytes);
      } else {
        return Bytes.toHex(bytes);
      }
    }
  }

}
