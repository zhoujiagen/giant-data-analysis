package com.spike.giantdataanalysis.flink.example.streaming.state

import java.util.Date

import org.apache.flink.api.common.JobID
import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.state.{ReducingStateDescriptor, ValueStateDescriptor}
import org.apache.flink.queryablestate.client.QueryableStateClient
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._

/**
  * WARNING: Beta API.
  * Queryable State Beta:
  *
  * @see com.spike.giantdataanalysis.flink.example.streaming.state.ExampleStatefulSource
  */
object ExampleStreamingQueryableState {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.enableCheckpointing(2000L)

    val stateDescriptor: ReducingStateDescriptor[(Long, Long)] = new ReducingStateDescriptor[(Long, Long)](
      "example-queryable-reduce",
      new ReduceFunction[(Long, Long)] {
        override def reduce(value1: (Long, Long), value2: (Long, Long)): (Long, Long) =
          (value1._1, math.max(value1._2, value2._2))
      },
      createTypeInformation[(Long, Long)])
    env.addSource(new ExampleQueryableStateSource)
      .keyBy(0)
      .asQueryableState("example-queryable", stateDescriptor)

    env.execute()
  }
}

class ExampleQueryableStateSource extends SourceFunction[(Long, Long)] {
  @volatile private var running: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[(Long, Long)]): Unit = {
    while (running) {
      Thread.sleep(500L)
      val value = new Date().getTime
      ctx.collect((value % 10, value))
    }
  }

  override def cancel(): Unit = running = false
}


object ExampleStreamingQueryableStateClient {
  def main(args: Array[String]): Unit = {
    val client = new QueryableStateClient("localhost", 9069) // query log
    val jobId: JobID = JobID.fromHexString("0ea2162ab4cf7bf7cbbd60e138b44017") // use web ui
    val queryableStateName: String = "example-queryable"
    var key: Long = 5
    val keyTypeInfo = createTypeInformation[Long]
    val stateDescriptor = new ValueStateDescriptor[(Long, Long)]("example-queryable-reduce", createTypeInformation[(Long, Long)])

    while (true) {
      key = (key + 1) % 10
      val future = client.getKvState(jobId, queryableStateName, key, keyTypeInfo, stateDescriptor)
      val state = future.get()
      println(state.value())
    }
  }
}