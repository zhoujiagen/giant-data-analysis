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

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;

/**
 * Put示例 > put 'webtable', 'com.cnn.www', 'anchor:cssnsi.com', 'CNN' 如何获取Cell中多版本: > create
 * 'webtable', 'anchor' > put 'webtable', 'com.cnn.www', 'anchor:cssnsi.com', 'CNN' > put
 * 'webtable', 'com.cnn.www', 'anchor:cssnsi.com', 'CNN' # 返回多个版本 > scan 'webtable', {VERSIONS => 3}
 * @author zhoujiagen
 */
public class PutExample extends BaseExample {
  private static final Logger LOG = LoggerFactory.getLogger(PutExample.class);

  public PutExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    PutExample example = new PutExample(WebTable.TABLE_NAME, //
        WebTable.CF_ANCHOR, WebTable.CF_CONTENTS, WebTable.CF_PEOPLE);

    example.doWork();
  }

  @Override
  protected void doSomething() throws IOException {

    try {
      Put put = new Put(Bytes.toBytes(AppConstants.ROWKEY_1));
      put.addColumn(//
        Bytes.toBytes(WebTable.CF_ANCHOR), //
        Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM),//
        // 1L, //
        Bytes.toBytes("CNN"));
      LOG.info("PUT " + put.toString());
      table.put(put);

    } catch (IOException e) {
      LOG.error("Put failed", e);
    }
  }
}
