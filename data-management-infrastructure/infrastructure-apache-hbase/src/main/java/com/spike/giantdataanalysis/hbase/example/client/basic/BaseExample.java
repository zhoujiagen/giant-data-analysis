package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.support.HBases;

/**
 * 示例支持基类 TODO 如何查看Schema(HBase Shell?)
 * @author zhoujiagen
 */
public abstract class BaseExample {
  private static final Logger LOG = LoggerFactory.getLogger(BaseExample.class);

  // 应用常量
  public interface AppConstants {
    String ROWKEY_1 = "com.cnn.www";
    String ROWKEY_2 = "com.example.www";
  }

  protected Configuration conf;
  // heavy-weighted and thread-safe object
  protected Connection connection;
  protected Admin admin;
  protected Table table;

  public BaseExample(String tableName, String... columnFamilyNames) {

    try {
      Configuration conf = HBaseConfiguration.create();
      // 从类路径加载资源
      conf.addResource("conf/hbase-site.xml");

      connection = HBases.connection(conf);
      admin = HBases.admin(connection);
      // 不存在则创建表
      if (HBases.checkTableExists(admin, tableName)) {
        LOG.info("表[" + tableName + "]存在");
        table = connection.getTable(TableName.valueOf(tableName));
      } else {
        if (columnFamilyNames == null || columnFamilyNames.length == 0) {
          throw new IOException("列族为空");
        }

        LOG.info("表[" + tableName + "]不存在, 开始");
        HBases.createTable(admin, tableName, columnFamilyNames[0]);
        table = connection.getTable(TableName.valueOf(tableName));
      }

      // 添加列族
      for (int i = 1, len = columnFamilyNames.length; i < len; i++) {
        if (!HBases.checkColumnFamilyExists(admin, tableName, columnFamilyNames[i])) {
          HBases.addColumnFamily(admin, tableName, columnFamilyNames[i]);
        }
      }

      LOG.info("初始化客户端完成");

    } catch (IOException e) {
      LOG.error("初始化客户端失败", e);
    }
  }

  public Configuration getConf() {
    return conf;
  }

  public Admin getAdmin() {
    return admin;
  }

  public Connection getConnection() {
    return connection;
  }

  /**
   * delegate to {@link #doSomething()}, 提供事后资源清理.
   * @throws IOException
   */
  public void doWork() throws IOException {

    try {
      doSomething();
    } finally {
      LOG.info("执行清理工作");
      HBases.releaseResource(connection, admin, table);
    }

  }

  protected abstract void doSomething() throws IOException;

}
