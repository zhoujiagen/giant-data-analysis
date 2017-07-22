package com.spike.giantdataanalysis.hbase.example.client.counter;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.spike.giantdataanalysis.hbase.example.client.basic.BaseExample;
import com.spike.giantdataanalysis.hbase.support.HBases;

public class MultipleCounterExample extends BaseExample {

  static String tableName = "counters";
  static byte[] row = Bytes.toBytes("20110101");
  static byte[] family1 = Bytes.toBytes("daily");
  static byte[] family2 = Bytes.toBytes("weekly");
  static byte[] qualifier1 = Bytes.toBytes("clicks");
  static byte[] qualifier2 = Bytes.toBytes("hits");

  public static void main(String[] args) throws IOException {
    MultipleCounterExample example = new MultipleCounterExample(//
        tableName, Bytes.toString(family1), Bytes.toString(family2));

    example.execute();
  }

  public MultipleCounterExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  @Override
  protected void doExecute() throws IOException {
    Increment increment = new Increment(row);

    increment.addColumn(family1, qualifier1, 1);
    increment.addColumn(family1, qualifier2, 1);
    increment.addColumn(family2, qualifier1, 1);
    increment.addColumn(family2, qualifier2, 1);

    Result result = table.increment(increment);
    System.out.println(HBases.asString(result, true));
  }
}
