package com.spike.giantdataanalysis.flink.example.streaming.operator

import com.spike.giantdataanalysis.flink.example.streaming.localStreamEnv
import org.apache.flink.streaming.api.scala._

// demonstration of iteration in stream
object ExampleStreamingIteration {

  def main(args: Array[String]): Unit = {
    val env = localStreamEnv

    val someIntegers: DataStream[Long] = env.generateSequence(0, 1000)

    val iteratedStream = someIntegers.iterate(
      //stepFunction: DataStream[T] => (DataStream[T], DataStream[R]), maxWaitTimeMillis:Long = 0
      // stepFunction: initialStream => (feedback, output)
      iteration => {
        val minusOne = iteration.map(v => v - 1)
        val stillGreaterThanZero = minusOne.filter(_ > 0)
        val lessThanZero = minusOne.filter(_ <= 0)
        (stillGreaterThanZero, lessThanZero)
      }
    )

    iteratedStream.print()

    env.execute()
  }
}
