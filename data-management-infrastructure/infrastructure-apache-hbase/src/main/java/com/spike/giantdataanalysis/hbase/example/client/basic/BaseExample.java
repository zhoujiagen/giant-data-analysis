package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.support.HBaseAdmins;
import com.spike.giantdataanalysis.hbase.support.HBaseColumnFamilies;
import com.spike.giantdataanalysis.hbase.support.HBaseConnections;
import com.spike.giantdataanalysis.hbase.support.HBaseTables;
import com.spike.giantdataanalysis.hbase.support.HBases;

/**
 * <pre>
 * 示例支持基类
 * 
 * 使用无参数的构造器, 仅初始化连接和管理客户端;
 * 使用带参数的构造器, 额外会检查表是否存在, 如果不存在则执行创建, 同时初始化客户端.
 * </pre>
 * @author zhoujiagen
 */
public abstract class BaseExample {
  private static final Logger LOG = LoggerFactory.getLogger(BaseExample.class);

  protected Configuration conf;
  // heavy-weighted and thread-safe object
  protected Connection connection;
  protected Admin admin;
  protected Table table;

  /**
   * 初始化连接{@link Connection}和管理客户端{@link Admin}
   */
  public BaseExample() {
    try {
      conf = HBaseConfiguration.create();
      // 从类路径加载资源
      conf.addResource("conf/hbase-site.xml");
      // System.out.println("\n======");
      // conf.writeXml(System.out);
      // System.out.println("\n======");

      connection = HBaseConnections.connection(conf);
      admin = HBaseAdmins.admin(connection);
    } catch (IOException e) {
      LOG.error("初始化客户端失败", e);
    }
  }

  public BaseExample(String tableName, String... columnFamilyNames) {

    try {
      conf = HBaseConfiguration.create();
      // 从类路径加载资源
      conf.addResource("conf/hbase-site.xml");
      // System.out.println("\n======");
      // conf.writeXml(System.out);
      // System.out.println("\n======");

      connection = HBaseConnections.connection(conf);
      admin = HBaseAdmins.admin(connection);
      // 不存在则创建表
      if (HBaseTables.checkTableExists(admin, tableName)) {
        LOG.info("表{}存在", tableName);

        if (columnFamilyNames != null && columnFamilyNames.length > 0) {
          for (String cfn : columnFamilyNames) {
            if (!HBaseColumnFamilies.checkColumnFamilyExists(admin, tableName, cfn)) {
              LOG.info("在表{}中添加列族{}", tableName, cfn);
              HBaseColumnFamilies.addColumnFamily(admin, tableName, cfn);
            }
          }
        }

        table = HBaseTables.table(connection, tableName);
      } else {
        // if (columnFamilyNames == null || columnFamilyNames.length == 0) {
        // throw new IOException("列族为空");
        // }

        LOG.info("表{}不存在, 开始创建...", tableName);
        HBaseTables.createTable(admin, tableName, columnFamilyNames[0]);
        if (columnFamilyNames != null && columnFamilyNames.length != 0) {
          // 添加列族
          for (int i = 0, len = columnFamilyNames.length; i < len; i++) {
            if (!HBaseColumnFamilies
                .checkColumnFamilyExists(admin, tableName, columnFamilyNames[i])) {
              LOG.info("在表{}中添加列族{}", tableName, columnFamilyNames[i]);
              HBaseColumnFamilies.addColumnFamily(admin, tableName, columnFamilyNames[i]);
            }
          }
        }

        table = HBaseTables.table(connection, tableName);
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
   * delegate to {@link #doExecute()}, 提供事后资源清理.
   * @throws IOException
   */
  public void execute() throws IOException {

    try {
      doExecute();
    } finally {
      LOG.info("执行清理工作");
      HBases.releaseResource(connection, admin, table);
    }

  }

  protected abstract void doExecute() throws IOException;

}
