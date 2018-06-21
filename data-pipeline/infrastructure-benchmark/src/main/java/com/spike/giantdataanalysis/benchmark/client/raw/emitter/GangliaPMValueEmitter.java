package com.spike.giantdataanalysis.benchmark.client.raw.emitter;

import info.ganglia.gmetric4j.gmetric.GMetric;
import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;
import info.ganglia.gmetric4j.gmetric.GMetricSlope;
import info.ganglia.gmetric4j.gmetric.GangliaException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.benchmark.client.raw.exception.BenchmarkException;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMValue;

/**
 * <pre>
 * 基于Ganglia的性能指标值提交器
 * 
 * 注意实现中的参数:
 * (1) host_dmax（以秒为单位的整数值）
 * dmax是delete max的缩写。当值为0时，即使远程主机停止报告，gmond也不会从列表里删除该主机。
 * 如果host_dmax设置为正值，  当gmond在“host_dmax”秒内接收不到某台主机的数据，gmond将删除该主机。
 * 
 * (2) host_tmax（以秒为单位的整数值）
 * tmax是timeout max的缩写，代表gmond等待一台主机更新的最长时间。
 * 因为消息可能在网络中丢失，所以如果在4倍的host_tmax时间内接收不到某台主机的任何消息，gmond就认为该主机已经崩溃。
 * </pre>
 */
public class GangliaPMValueEmitter implements PMValueEmitter {
  private static final Logger LOG = LoggerFactory.getLogger(GangliaPMValueEmitter.class);

  private GMetric gMetric;

  /** 认为主机崩溃的最长时间: 1天 */
  public static final int DEFAULT_TMAX = 60 * 60 * 24;

  /** 保持主机度量的时间: 0用不删除 */
  public static final int DEFAULT_DMAX = 0;

  /**
   * @param host localhost
   * @param port 8649
   */
  public GangliaPMValueEmitter(String host, int port) {
    LOG.info("初始化Ganglia GMetric, 参数: host={}, port={}", host, port);

    try {
      gMetric = new GMetric(host, port, UDPAddressingMode.UNICAST, 1, true);
    } catch (IOException e) {
      LOG.error("初始化Ganglia GMetric失败", e);
    }
  }

  @Override
  public void close() throws Exception {
    LOG.info("关闭Ganglia GMetric");

    gMetric.close();
  }

  @Override
  public void emit(PMValue pmValue) throws BenchmarkException {
    LOG.info("向Ganglia GMetric提交采样值: {}", pmValue.toString());

    try {
      gMetric.announce(pmValue.getMetric(), pmValue.getValue(), pmValue.getValueType(),
        pmValue.getUnit(), GMetricSlope.BOTH, DEFAULT_TMAX, DEFAULT_DMAX, pmValue.getGroup());
    } catch (GangliaException e) {
      LOG.error("GMetric发送数据失败", e);
      throw BenchmarkException.newException(e);
    }
  }

}
