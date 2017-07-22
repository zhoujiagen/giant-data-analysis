package com.spike.giantdataanalysis.hbase.support;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class HBaseTables {
  private static final Logger LOG = LoggerFactory.getLogger(HBaseTables.class);

  /**
   * 获取表{@link Table}, 可转型为{@link HTable}.
   * @param connection
   * @param tableName
   * @return
   * @throws IOException
   * @see {@link Connection#getTable(TableName)}
   * @see {@link Connection#getBufferedMutator(TableName)}
   * @see {@link Connection#getRegionLocator(TableName)}
   */
  public static Table table(Connection connection, String tableName) throws IOException {
    Preconditions.checkArgument(connection != null, "Argument connection must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    return connection.getTable(TableName.valueOf(tableName));
  }

  /**
   * 获取{@link HTable}实例. 不建议使用.
   * @param conf
   * @param tableName
   * @return
   * @throws IOException
   */
  @SuppressWarnings("deprecation")
  public static HTable htable(Configuration conf, String tableName) throws IOException {
    return new HTable(conf, tableName);
  }

  /**
   * 释放表
   * @param table
   * @throws IOException
   */
  public static void releaseTable(Table table) throws IOException {
    if (table == null) return;

    LOG.info("释放Table.");
    table.close();
  }

  /**
   * 创建表
   * <p>
   * Table should have at least one column family.
   * <p>
   * Set hbase.table.sanity.checks to false at conf or table descriptor if you want to bypass sanity
   * checks.
   * @param admin
   * @param tableName
   * @param cfName
   * @throws IOException
   */
  public static void createTable(Admin admin, String tableName, String cfName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    Preconditions.checkArgument(!checkTableExists(admin, tableName), "表[" + tableName + "]已存在!");
    Preconditions.checkArgument(
      !HBaseColumnFamilies.checkColumnFamilyExists(admin, tableName, cfName), //
      "表[" + tableName + "]中列族[" + cfName + "]已存在!");

    HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
    HColumnDescriptor columnDescriptor = new HColumnDescriptor(cfName);
    columnDescriptor.setMaxVersions(3);// 默认最大版本
    tableDescriptor.addFamily(columnDescriptor);
    // ADD MORE COLUMN CONFIGURATION...

    forceCreateTable(admin, tableDescriptor);
  }

  /**
   * 检查表是否存在
   * @param admin
   * @param tableName
   * @return
   * @throws IOException
   */
  public static boolean checkTableExists(Admin admin, String tableName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    return admin.tableExists(TableName.valueOf(tableName));
  }

  /**
   * 获取表描述符
   * @param admin
   * @param tableName
   * @return
   * @throws IOException
   */
  public static HTableDescriptor tableDescriptor(Admin admin, String tableName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    return admin.getTableDescriptor(TableName.valueOf(tableName));
  }

  /**
   * 强制创建表, 类内部用
   * @param admin
   * @param tableDescriptor
   * @throws IOException
   */
  private static void forceCreateTable(Admin admin, HTableDescriptor tableDescriptor)
      throws IOException {
    // 存在则删除
    if (admin.tableExists(tableDescriptor.getTableName())) {
      admin.disableTable(tableDescriptor.getTableName());
      admin.deleteTable(tableDescriptor.getTableName());
    }

    // 执行创建
    admin.createTable(tableDescriptor);
  }

  /**
   * 失效表
   * @param admin
   * @param tableName
   * @throws IOException
   */
  public static void disableTable(Admin admin, String tableName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    LOG.info("失效表[{}]", tableName);
    admin.disableTable(TableName.valueOf(tableName));
  }

  /**
   * 激活表
   * @param admin
   * @param tableName
   * @throws IOException
   */
  public static void enableTable(Admin admin, String tableName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    LOG.info("激活表[{}]", tableName);
    admin.enableTable(TableName.valueOf(tableName));
  }

  /**
   * 删除表
   * @param admin
   * @param tableName
   * @throws IOException
   */
  public static void deleteTable(Admin admin, String tableName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    LOG.info("删除表[{}]", tableName);
    admin.deleteTable(TableName.valueOf(tableName));
  }

  /**
   * 强制删除表
   * @param admin
   * @param tableName
   * @throws IOException
   */
  public static void forceDeleteTable(Admin admin, String tableName) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");

    LOG.info("失效表[{}]", tableName);
    admin.disableTable(TableName.valueOf(tableName));
    LOG.info("删除表[{}]", tableName);
    admin.deleteTable(TableName.valueOf(tableName));
  }

  /**
   * 修改表
   * @param admin
   * @param tableName
   * @param latestTableDescriptor
   * @throws IOException
   */
  public static void modifyTable(Admin admin, String tableName,
      HTableDescriptor latestTableDescriptor) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");
    Preconditions.checkArgument(latestTableDescriptor != null,
      "Argument latestTableDescriptor must not be null!");

    LOG.info("Modify Table[" + tableName + "] with parameter: " + latestTableDescriptor);
    admin.modifyTable(TableName.valueOf(tableName), latestTableDescriptor);
  }
}
