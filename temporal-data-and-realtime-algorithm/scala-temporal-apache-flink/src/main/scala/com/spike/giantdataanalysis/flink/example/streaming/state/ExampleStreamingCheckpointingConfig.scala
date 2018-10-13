package com.spike.giantdataanalysis.flink.example.streaming.state

import java.util.concurrent.TimeUnit

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.time.Time
import org.apache.flink.runtime.state.StateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
  * 状态检查点配置:
  * 1 选择状态后端
  * 2 迭代作业中状态检查点
  * 3 重启策略
  */
object ExampleStreamingCheckpointingConfig {
  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    // 每1秒开启检查点, 默认EXACTLY_ONCE
    env.enableCheckpointing(1000L)
    // 强制在迭代作业中开启 - 已废弃
    env.enableCheckpointing(1000L, CheckpointingMode.EXACTLY_ONCE, force = true)

    // 设置检查点模式
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    // 设置检查点超时时间
    env.getCheckpointConfig.setCheckpointTimeout(6000)

    // 设置两次检查点之间最小停顿时间
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)

    // 设置并发检查点数量
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)

    // 设置状态后端
    val backend: StateBackend = new MemoryStateBackend
    env.setStateBackend(backend)
  }

  //---------------------------------------------------------------------------
  // 重启策略
  //
  // Fixed Delay Restart Strategy
  // Failure Rate Restart Strategy
  // No Restart Strategy
  // Fallback Restart Strategy: 集群中的重试策略
  //---------------------------------------------------------------------------

  private def restartStrategy(env: StreamExecutionEnvironment): Unit = {
    // 等待10秒重试, 最多重试3次
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
      3, Time.of(10, TimeUnit.SECONDS)))
    // 5秒内最多失败3次, 等待10秒重试
    env.setRestartStrategy(RestartStrategies.failureRateRestart(
      3,
      Time.of(50, TimeUnit.SECONDS),
      Time.of(10, TimeUnit.SECONDS)))
    // 不重试
    env.setRestartStrategy(RestartStrategies.noRestart())
  }
}




