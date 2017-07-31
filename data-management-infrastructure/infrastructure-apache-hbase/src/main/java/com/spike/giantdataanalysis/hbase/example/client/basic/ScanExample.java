package com.spike.giantdataanalysis.hbase.example.client.basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.hbase.example.domain.TestTable;
import com.spike.giantdataanalysis.hbase.support.Log4jCounterAppender;

public class ScanExample extends BaseExample {
  private static final Logger LOG = LoggerFactory.getLogger(ScanExample.class);

  public ScanExample(String tableName, String... columnFamilyNames) {
    super(tableName, columnFamilyNames);
  }

  public static void main(String[] args) throws IOException {
    ScanExample example = new ScanExample(TestTable.T_NAME, //
        Bytes.toString(TestTable.CF_1), Bytes.toString(TestTable.CF_2));

    // 只执行一次
    // example.prepareData();

    example.execute();
  }

  // 准备数据
  void prepareData() throws IOException {
    LOG.info("准备数据");

    // 200个单元格
    int rowCnt = 10;
    int colCnt = 10;
    List<Put> puts = new ArrayList<>();
    for (int r = 0; r < rowCnt; r++) {
      for (int c = 0; c < colCnt; c++) {
        Put put = new Put(Bytes.toBytes(TestTable.PREFIX_ROW + r));
        put.addColumn(//
          TestTable.CF_1, //
          Bytes.toBytes(TestTable.PREFIX_C + c),//
          Bytes.toBytes(TestTable.PREFIX_V + (c * rowCnt + r)));
        put.addColumn(//
          TestTable.CF_2, //
          Bytes.toBytes(TestTable.PREFIX_C + c),//
          Bytes.toBytes(TestTable.PREFIX_V + (c * rowCnt + r)));
        puts.add(put);
      }
    }
    table.put(puts);
  }

  @Override
  public void doExecute() throws IOException {
    this.doScan(1, 1);
    this.doScan(200, 1);
    this.doScan(2000, 100);
    this.doScan(2, 100);
    this.doScan(2, 10);
    this.doScan(5, 100);
    this.doScan(5, 20);
    this.doScan(10, 10);
  }

  void doScan(int caching, int batch) throws IOException {

    AtomicLong resultCnt = new AtomicLong(0l);

    Log4jCounterAppender appender = new Log4jCounterAppender();
    appender.init();

    Scan scan = new Scan();
    /**
     * <pre>
     * setBatch()
     * Set the maximum number of values to return for each call to next(). 
     * Callers should be aware that invoking this method with any value is equivalent to 
     * calling setAllowPartialResults(boolean) with a value of true; 
     * partial results may be returned if this method is called. 
     * Use setMaxResultSize(long)} to limit the size of a Scan's Results instead.
     * </pre>
     */
    // TODO(zhoujiagen) 待确认是否是特性
    // 预期: PC请求的次数=(行数x每行的列数)/Min(每行的列数，批量大小)/扫描器缓存
    // 情况: 返回结果数量是正确的, 返回PRC数量为0
    // 已确认不是未调用HTable的问题, 以及同时在客户端和服务器端设置hbase.client.scanner.caching为1
    scan.setCaching(caching);// 行级操作
    scan.setBatch(batch); // 列级操作: 行内扫描

    try (ResultScanner resultScanner = table.getScanner(scan);) {
      Iterator<Result> it = resultScanner.iterator();
      while (it.hasNext()) {
        it.next();
        // LOG.info(HBases.asString(it.next(), true));
        resultCnt.incrementAndGet();
      }
    }
    System.err.println("C=" + caching + ", B=" + batch + " "//
        + "RPC: " + appender.getCounter() + ", RESULT: " + resultCnt.get());
  }
}
