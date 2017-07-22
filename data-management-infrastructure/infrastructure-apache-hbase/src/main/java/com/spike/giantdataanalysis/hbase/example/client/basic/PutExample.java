package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;

/**
 * <pre>
 * Put示例 
 * 
 * > create 'webtable', 'anchor' 
 * > put 'webtable', 'com.cnn.www', 'anchor:cssnsi.com', 'CNN' 
 * > put 'webtable', 'com.cnn.www', 'anchor:cssnsi.com', 'CNN' 
 * > put 'webtable', 'com.cnn.www', 'anchor:cssnsi.com', 'CNN' 
 * 
 * 返回多个版本 
 * > scan 'webtable', {VERSIONS => 3}
 * </pre>
 * @author zhoujiagen
 */
public class PutExample extends BaseExample {
  private static final Logger LOG = LoggerFactory.getLogger(PutExample.class);

  public PutExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    PutExample example = new PutExample(WebTable.T_NAME, //
        WebTable.CF_ANCHOR, WebTable.CF_CONTENTS);

    example.execute();
  }

  @Override
  protected void doExecute() throws IOException {

    try {

      byte[] row = Bytes.toBytes(WebTable.ROWKEY_1);
      byte[] family = Bytes.toBytes(WebTable.CF_ANCHOR);
      byte[] qualifier = Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM);
      Put put = new Put(row);
      put.addColumn(//
        family, //
        qualifier,//
        // 1L, // version timestamp
        Bytes.toBytes("CNN"));

      LOG.info("PUT " + put.toString());
      table.put(put);

      // compare-and-set
      // Atomically checks if a row/family/qualifier value matches the expected value.
      // If it does, it adds the put.
      // If the passed value is null, the check is for the lack of column (ie: non-existance)
      boolean cas = table.checkAndPut(row, family, qualifier, null, put);
      System.out.println(cas);
    } catch (IOException e) {
      LOG.error("Put failed", e);
    }
  }
}
