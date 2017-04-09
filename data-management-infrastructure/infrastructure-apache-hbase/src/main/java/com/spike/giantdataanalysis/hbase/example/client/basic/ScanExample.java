package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;
import com.spike.giantdataanalysis.hbase.support.HBases;

public class ScanExample extends BaseExample {
  // private static final Logger LOG =
  // LoggerFactory.getLogger(ScanExample.class);

  public ScanExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    ScanExample example = new ScanExample(WebTable.TABLE_NAME, //
        WebTable.CF_ANCHOR, WebTable.CF_CONTENTS, WebTable.CF_PEOPLE);

    example.doWork();
  }

  @Override
  protected void doSomething() throws IOException {
    Scan scan = new Scan()//
        .addColumn(Bytes.toBytes(WebTable.CF_ANCHOR), Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM))
        // Filter filter = null;
        // scan.setFilter(filter);
        .setRowPrefixFilter(Bytes.toBytes("com"))//
        .setMaxVersions(3)//
    // .setMaxResultSize(2L)//
    ;

    try (ResultScanner resultScanner = table.getScanner(scan);) {
      Iterator<Result> iter = resultScanner.iterator();
      // int i = 0;
      while (iter.hasNext()) {
        // LOG.info("Result[" + (i++) + "]=" + iter.next().toString());
        HBases.renderer(iter.next());
      }
    }

  }
}
