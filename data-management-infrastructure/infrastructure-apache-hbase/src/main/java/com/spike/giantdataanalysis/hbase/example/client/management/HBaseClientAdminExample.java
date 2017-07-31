package com.spike.giantdataanalysis.hbase.example.client.management;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;

/**
 * <pre>
 * REF: docs/book.html#hbase_apis
 * 
 * Create, modify and delete a Table Using Java
 * </pre>
 * @author zhoujiagen
 * @since 2016年10月16日 下午11:19:14
 */
public class HBaseClientAdminExample {

  private static final String TABLE_NAME = "MY_TABLE_NAME_TOO";
  private static final String CF_DEFAULT = "DEFAULT_COLUMN_FAMILY";

  public static void main(String... args) throws IOException {
    Configuration config = HBaseConfiguration.create();

    // 已在CLASSPATH:conf/目录下, 不再从系统环境中获取
    // Add any necessary configuration files (hbase-site.xml, core-site.xml)
    // config.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"));
    // config.addResource(new Path(System.getenv("HADOOP_CONF_DIR"), "core-site.xml"));

    createSchemaTables(config);
    modifySchema(config);
  }

  // 创建或覆盖表定义
  public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
    if (admin.tableExists(table.getTableName())) {
      admin.disableTable(table.getTableName());
      admin.deleteTable(table.getTableName());
    }
    admin.createTable(table);
  }

  // 从配置中创建表
  public static void createSchemaTables(Configuration config) throws IOException {
    try (Connection connection = ConnectionFactory.createConnection(config);
        Admin admin = connection.getAdmin()) {

      HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
      // Mac版不支持Algorithm.SNAPPY压缩算法 20161016
      table.addFamily(new HColumnDescriptor(CF_DEFAULT)); // .setCompressionType(Algorithm.SNAPPY)

      System.out.print("Creating table. ");
      createOrOverwrite(admin, table);
      System.out.println(" Done.");
    }
  }

  // 修改表Schema
  public static void modifySchema(Configuration config) throws IOException {
    try (Connection connection = ConnectionFactory.createConnection(config);
        Admin admin = connection.getAdmin()) {

      TableName tableName = TableName.valueOf(TABLE_NAME);
      if (admin.tableExists(tableName)) {
        System.out.println("Table does not exist.");
        System.exit(-1);
      }

      HTableDescriptor table = new HTableDescriptor(tableName);

      // Update existing table - 添加Column Family
      HColumnDescriptor newColumn = new HColumnDescriptor("NEWCF");
      newColumn.setCompactionCompressionType(Algorithm.GZ);
      newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
      admin.addColumn(tableName, newColumn);

      // Update existing column family - 更新Column Family
      HColumnDescriptor existingColumn = new HColumnDescriptor(CF_DEFAULT);
      existingColumn.setCompactionCompressionType(Algorithm.GZ);
      existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
      table.modifyFamily(existingColumn);
      admin.modifyTable(tableName, table);

      // Disable an existing table - 失效表
      admin.disableTable(tableName);

      // Delete an existing column family - 删除Column Family
      admin.deleteColumn(tableName, CF_DEFAULT.getBytes("UTF-8"));

      // Delete a table (Need to be disabled first) - 删除表
      admin.deleteTable(tableName);
    }
  }

}
