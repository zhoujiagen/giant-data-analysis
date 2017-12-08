package com.spike.giantdataanalysis.pipeline.flink;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.api.functions.async.collector.AsyncCollector;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;

// REF
// (1) Event Time
// https://ci.apache.org/projects/flink/flink-docs-release-1.3/dev/event_timestamps_watermarks.html
public class ExampleMySQLRecordEventTime {

  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

  public static void main(String[] args) throws Exception {

    // limit 0,10
    final String sql = "SELECT readTime, rechargeAmt FROM demo order by readTime";

    final StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironment();

    DataStream<String> input = see.addSource(new SourceFunction<String>() {
      private static final long serialVersionUID = 1L;

      // mock polling
      @Override
      public void run(
          org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext<String> ctx)
          throws Exception {
        String finalSql = sql;
        int pageSize = 100;
        int offsetStart = 0;
        int offsetEnd = offsetStart + pageSize;
        while (true) {
          finalSql = sql + " limit " + offsetStart + "," + offsetEnd;
          System.err.println(finalSql);
          ctx.collect(finalSql);

          Thread.sleep(2000l);
          offsetStart = offsetEnd + 1;
          offsetEnd = offsetStart + pageSize;
        }
      }

      @Override
      public void cancel() {
        // do nothing
      }
    }).shuffle();

    DataStream<String> resultStream =
        AsyncDataStream
            .unorderedWait(input, new AsyncDatabaseRequest(), 15, TimeUnit.SECONDS, 100)
            //
            .assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<DummyDomain>() {
              private static final long serialVersionUID = 1L;

              private long minTime = Long.MAX_VALUE;
              private long triggerDuration = 24 * 60 * 60 * 1000l;

              @Override
              public long extractTimestamp(DummyDomain element, long previousElementTimestamp) {
                long currentTime = element.createTime.getTime();
                minTime = Math.min(minTime, currentTime);
                return currentTime;
              }

              @Override
              public Watermark checkAndGetNextWatermark(DummyDomain lastElement,
                  long extractedTimestamp) {
                if (extractedTimestamp - minTime > triggerDuration) {
                  return new Watermark(extractedTimestamp);
                }
                return null;
              }
            })
            //
            .keyBy("createTime")
            //
            .timeWindow(Time.seconds(5), Time.seconds(5)).sum("amount")
            .map(new MapFunction<DummyDomain, String>() {
              private static final long serialVersionUID = 1L;

              @Override
              public String map(DummyDomain value) throws Exception {
                return sdf2.format(value.createTime) + ": " + value.amount;
              }
            });

    resultStream.print().setParallelism(1);

    String jobName = ExampleMySQLRecordEventTime.class.getSimpleName();
    see.execute(jobName);
  }

  public static class AsyncDatabaseRequest extends RichAsyncFunction<String, DummyDomain> {
    private static final long serialVersionUID = 1L;

    private transient AsyncDatabaseClient client;

    @Override
    public void open(Configuration parameters) throws Exception {
      client = new AsyncDatabaseClient();
      client.init();
    }

    @Override
    public void close() throws Exception {
      client.close();
    }

    @Override
    public void asyncInvoke(String input, AsyncCollector<DummyDomain> asyncCollector)
        throws Exception {
      System.err.println("input=" + input);
      Future<List<List<String>>> f = client.query(input);

      Futures.lazyTransform(f, new Function<List<List<String>>, Void>() {
        @Override
        public Void apply(List<List<String>> input) {
          List<DummyDomain> result = Lists.newArrayList();
          for (List<String> row : input) {

            if (StringUtils.isBlank(row.get(0)) || StringUtils.isBlank(row.get(1))) {
              continue;
            }

            try {
              DummyDomain dd = new DummyDomain();
              dd.createTime = sdf2.parse(row.get(0));

              dd.amount = Long.parseLong(row.get(1));

              result.add(dd);
            } catch (ParseException e) {
              e.printStackTrace();
            }
            // System.err.println(result);
            asyncCollector.collect(result);

          }
          return (Void) null;
        }
      }).get();
    }

  }

  public static class AsyncDatabaseClient implements Serializable, AutoCloseable {
    private static final long serialVersionUID = 1L;

    private Connection conn;
    private ExecutorService es = Executors.newFixedThreadPool(2);;

    public void init() throws StorageException {
      String driver = "com.mysql.jdbc.Driver";
      String url = "jdbc:mysql://localhost/test";
      String user = "root";
      String password = "root";

      try {
        // 加载驱动程序
        Class.forName(driver);

        // 连续数据库
        conn = DriverManager.getConnection(url, user, password);
        if (!conn.isClosed()) {
          System.out.println("Succeeded connecting to the Database!");
        }
      } catch (ClassNotFoundException e) {
        throw StorageException.newException(e);
      } catch (SQLException e) {
        throw StorageException.newException(e);
      } catch (Exception e) {
        throw StorageException.newException(e);
      }
    }

    public Future<List<List<String>>> query(String querySQL) {
      return es.submit(new Callable<List<List<String>>>() {
        @Override
        public List<List<String>> call() throws Exception {
          List<List<String>> result = Lists.newArrayList();

          try (Statement statement = conn.createStatement();
              ResultSet rs = statement.executeQuery(querySQL);) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            while (rs.next()) {
              List<String> row = Lists.newArrayList();
              for (int i = 1; i <= cc; i++) {
                row.add(rs.getString(i));
              }
              result.add(row);
            }
          }

          // System.err.println(result);
          return result;
        }
      });
    }

    @Override
    public void close() throws Exception {
      if (conn != null) {
        conn.close();
      }
      if (es != null) {
        es.shutdown();
      }
    }

  }

  public static class StorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static StorageException newException(String message) {
      return new StorageException(message);
    }

    public static StorageException newException(String message, Throwable cause) {
      return new StorageException(message, cause);
    }

    public static StorageException newException(Throwable cause) {
      return new StorageException(cause);
    }

    public StorageException(String message) {
      super(message);
    }

    public StorageException(String message, Throwable cause) {
      super(message, cause);
    }

    public StorageException(Throwable cause) {
      super(cause);
    }
  }

  public static class DummyDomain {
    public Date createTime;
    public long amount;

    @Override
    public String toString() {
      return "DummyDomain [" + createTime + ", " + amount + "]";
    }

  }

}
