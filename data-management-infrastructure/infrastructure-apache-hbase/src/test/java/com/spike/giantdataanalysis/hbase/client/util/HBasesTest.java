package com.spike.giantdataanalysis.hbase.client.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.Test;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;
import com.spike.giantdataanalysis.hbase.support.HBases;

/**
 * {@link HBases}的单元测试
 * @author zhoujiagen
 */
public class HBasesTest {

  /**
   * 测试通过{@link Admin}创建的表列族中Cell的多版本问题
   * @throws IOException
   */
  @Test
  public void createTable() throws IOException {
    Configuration conf = HBases.loadDefaultConfiguration();
    Connection connection = HBases.connection(conf);
    Admin admin = HBases.admin(connection);

    HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(WebTable.TABLE_NAME));
    HColumnDescriptor columnDescriptor = new HColumnDescriptor(WebTable.CF_ANCHOR);
    // 设置minVersion时,必须设置TTL
    // columnDescriptor.setMinVersions(3);// 最小版本数量, 默认为0
    // columnDescriptor.setTimeToLive(HConstants.WEEK_IN_SECONDS);
    columnDescriptor.setMaxVersions(3); // 最大版本数量

    tableDescriptor.addFamily(columnDescriptor);
    admin.createTable(tableDescriptor);

    HBases.releaseConnection(connection);
    HBases.releaseAdmin(admin);
  }

}
