package com.spike.giantdataanalysis.pipeline.flink;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;

import com.spike.giantdataanalysis.pipeline.flink.ExampleMySQLRecordEventTime.AsyncDatabaseClient;
import com.spike.giantdataanalysis.pipeline.flink.ExampleMySQLRecordEventTime.DummyDomain;

public class TestAsyncDatabaseClient {
  public static void main(String[] args) {
    try (final AsyncDatabaseClient dc = new AsyncDatabaseClient();) {

      dc.init();
      String sql =
          "SELECT createTime, rechargeAmt FROM t_house_orgconsume order by createTime limit 0,10";

      Future<List<List<String>>> f = dc.query(sql);
      List<List<String>> queryResult = f.get();

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      for (List<String> row : queryResult) {
        DummyDomain dd = new DummyDomain();
        if (StringUtils.isNotBlank(row.get(0))) {
          dd.createTime = sdf.parse(row.get(0));
        }
        if (StringUtils.isNotBlank(row.get(1))) {
          dd.amount = Long.parseLong(row.get(1));
        }
        System.out.println(dd);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
