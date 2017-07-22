package com.spike.giantdataanalysis.hbase.support;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.RegionLoad;
import org.apache.hadoop.hbase.ServerLoad;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class HBaseAdmins {
  private static final Logger LOG = LoggerFactory.getLogger(HBaseAdmins.class);

  /**
   * 获取管理客户端
   * @param connection
   * @return
   * @throws IOException
   */
  public static Admin admin(Connection connection) throws IOException {
    Preconditions.checkArgument(connection != null, "Argument connection must not be null!");

    return connection.getAdmin();
  }

  /**
   * 释放管理客户端
   * @param admin
   * @throws IOException
   */
  public static void releaseAdmin(Admin admin) throws IOException {
    if (admin == null) return;

    LOG.info("释放Admin.");
    admin.close();
  }

  /**
   * 显示集群状态信息
   * @param status
   * @return
   */
  public static String asString(ClusterStatus status) {
    Preconditions.checkArgument(status != null, "Argument status should not be null!");

    StringBuffer sb = new StringBuffer();

    sb.append(Strings.repeat("=", 50)).append("\n");
    sb.append("Servers: ").append(status.getServers()).append("\n");
    sb.append("Dead Servers: ").append(status.getDeadServerNames()).append("\n");
    sb.append("Region Cnt: ").append(status.getRegionsCount()).append("\n");
    sb.append("\n");

    for (ServerName serverName : status.getServers()) {
      // RegionServer信息
      sb.append(Strings.repeat("-", 50)).append("\n");
      sb.append("RegionServer: ").append(serverName.getServerName()).append("\n");
      sb.append(Strings.repeat("-", 50)).append("\n");
      sb.append("Host: ").append(serverName.getHostAndPort()).append("\n");
      sb.append("Port: ").append(serverName.getPort()).append("\n");
      sb.append("\n");

      ServerLoad serverLoad = status.getLoad(serverName);
      sb.append("Region Cnt: ").append(serverLoad.getNumberOfRegions()).append("\n");
      sb.append("Max Heap(MB): ").append(serverLoad.getMaxHeapMB()).append("\n");
      sb.append("Used Heap(MB): ").append(serverLoad.getUsedHeapMB()).append("\n");
      sb.append("Request Cnt: ").append(serverLoad.getNumberOfRequests()).append("\n");
      sb.append("Request Rate(S): ").append(serverLoad.getRequestsPerSecond()).append("\n");
      sb.append("StoreFile Cnt: ").append(serverLoad.getStorefiles()).append("\n");
      sb.append("StoreFile(MB): ").append(serverLoad.getStorefileSizeInMB()).append("\n");
      sb.append("StoreFile Index(MB): ").append(serverLoad.getStorefileIndexSizeInMB())
          .append("\n");
      sb.append("\n");

      // RegionServer中Region信息
      Map<byte[], RegionLoad> regionNameLoadMap = serverLoad.getRegionsLoad();
      for (byte[] key : regionNameLoadMap.keySet()) {
        RegionLoad regionLoad = regionNameLoadMap.get(key);

        sb.append(Strings.repeat(".", 40)).append("\n");
        sb.append("Region: ").append(Bytes.toStringBinary(key)).append("\n");
        sb.append(Strings.repeat(".", 40)).append("\n");
        sb.append("Store Cnt: ").append(regionLoad.getStores()).append("\n");
        sb.append("StoreFile Cnt: ").append(regionLoad.getStorefiles()).append("\n");
        sb.append("StoreFile(MB): ").append(regionLoad.getStorefileSizeMB()).append("\n");
        sb.append("MemStore(MB): ").append(regionLoad.getMemStoreSizeMB()).append("\n");
        sb.append("Request Cnt: ").append(regionLoad.getRequestsCount()).append("\n");
        sb.append("Request(R) Cnt: ").append(regionLoad.getReadRequestsCount()).append("\n");
        sb.append("Request(W) Cnt: ").append(regionLoad.getWriteRequestsCount()).append("\n");
        sb.append("\n");
      }

      sb.append("\n");
    }

    return sb.toString();
  }

}
