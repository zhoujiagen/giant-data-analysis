/*-
 * [[[LICENSE-START]]]
 * GDA[infrastructure-apache-hbase]
 * ==============================================================================
 * Copyright (C) 2017 zhoujiagen@gmail.com
 * ==============================================================================
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * [[[LICENSE-END]]]
 */

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
