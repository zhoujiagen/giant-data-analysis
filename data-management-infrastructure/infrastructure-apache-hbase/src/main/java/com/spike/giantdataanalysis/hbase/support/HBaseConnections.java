package com.spike.giantdataanalysis.hbase.support;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class HBaseConnections {
  private static final Logger LOG = LoggerFactory.getLogger(HBaseConnections.class);

  /**
   * 获取连接
   * <p>
   * Connection is heavy-weighted and thread-safe object, and should create once then use always.
   * @param config
   * @return
   * @throws IOException
   */
  public static Connection connection(Configuration config) throws IOException {
    Preconditions.checkArgument(config != null, "Argument config must not be null!");

    return ConnectionFactory.createConnection(config);
  }

  /**
   * 释放链接
   * @param connection
   * @throws IOException
   */
  public static void releaseConnection(Connection connection) throws IOException {
    if (connection == null) return;

    LOG.info("释放Connection.");
    connection.close();
  }
}
