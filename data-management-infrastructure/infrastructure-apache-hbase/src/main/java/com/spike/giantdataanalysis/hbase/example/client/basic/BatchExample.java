package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.coprocessor.Batch.Callback;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.spike.giantdataanalysis.hbase.example.domain.TestTable.*;

import com.google.common.base.Strings;

/**
 * <pre>
 * 批量操作示例
 * 
 * 可以实现跨行的不同操作; 
 * 实际执行顺序无法预测, 因此不可以将对同一行的Put和Delete操作放在同一批量操作中;
 * 检测结果之前, 所有的操作都被执行, 即未产生异常的操作都已被执行.
 * </pre>
 */
public class BatchExample extends BaseExample {

  public static final Logger LOG = LoggerFactory.getLogger(BatchExample.class);

  public BatchExample(String tableName, String... cfs) {
    super(tableName, cfs);
  }

  public static void main(String[] args) throws IOException {
    BatchExample example = new BatchExample(//
        T_NAME, Bytes.toString(CF_1), Bytes.toString(CF_2));
    example.execute();
  }

  @Override
  public void doExecute() throws IOException {
    List<Row> batch = new ArrayList<>();

    Put put = new Put(ROW_2);
    put.addColumn(CF_2, C_1, V_5);
    batch.add(put);

    Get get1 = new Get(ROW_1);
    get1.addColumn(CF_1, C_1);
    batch.add(get1);

    Delete delete = new Delete(ROW_1);
    delete.addColumn(CF_1, C_2);
    batch.add(delete);

    Get get2 = new Get(ROW_1);
    get2.addFamily(Bytes.toBytes("NON-EXISTED")); // 列族不存在
    batch.add(get2);

    Object[] results = new Object[batch.size()];
    Callback<Object> callback = new Callback<Object>() {
      @Override
      public void update(byte[] region, byte[] row, Object result) {
        System.out.println(Strings.repeat("=", 20));
        System.out.println(Bytes.toString(region));
        System.out.println(Bytes.toString(row));
        System.out.println(result);
        System.out.println(Strings.repeat("=", 20));
      }
    };
    try {
      table.batchCallback(batch, results, callback);
    } catch (InterruptedException e) {
      LOG.error("batch failed", e);
    }

    for (int i = 0, len = results.length; i < len; i++) {
      System.out.println("result[" + i + "]=" + results[i]);
    }
  }
}
