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

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.util.Bytes;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;

/**
 * <pre>
 * Before:
 * hbase(main):033:0> scan 'webtable', {VERSIONS => 3}
 * ROW                                    COLUMN+CELL                                                                                                    
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1476756834893, value=CNN                                                   
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1476756789258, value=CNN                                                   
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1476756670762, value=CNN                                                   
 * 1 row(s) in 0.0200 seconds
 * 
 * Action:
 * 删除timestamp=1476756670762
 * 
 * After:
 * hbase(main):034:0> scan 'webtable', {VERSIONS => 3}
 * ROW                                    COLUMN+CELL                                                                                                    
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1476756834893, value=CNN                                                   
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1476756789258, value=CNN                                                   
 * 1 row(s) in 0.0190 seconds
 * 
 * </pre>
 * @author zhoujiagen
 */
public class DeleteExample extends BaseExample {

  public DeleteExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    new DeleteExample(WebTable.TABLE_NAME, WebTable.CF_ANCHOR, WebTable.CF_CONTENTS,
        WebTable.CF_PEOPLE)//
        .doWork();
  }

  @Override
  protected void doSomething() throws IOException {
    Delete delete = new Delete(Bytes.toBytes(AppConstants.ROWKEY_1));
    delete.addColumn(Bytes.toBytes(WebTable.CF_ANCHOR),
      Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM), 1476756670762L);

    // 删除指定的row和cell
    table.delete(delete);
  }

}
