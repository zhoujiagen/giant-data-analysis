package com.spike.giantdataanalysis.hbase.example.client.management;

import java.io.IOException;

import org.apache.hadoop.hbase.ClusterStatus;

import com.spike.giantdataanalysis.hbase.example.client.basic.BaseExample;
import com.spike.giantdataanalysis.hbase.support.HBaseAdmins;

public class HBaseAdminExample extends BaseExample {

  public static void main(String[] args) throws IOException {
    HBaseAdminExample example = new HBaseAdminExample();
    example.execute();
  }

  @Override
  protected void doExecute() throws IOException {
    // 集群状态信息
    ClusterStatus status = admin.getClusterStatus();
    System.out.println(HBaseAdmins.asString(status));
  }
}
