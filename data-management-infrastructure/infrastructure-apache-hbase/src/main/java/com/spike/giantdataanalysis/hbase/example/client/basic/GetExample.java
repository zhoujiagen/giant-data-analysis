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

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;
import com.spike.giantdataanalysis.hbase.support.HBases;

public class GetExample extends BaseExample {
  // private static final Logger LOG = LoggerFactory.getLogger(GetExample.class);

  public GetExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    GetExample example = new GetExample(WebTable.TABLE_NAME, //
        WebTable.CF_ANCHOR, WebTable.CF_CONTENTS, WebTable.CF_PEOPLE);

    example.doWork();
  }

  @Override
  protected void doSomething() throws IOException {
    Get get = new Get(Bytes.toBytes(AppConstants.ROWKEY_1));
    get.addColumn(Bytes.toBytes(WebTable.CF_ANCHOR), Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM));
    // 设置获取最大版本数量
    get.setMaxVersions(3);

    Result result = table.get(get);
    // LOG.info("result=" + result.toString());
    //
    // // 获取Cell
    // List<Cell> cells = result.listCells();
    // LOG.info("Cells=" + cells);
    //
    // CellScanner cellScanner = result.cellScanner();
    // while (cellScanner.advance()) {
    // LOG.info("Cell=" + cellScanner.current());
    // }
    HBases.renderer(result);
  }
}
