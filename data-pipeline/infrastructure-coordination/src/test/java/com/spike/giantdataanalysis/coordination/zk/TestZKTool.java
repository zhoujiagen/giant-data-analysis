package com.spike.giantdataanalysis.coordination.zk;

import org.apache.zookeeper.CreateMode;

/**
 * @author zhoujiagen@gmail.com
 */
public class TestZKTool {

  public static void main(String[] args) throws Exception {
    ZKTool.startLocalQuorum();
    ZKClient client = new ZKClient(ZKTool.DEFAULT_CONN_STRING, 15000);
    client.initialize();

    client.create("/test", new byte[] {}, CreateMode.PERSISTENT);
    client.create("/test/1", new byte[] {}, CreateMode.PERSISTENT);
    client.create("/test/2", new byte[] {}, CreateMode.PERSISTENT);
    client.create("/test/1/11", new byte[] {}, CreateMode.PERSISTENT);
    client.create("/test/2/21", new byte[] {}, CreateMode.PERSISTENT);

    ZKTool.listTree(client, "/", 3);

    client.delete("/test/1/11", -1);
    client.delete("/test/2/21", -1);
    client.delete("/test/1", -1);
    client.delete("/test/2", -1);
    client.delete("/test", -1);

    client.close();
    ZKTool.stopLocalQuorum();
  }

}
