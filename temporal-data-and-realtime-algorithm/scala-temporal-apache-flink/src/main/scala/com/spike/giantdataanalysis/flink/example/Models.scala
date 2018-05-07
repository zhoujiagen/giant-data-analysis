package com.spike.giantdataanalysis.flink.example

object Models {
  // Data type for words with count
  case class WordWithCount(word : String, count : Long)

}