package com.spike.giantdataanalysis.hbase.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class HBaseColumnFamilies {
  private static final Logger LOG = LoggerFactory.getLogger(HBaseColumnFamilies.class);

  /**
   * 添加列族, 暂不支持选项设置
   * @param admin
   * @param tableName
   * @param cfName
   * @throws IOException
   */
  public static void addColumnFamily(Admin admin, String tableName, String cfName)
      throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");
    Preconditions.checkArgument(StringUtils.isNotBlank(cfName),
      "Argument cfName must not be null or empty!");

    HColumnDescriptor columnDescriptor = new HColumnDescriptor(cfName);
    columnDescriptor.setMaxVersions(3); // 列最大版本数量
    // ADD MORE COLUMN CONFIGURATION...
    // columnDescriptor.setCompactionCompressionType(Algorithm.GZ);
    // columnDescriptor.setMaxVersions(HConstants.ALL_VERSIONS);

    LOG.info("添加列族: Table[{}], cfName=[{}]", tableName, cfName);
    admin.addColumn(TableName.valueOf(tableName), columnDescriptor);
  }

  /**
   * 修改列族
   * @param admin
   * @param tableName
   * @param cfName
   * @param latestColumnDescriptor
   * @throws IOException
   */
  public static void modifyColumnFamily(Admin admin, String tableName, String cfName,
      HColumnDescriptor latestColumnDescriptor) throws IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");
    Preconditions.checkArgument(StringUtils.isNotBlank(cfName),
      "Argument cfName must not be null or empty!");
    Preconditions.checkArgument(latestColumnDescriptor != null,
      "Argument latestTableDescriptor must not be null!");

    HBaseTables.disableTable(admin, tableName);

    LOG.info("Modify Table[" + tableName + "], Column[" + cfName + "] with parameter: "
        + latestColumnDescriptor);
    admin.modifyColumn(TableName.valueOf(tableName), latestColumnDescriptor);

    HBaseTables.enableTable(admin, tableName);
  }

  /**
   * 获取表中所有列族
   * @param admin
   * @return
   * @throws IOException
   * @throws TableNotFoundException
   */
  public static HColumnDescriptor[] columnFamilies(Admin admin, String tableName)
      throws TableNotFoundException, IOException {
    HTableDescriptor tableDescriptor = admin.getTableDescriptor(TableName.valueOf(tableName));
    return tableDescriptor.getColumnFamilies();
  }

  /**
   * 检查表中列族是否存在
   * @param admin
   * @param tableName
   * @param cfName
   * @return
   * @throws IOException
   * @see Table#exists(org.apache.hadoop.hbase.client.Get)
   */
  public static boolean checkColumnFamilyExists(Admin admin, String tableName, String cfName)
      throws IOException {
    boolean result = false;
    HTableDescriptor[] tableDescs = admin.getTableDescriptors(Arrays.asList(tableName));

    if (tableDescs != null && tableDescs.length >= 1) {
      HColumnDescriptor[] columnDescs = tableDescs[0].getColumnFamilies();
      if (columnDescs != null && columnDescs.length > 0) {
        for (HColumnDescriptor columnDesc : columnDescs) {
          if (columnDesc.getNameAsString().equals(cfName)) {
            result = true;
          }
        }
      }
    }

    LOG.info("检查表中列族是否存在: Table[{}], cfName=[{}], result={}", tableName, cfName, result);
    return result;
  }

  /**
   * 删除列族
   * @param admin
   * @param tableName
   * @param cfName
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  public static void deleteColumnFamily(Admin admin, String tableName, String cfName)
      throws UnsupportedEncodingException, IOException {
    Preconditions.checkArgument(admin != null, "Argument admin must not be null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
      "Argument tableName must not be null or empty!");
    Preconditions.checkArgument(StringUtils.isNotBlank(cfName),
      "Argument cfName must not be null or empty!");

    LOG.info("删除表[{}]中列族[{}]", tableName, cfName);
    admin.deleteColumn(TableName.valueOf(tableName), cfName.getBytes("UTF-8"));
  }
}
