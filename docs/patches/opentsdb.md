# Patches of OpenTSDB

##  程序/工具入口

tsdb命令

```
      case $1 in
        (fsck)
          MAINCLASS=Fsck              ===========================
          ;;
        (import)
          MAINCLASS=TextImporter
          ;;
        (mkmetric)
          shift
          set uid assign metrics "$@"
          MAINCLASS=UidManager
          ;;
        (query)
          MAINCLASS=CliQuery
          ;;
        (tsd)
          MAINCLASS=TSDMain           ===========================   TSD
          ;;
        (scan)
          MAINCLASS=DumpSeries        ===========================
          ;;
        (search)
          MAINCLASS=Search
          ;;
        (uid)
          MAINCLASS=UidManager
          ;;
        (version)
          MAINCLASS=BuildData
          ;;
        (*)
          echo >&2 "$me: error: unknown command '$1'"
          usage
          ;;
      esac

      ......

      exec $JAVA $JVMARGS -classpath "$CLASSPATH" net.opentsdb.tools.$MAINCLASS "$@"
```

##  net.opentsdb.tools.TSDMain

#### (1) 流程

加载配置, 初始化TSDB, RpcManager;

启动JBoss Netty协议处理(PipelineFactory=>IdleStateHandler,RpcHandler,ConnectionManager);

#### (2) 实例
以内建HTTP RPC请求处理为例,

```
    net.opentsdb.tsd.RpcManager.instance(TSDB)
    |-- net.opentsdb.tsd.RpcManager.initializeBuiltinRpcs(String, Builder<String, TelnetRpc>, Builder<String, HttpRpc>)

    net.opentsdb.tsd.RpcHandler.messageReceived(ChannelHandlerContext, MessageEvent)
    |-- net.opentsdb.tsd.RpcHandler.handleHttpQuery(TSDB, Channel, HttpRequest)
    |--|-- net.opentsdb.tsd.HttpRpc.execute(TSDB, HttpQuery)  路由后调用接口防范
    |--|-- net.opentsdb.tsd.PutDataPointRpc.execute(TSDB, HttpQuery) 写入数据点的接口实现
    |--|--|-- net.opentsdb.core.TSDB.addPoint(String, long, long, Map<String, String>) 调用TSDB中方法
    |--|--|--|-- net.opentsdb.core.TSDB.addPointInternal(String, long, byte[], Map<String, String>, short)
```

#### (3) 具体的写入数据点

相关的配置选项

```
tsd.core.storage_exception_handler.enable
tsd.core.storage_exception_handler.plugin 存储异常处理插件, 目前没有可用的实现.

tsd.storage.compaction.flush_interval          后台线程在两次压缩队列刷新调用之间的等待秒数.
tsd.storage.compaction.flush_speed             尝试刷新压缩队列的频率, 2表示在30分钟内, 1表示1小时.
tsd.storage.compaction.max_concurrent_flushes  任意时刻刷入HBase的压缩调用的最大数量
tsd.storage.compaction.min_flush_threshold     压缩队列在刷新之前的阈值大小

tsd.storage.enable_appends
tsd.storage.enable_compaction
tsd.storage.fix_duplicates
tsd.storage.flush_interval
```

核心过程

```
net.opentsdb.core.TSDB.addPointInternal(String, long, byte[], Map<String, String>, short)

final byte[] row = IncomingDataPoints.rowKeyTemplate(this, metric, tags);// MARK 按<metric><tagk1><tagv1>...构成行键模板, 缺基准时间(小时级别)
final long base_time; // MARK 按小时计的基准时间
final byte[] qualifier = Internal.buildQualifier(timestamp, flags); // MARK 确定列限定符

net.opentsdb.core.TSDB.addPointInternal(...).WriteCB
    scheduleForCompaction(row, (int) base_time); // MARK(zhoujiagen) 加入压缩队列
        compactionq.add(row); // MARK 压缩队列中实际存储的是完整的行键
    final PutRequest point = new PutRequest(table, row, FAMILY, qualifier, value);
    result = client.put(point);// MARK(zhoujiagen) 写入HBase: 同一行中不同的列中
```

#### 压缩的处理

```
net.opentsdb.core.CompactionQueue.CompactionQueue(TSDB)
    if (tsdb.config.enable_compactions()) {
      startCompactionThread();// MARK(zhoujiagen) 启动后台压缩队列中的压缩线程
    }
```

###### (1) TSDB中显式调用

```
net.opentsdb.core.TSDB.flush()
    net.opentsdb.core.CompactionQueue.flush() // MARK(zhoujiagen) 将压缩队列中一个小时之前的数据刷入HBase中.
```

###### (2) 后台压缩线程

```
net.opentsdb.core.CompactionQueue.Thrd.run()
    if (size > min_flush_threshold) { // MARK  仅在超过阈值时处理

    final int maxflushes = Math.max(min_flush_threshold, size * flush_interval * flush_speed / Const.MAX_TIMESPAN);
    flush(now / 1000 - Const.MAX_TIMESPAN - 1, maxflushes); // MARK 压缩线程中执行压缩
        else if (nflushes == max_concurrent_flushes) { // MARK 实际含义是指压缩线程的一次唤醒操作的最大行数
        // We kicked off the compaction of too many rows already, let's wait
        // until they're done before kicking off more.
        break;
        }
        ds.add(tsdb.get(row).addCallbacks(compactcb, handle_read_error)); // MARK 执行一行的压缩: 先读再压缩
```

###### 压缩参数的解释

待压缩的是一小时的数据, 当前压缩队列中有size行待压缩, 要在一小时之内处理完.
size * flush_speed / Const.MAX_TIMESPAN : 一次压缩线程操作的行数
flush_speed: 加速压缩速度
max_concurrent_flushes: 实际含义是指压缩线程的一次唤醒操作的最大行数


压缩回调

```
net.opentsdb.core.CompactionQueue.CompactCB
    net.opentsdb.core.CompactionQueue.compact(ArrayList<KeyValue>, KeyValue[], List<Annotation>)
        net.opentsdb.core.CompactionQueue.Compaction.compact()
```

执行单行的压缩

```
net.opentsdb.core.CompactionQueue.Compaction.compact()
    heap = new PriorityQueue<ColumnDatapointIterator>(nkvs);
    int tot_values = buildHeapProcessAnnotations(); // 构建数据点(原列限定符)的堆, 假设: 之前没有压缩过. 将这些数据点加入待删除列表.
    mergeDatapoints(compacted_qual, compacted_val); // MARK 压缩原列限定符和值为单个列限定符和单值
    // MARK 下面是将压缩写回HBase的过程
    Deferred<Object> deferred = tsdb.put(key, compact.qualifier(), compact.value()); // MARK 写入压缩后的列限定符和值
    deferred = deferred.addCallbacks(new DeleteCompactedCB(to_delete), handle_write_error); // MARK 写入成功后删除原列限定符和值
```

删除回调

```
net.opentsdb.core.CompactionQueue.DeleteCompactedCB.call(Object)
    tsdb.delete(key, qualifiers).addErrback(handle_delete_error);// MARK 执行删除
```

失败处理回调

```
net.opentsdb.core.CompactionQueue.HandleErrorCB.call(Exception)
    add(((HBaseRpc.HasKey) rpc).key());// MARK 重新加入压缩队列, 待后续重试(这种情况下, 压缩的列限定符和值已经写入HBase)
```

##  扩展

#### 1 GangliaStatsCollector

TSDB统计指标收集器.

```
com.spike.giantdataanalysis.opentsdb.GangliaStatsCollector


// 参考统计RPC中指标收集
net.opentsdb.tsd.StatsRpc.execute(TSDB, HttpQuery)

ConnectionManager.collectStats(collector);
RpcHandler.collectStats(collector);
RpcManager.collectStats(collector);
tsdb.collectStats(collector);
```

程序入口处开启统计信息收集背景线程

pom.xml

```
        <!-- MARK(zhoujiagen) Ganglia metric sender -->
        <dependency>
            <groupId>info.ganglia.gmetric4j</groupId>
            <artifactId>gmetric4j</artifactId>
            <version>1.0.10</version>
        </dependency>
```

net.opentsdb.tools.TSDMain

```
    private static StatsCollectorWorker collector;


      public static void main(String[] args) throws IOException {
        ...
        // The server is now running in separate threads, we can exit main.
        // MARK(zhoujiagen) 执行指标收集
        collector = new StatsCollectorWorker(new GangliaStatsCollector("tsdb", tsdb));
        new Thread(collector, "OpenTSDB StatsCollectorWorker").start();
        registerShutdownCollectorHook();
```

StatsCollectorWorker

```
    package net.opentsdb.tsd;

    import java.util.Date;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import com.google.common.base.Preconditions;
    import com.spike.giantdataanalysis.opentsdb;

    /**
     * 定制的统计信息收集工作者
     */
    public class StatsCollectorWorker implements Runnable {
        private static final Logger LOG = LoggerFactory.getLogger(StatsCollectorWorker.class);

        private final GangliaStatsCollector collector;

        public static long SLEEP_DURATION = 10000l;

        public StatsCollectorWorker(GangliaStatsCollector collector) {
            Preconditions.checkArgument(collector != null, "Argument GangliaStatsCollector should not be null!");

            this.collector = collector;
        }

        public void shutdown() {
            LOG.info("Shutdown StatsCollectorWorker");

            System.exit(0);
        }

        @Override
        public void run() {

            while (true) {
                LOG.debug("Collect Stats at timestamp: {}", new Date().getTime());

                ConnectionManager.collectStats(collector);
                RpcHandler.collectStats(collector);
                RpcManager.collectStats(collector);
                collector.getTsdb().collectStats(collector);

                try {
                    Thread.sleep(SLEEP_DURATION);
                } catch (InterruptedException e) {}
            }

        }

    }
```

GangliaStatsCollector

```
      package com.spike.giantdataanalysis.opentsdb;

        import info.ganglia.gmetric4j.gmetric.GMetric;
        import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;
        import info.ganglia.gmetric4j.gmetric.GMetricSlope;
        import info.ganglia.gmetric4j.gmetric.GMetricType;
        import info.ganglia.gmetric4j.gmetric.GangliaException;

        import java.io.IOException;
        import java.util.List;

        import net.opentsdb.core.TSDB;
        import net.opentsdb.stats.StatsCollector;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import com.google.common.base.Preconditions;
        import com.google.common.base.Splitter;


        public final class GangliaStatsCollector extends StatsCollector {
            private static final Logger LOG = LoggerFactory.getLogger(GangliaStatsCollector.class);

            public static final String SEP = " ";
            public static final String SEP_KV = "=";
            public static final String METRIC_NAME_SEP = "_";
            public static final String SOURCE_METRIC_NAME_SEP = ".";

            public static final String KEY_HOST = "host";
            public static final String KEY_FQDN = "fqdn";

            private final TSDB tsdb;

            private GMetric gMetric;
            /** 认为主机崩溃的最长时间: 1天 */
            public static final int DEFAULT_TMAX = 60 * 60 * 24;
            /** 保持主机度量的时间: 0用不删除 */
            public static final int DEFAULT_DMAX = 0;

            public GangliaStatsCollector(String prefix, TSDB tsdb) {
                super(prefix);

                Preconditions.checkArgument(tsdb != null, "Argument tsdb should not be null!");
                this.tsdb = tsdb;

                String gangliaHost = tsdb.getConfig().ganglia_host();
                int gangliaPort = tsdb.getConfig().ganglia_port();
                LOG.debug("初始化Ganglia GMetric, 参数: host={}, port={}", gangliaHost, gangliaPort);
                try {
                    gMetric = new GMetric(gangliaHost, gangliaPort, UDPAddressingMode.UNICAST, 1, true);
                } catch (IOException e) {
                    LOG.error("初始化Ganglia GMetric失败", e);
                }

            }

            /**
             * <pre>
             * datapoint的格式:
             * 0|prefix.name 1|timestamp 2|value 3|key1=value1 4|key2=value2 ...
             *
             * 其中:
             * key=value中有host=hostname或者fqdn=hostname, 将其追加port, 已处理单个主机上启动了多个TSDB实例的情况.
             * </pre>
             */
            @Override
            public void emit(String datapoint) {
                if (datapoint == null || "".equals(datapoint)) {
                    return;
                }

                List<String> fields = Splitter.on(" ").splitToList(datapoint);
                if (fields == null || fields.size() == 0) {
                    return;
                }
                StringBuilder metric = new StringBuilder();
                metric.append(fields.get(0).replaceAll(METRIC_NAME_SEP, SOURCE_METRIC_NAME_SEP));// _ => .
                String tempStr = null;
                String key, value;
                for (int i = 3, len = fields.size(); i < len; i++) {
                    tempStr = fields.get(i);
                    if (tempStr.contains(SEP_KV)) {
                        key = tempStr.substring(0, tempStr.indexOf(SEP_KV));
                        value = tempStr.substring(tempStr.indexOf(SEP_KV) + 1, tempStr.length());

                        // 添加port
                        if (KEY_HOST.equals(key) || KEY_FQDN.equals(key)) {
                            value = value + METRIC_NAME_SEP + String.valueOf(tsdb.getConfig().getInt("tsd.network.port"));
                        }
                        metric.append(METRIC_NAME_SEP).append(value);
                    }
                }

                this.emit(prefix, metric.toString(), fields.get(2), "");

            }

            private void emit(String group, String metric, String value, String unit) {
                LOG.debug("向Ganglia GMetric提交采样值, group={}, metric={}, value={}, unit={}",//
                        group, metric, value, unit);

                try {
                    gMetric.announce(metric, value, GMetricType.INT32, unit, //
                            GMetricSlope.BOTH, DEFAULT_TMAX, DEFAULT_DMAX, group);
                } catch (GangliaException e) {
                    LOG.error("GMetric发送数据失败", e);
                }
            }

            public TSDB getTsdb() {
                return tsdb;
            }
        }
```

扩展的配置项 net.opentsdb.utils.Config

```
    // MARK(zhoujiagen) 自定义配置参数
    /** tsd.extension.ganglia.host */
    private String ganglia_host = "localhost";
    /** tsd.extension.ganglia.port */
    private int ganglia_port = 8649;


    // MARK(zhoujiagen) Ganglia配置属性getter
    public String ganglia_host() {
        return ganglia_host;
    }
    public int ganglia_port() {
        return ganglia_port;
    }


    protected void loadStaticVariables() {
    ...
      // MARK(zhoujiagen) 更新Ganglia配置
      ganglia_host = this.getString("tsd.extension.ganglia.host");
      ganglia_port = this.getInt("tsd.extension.ganglia.port");
```


opentsdb.conf

```
    ## ----------- EXTENSION -----------
    ## 指标收集Ganglia host
    tsd.extension.ganglia.host=localhost
    ## 指标收集Ganglia port
    tsd.extension.ganglia.port=8649
```

#### 2 KafkaRTPublisher

使用Kafka的实时发布器.

```
com.spike.giantdataanalysis.opentsdb.KafkaRTPublisher

tsd.extension.plugin.kakfa.file
tsd.extension.plugin.rt.kakfa.topic.annotation
```

#### 3 KafkaStorageExceptionHandler

使用Kafka的存储异常处理器.

```
com.spike.giantdataanalysis.opentsdb.KafkaStorageExceptionHandler

tsd.extension.plugin.kakfa.file
tsd.extension.plugin.storage_exception_handler.kakfa.topic.error
```
