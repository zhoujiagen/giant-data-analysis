package com.spike.giantdataanalysis.spark.streaming.example

import com.spike.giantdataanalysis.spark.streaming.support.Datasets

/**
 * use <code>nc -lk 9999</code> for test
 */
object SocketStreamExample {
  def main(args: Array[String]): Unit = {
    val lines = ssc.socketTextStream(Datasets.HOST_NAME_DEFAULT, Datasets.SOCKET_PORT_DEFAULT)

    val words = lines.flatMap(line ⇒ line.split(" "))
    val pairs = words.map(word ⇒ (word, 1))
    val wordCount = pairs.reduceByKey(_ + _)

    wordCount.print()

    ssc.start()
    ssc.awaitTermination()
  }
}