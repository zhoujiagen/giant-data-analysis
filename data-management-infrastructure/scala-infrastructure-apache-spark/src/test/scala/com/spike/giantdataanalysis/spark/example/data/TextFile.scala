package com.spike.giantdataanalysis.spark.example.data

import com.spike.giantdataanalysis.spark.support.Datasets

object TextFile {
  def main(args : Array[String]) : Unit = {
    val inputFile = Datasets.DATA_DIR + "sonnets.txt"
    val input = sc.textFile(inputFile)

    input.take(20).foreach(println)

    sc.stop()
  }
}