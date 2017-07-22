package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.Tag;
import org.apache.hadoop.hbase.util.Bytes;

import com.spike.giantdataanalysis.hbase.example.domain.WebTable;

public class KeyValueExample {

  public static void main(String[] args) {
    final byte[] row = Bytes.toBytes(WebTable.ROWKEY_1);
    final byte[] family = Bytes.toBytes(WebTable.CF_ANCHOR);
    final byte[] qualifier = Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM);
    final long timestamp = 1500683948961l;
    KeyValue.Type type = KeyValue.Type.Put;
    final byte[] value = Bytes.toBytes(WebTable.VALUE_2);
    final List<Tag> tags = new ArrayList<>();// 标签, 放在字节数组中的value之后
    tags.add(new Tag(WebTable.TAG_COMMENT, "this is a comment"));
    tags.add(new Tag(WebTable.TAG_OTHER, "this is a comment too"));

    KeyValue kv = new KeyValue(row, family, qualifier, timestamp, type, value, tags);
    // com.cnn.www/anchor:cssnsi.com/1500683948961/Put/vlen=7/seqid=0
    System.out.println(kv);

    System.out.println(Bytes.toString(CellUtil.cloneRow(kv)));
    System.out.println(Bytes.toString(CellUtil.cloneFamily(kv)));
    System.out.println(Bytes.toString(CellUtil.cloneQualifier(kv)));
    System.out.println(Bytes.toString(CellUtil.cloneValue(kv)));

    List<Tag> _tags = kv.getTags();
    for (Tag t : _tags) {
      System.out.println(t.getType() + ": " + Bytes.toString(t.getValue()));
    }
    System.out.print(Bytes.toString(CellUtil.getTagArray(kv)));
  }
}
