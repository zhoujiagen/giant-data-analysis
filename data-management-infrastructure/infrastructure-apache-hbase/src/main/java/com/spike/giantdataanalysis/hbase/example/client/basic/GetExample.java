package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;
import com.spike.giantdataanalysis.hbase.support.HBases;

public class GetExample extends BaseExample {
  private static final Logger LOG = LoggerFactory.getLogger(GetExample.class);

  public GetExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    GetExample example = new GetExample(WebTable.T_NAME, //
        WebTable.CF_ANCHOR, WebTable.CF_CONTENTS);

    example.execute();
  }

  @Override
  protected void doExecute() throws IOException {
    Get get = new Get(Bytes.toBytes(WebTable.ROWKEY_1));
    get.addColumn(Bytes.toBytes(WebTable.CF_ANCHOR), Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM));
    // 设置获取最大版本数量
    get.setMaxVersions(3);

    Result result = table.get(get);
    LOG.info(HBases.asString(result, true));
  }
}
