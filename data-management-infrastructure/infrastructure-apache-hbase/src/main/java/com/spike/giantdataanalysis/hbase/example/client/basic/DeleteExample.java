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
