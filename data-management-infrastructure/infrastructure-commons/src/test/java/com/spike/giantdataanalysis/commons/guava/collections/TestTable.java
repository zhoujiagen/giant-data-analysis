package com.spike.giantdataanalysis.commons.guava.collections;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * <pre>
 * {@link Table}的单元测试
 * 
 * A table is a collection that takes two keys, a row, and a column, and maps those keys to a single value.
 * </pre>
 *
 * @author zhoujiagen
 */
public class TestTable {

  HashBasedTable<Integer, Integer, String> hashBasedTable;

  @Before
  public void setUp() {
    hashBasedTable = HashBasedTable.create();

    hashBasedTable.put(1, 1, "11");
    hashBasedTable.put(1, 2, "12");
    hashBasedTable.put(1, 3, "13");
    hashBasedTable.put(1, 3, "13+");

    hashBasedTable.put(2, 1, "21");
    hashBasedTable.put(2, 2, "22");
    hashBasedTable.put(2, 3, "23");
    // 各行的列数可以不一致
    hashBasedTable.put(2, 4, "24");

    System.out.println(hashBasedTable);
  }

  @Test
  public void _tableOperation() {
    // cell包含性
    Assert.assertTrue(hashBasedTable.contains(1, 3));
    Assert.assertFalse(hashBasedTable.contains(1, 4));

    // row包含性
    Assert.assertTrue(hashBasedTable.containsRow(1));
    Assert.assertFalse(hashBasedTable.containsRow(3));

    // col包含性
    Assert.assertTrue(hashBasedTable.containsColumn(4));
    Assert.assertFalse(hashBasedTable.containsColumn(5));

    // cell值包含性
    Assert.assertTrue(hashBasedTable.containsValue("21"));
    Assert.assertFalse(hashBasedTable.containsValue("31"));
  }

  @Test
  public void _tableViews() {
    Map<Integer, String> row = hashBasedTable.row(1);
    System.out.println(row);

    Map<Integer, String> col = hashBasedTable.column(4);
    // 会忽略不存在的第1行第4列
    Assert.assertFalse(col.containsKey("1"));
    System.out.println(col);
  }

}
