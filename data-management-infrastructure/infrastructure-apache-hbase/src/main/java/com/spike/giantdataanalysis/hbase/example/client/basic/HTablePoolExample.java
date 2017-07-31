package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.util.Bytes;

import com.spike.giantdataanalysis.hbase.example.domain.TestTable;

/**
 * 已废弃的{@link HTablePool}的示例.
 */
@SuppressWarnings("deprecation")
public class HTablePoolExample {
  public static void main(String[] args) throws IOException {
    Configuration conf = HBaseConfiguration.create();
    // 从类路径加载资源
    conf.addResource("conf/hbase-site.xml");
    // System.out.println("\n======");
    // conf.writeXml(System.out);
    // System.out.println("\n======");

    HTablePool pool = new HTablePool(conf, 5);

    String tableName = TestTable.T_NAME;
    HTableInterface table = pool.getTable(tableName);
    System.out.println(Bytes.toString(table.getTableName()));

    pool.closeTablePool(tableName);

    pool.close();
  }
}
