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
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1500682882891, value=CNN                                                   
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1500682882891, value=CNN                                                   
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1500652324685, value=CNN                                                   
 * 1 row(s) in 0.0200 seconds
 * 
 * Action:
 * 删除timestamp=1476756670762
 * 
 * After:
 * hbase(main):034:0> scan 'webtable', {VERSIONS => 3}
 * ROW                                    COLUMN+CELL                                                                                                    
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1500682882891, value=CNN                                                   
 *  com.cnn.www                           column=anchor:cssnsi.com, timestamp=1500682882891, value=CNN                                                   
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
    new DeleteExample(WebTable.T_NAME, WebTable.CF_ANCHOR, WebTable.CF_CONTENTS)//
        .execute();
  }

  @Override
  protected void doExecute() throws IOException {

    byte[] row = Bytes.toBytes(WebTable.ROWKEY_1);
    byte[] family = Bytes.toBytes(WebTable.CF_ANCHOR);
    byte[] qualifier = Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM);
    byte[] value = null;
    long timestamp = 1500652324685L;

    Delete delete = new Delete(row);
    delete.addColumn(family, qualifier, timestamp);

    // 删除指定的row和cell
    table.delete(delete);

    // compare and delete
    // Atomically checks if a row/family/qualifier value matches the expected value.
    // If it does, it adds the delete.
    // If the passed value is null, the check is for the lack of column (ie: non-existance)
    table.checkAndDelete(row, family, qualifier, value, delete);
  }

}
