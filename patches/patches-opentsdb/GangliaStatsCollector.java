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

/**
 * TSDB统计指标收集器.
 * @author zhoujiagen
 */
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
