package com.spike.giantdataanalysis.hbase.example.client.counter;

import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;

import com.spike.giantdataanalysis.hbase.example.client.basic.BaseExample;

public class SingleCounterExample extends BaseExample {
  static String tableName = "counters";
  static byte[] row = Bytes.toBytes("20110101");
  static byte[] family = Bytes.toBytes("daily");
  static byte[] qualifier = Bytes.toBytes("hits");

  public static void main(String[] args) throws IOException {
    SingleCounterExample example = new SingleCounterExample(//
        tableName, Bytes.toString(family));

    example.execute();
  }

  public SingleCounterExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  @Override
  public void doExecute() throws IOException {

    long cnt1 = table.incrementColumnValue(row, family, qualifier, 1);// +1
    long cnt2 = table.incrementColumnValue(row, family, qualifier, 1);// +1

    // 获取当前值
    long current = table.incrementColumnValue(row, family, qualifier, 0);

    long cnt3 = table.incrementColumnValue(row, family, qualifier, -1);// -1

    System.out.println(cnt1);// 1
    System.out.println(cnt2);// 2
    System.out.println(current);// 2
    System.out.println(cnt3);// 1

  }
}
