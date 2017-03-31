package com.spike.giantdataanalysis.spark

object DummyTest extends App {
  val WORK_DIR = System.getProperty("user.dir")
  val DATA_DIR: String = WORK_DIR + "/../datasets/"
  println(DATA_DIR)
}