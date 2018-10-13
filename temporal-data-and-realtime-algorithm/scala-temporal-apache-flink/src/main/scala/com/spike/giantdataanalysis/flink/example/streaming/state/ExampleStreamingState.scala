package com.spike.giantdataanalysis.flink.example.streaming.state


import java.beans.Transient
import java.util
import java.util.Collections

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.state.{CheckpointListener, FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.{CheckpointedFunction, ListCheckpointed}
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * keyed state: managed, raw
  * operator state: managed, raw
  *
  * state只可以通过RuntimeContext访问, 即只可以在RichFunction中使用.
  */
object ExampleStreamingState {

  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    //    managedKeyedState(env)
    //    managedKeyedState2(env)
    //    managedOperatorState(env)
    statefulSource(env)

    env.execute()
  }


  private def managedKeyedState(env: StreamExecutionEnvironment): Unit = {
    env.fromCollection(List(
      (1L, 3L),
      (1L, 5L), // (1, 4)
      (1L, 7L),
      (1L, 4L), // (1, 5): (7+4)/2=5
      (1L, 2L)
    )).keyBy(_._1)
      .flatMap(new CountWindowAverage)
      .print()
  }

  private def managedKeyedState2(env: StreamExecutionEnvironment): Unit = {
    env.fromCollection(List(
      (1, 3), // (1, 3)
      (1, 5), // (1, 8)
      (1, 7), // (1, 15)
      (1, 4), // (1, 19)
      (1, 2)
    )).keyBy(_._1)
      .mapWithState { (in: (Int, Int), count: Option[Int]) =>
        count match {
          case Some(c) => ((in._1, c), Some(c + in._2))
          case None => ((in._1, 0), Some(in._2))
        }
      }
      .print()
  }


  private def managedOperatorState(env: StreamExecutionEnvironment): Unit = {
    env.fromCollection(List(
      ("a", 1),
      ("b", 2),
      ("c", 3),
      ("a", 3),
      ("b", 2),
      ("c", 1)
    )).filter(_._2 >= 0)
      .addSink(new ExampleBufferingSink(2))
  }


  private def statefulSource(env: StreamExecutionEnvironment): Unit = {
    // 检查点配置
    env.enableCheckpointing(1000)

    env.addSource(new ExampleStatefulSource)
      .print()
  }
}


//---------------------------------------------------------------------------
// managed keyed state
//---------------------------------------------------------------------------
// IN: (Long, Long)
// OUT: (Long, Long)
class CountWindowAverage extends RichFlatMapFunction[(Long, Long), (Long, Long)] {
  val LOG: Logger = LoggerFactory.getLogger(classOf[CountWindowAverage])

  private var sumState: ValueState[(Long, Long)] = _ // (count, sum of value)

  override def open(parameters: Configuration): Unit = {
    sumState = getRuntimeContext.getState(
      // 创建状态描述符
      new ValueStateDescriptor[(Long, Long)](
        "average",
        createTypeInformation[(Long, Long)] // org.apache.flink.streaming.api.scala._
      )
    )
  }

  override def flatMap(value: (Long, Long), out: Collector[(Long, Long)]): Unit = {
    var currentSumStateValue = sumState.value()

    currentSumStateValue = if (currentSumStateValue != null) {
      currentSumStateValue
    } else {
      (0L, 0L)
    }
    if (LOG.isDebugEnabled) {
      LOG.debug("state: " + currentSumStateValue + ", value: " + value + ".")
    }

    val newSumStateValue = (currentSumStateValue._1 + 1, currentSumStateValue._2 + value._2)
    sumState.update(newSumStateValue)

    if (newSumStateValue._1 >= 2) {
      out.collect((value._1, newSumStateValue._2 / newSumStateValue._1))
      sumState.clear()
    }
  }
}

// State Time-To-Live (TTL)
// 在状态描述符上设置TTL: org.apache.flink.api.common.state.StateTtlConfig


//---------------------------------------------------------------------------
// managed operator state
//
// CheckpointedFunction
// ListCheckpointed: only supports list-style state with even-split redistribution scheme on restore
//---------------------------------------------------------------------------
class ExampleBufferingSink(threshold: Int = 0) extends SinkFunction[(String, Int)] with CheckpointedFunction {
  val LOG: Logger = LoggerFactory.getLogger(classOf[ExampleBufferingSink])

  @Transient
  private var checkpointedState: ListState[(String, Int)] = _ // 检查点状态

  private val bufferedElements = ListBuffer[(String, Int)]() // 缓存的元素

  // sink被调用
  override def invoke(value: (String, Int)) {
    bufferedElements += value
    if (bufferedElements.size == threshold) {
      for (e <- bufferedElements) {
        LOG.info("sink element: " + e)
      }
      bufferedElements.clear()
    }
  }

  // 初始化状态
  override def initializeState(context: FunctionInitializationContext): Unit = {
    if (LOG.isDebugEnabled) {
      LOG.debug("initialize state")
    }
    checkpointedState = context.getOperatorStateStore // 操作符状态存储
      .getListState(
      new ListStateDescriptor[(String, Int)](
        "buffered-elements",
        createTypeInformation[(String, Int)]))

    if (context.isRestored) { // restore
      if (LOG.isDebugEnabled) {
        LOG.debug("restore state: " + checkpointedState.get())
      }

      val iter = checkpointedState.get().iterator()
      while (iter.hasNext) {
        bufferedElements += iter.next()
      }
    }
  }

  // 快照状态
  override def snapshotState(context: FunctionSnapshotContext): Unit = {
    if (LOG.isDebugEnabled) {
      LOG.debug("snapshot state: " + checkpointedState.get())
    }
    checkpointedState.clear()
    for (e <- bufferedElements) {
      checkpointedState.add(e)
    }
  }

}

// 有状态的数据源函数
class ExampleStatefulSource extends RichParallelSourceFunction[Long]
  with ListCheckpointed[java.lang.Long]
  with CheckpointListener {

  val LOG = LoggerFactory.getLogger(classOf[ExampleStatefulSource])

  @volatile private var running = true

  private var offset = 0L

  override def snapshotState(checkpointId: Long, timestamp: Long): util.List[java.lang.Long] = {
    val result = Collections.singletonList(Long.box(offset))
    if (LOG.isDebugEnabled) {
      LOG.debug("snapshot state: checkpointId=" + checkpointId, ", timestamp=" + timestamp + ", state=" + result + ".")
    }
    result
  }

  // CheckpointListener: 检查点完成通知
  override def notifyCheckpointComplete(checkpointId: Long): Unit = {
    if (LOG.isDebugEnabled) {
      LOG.debug("checkpoint finished: " + checkpointId + ".")
    }
  }

  override def restoreState(state: util.List[java.lang.Long]): Unit = {
    for (s <- state.asScala) {
      offset = s
    }
  }

  override def run(ctx: SourceFunction.SourceContext[Long]): Unit = {
    val lock = ctx.getCheckpointLock // 协议: 获取检查点锁
    while (running) {
      lock.synchronized {
        ctx.collect(offset) // 输出
        offset += 1 // 状态更新
      }

      Thread.sleep(1000L) // mock some delay
    }
  }

  override def cancel(): Unit = running = false
}