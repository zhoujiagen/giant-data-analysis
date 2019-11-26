package com.spike.giantdataanalysis.spark

/**
 * 样例的两种运行方式:
 *
 * (1) 提交任务
 * 见scripts目录下的*.sh
 *
 * (2) 本地运行
 * val conf = new SparkConf().setMaster("local").setAppName("<appname>")
 * val sc = new SparkContext(conf)
 *
 * WARNING: NUST COMPILE WITH SBT IN THE OS SHELL!!!
 * e.g. > ~compile
 */
package object example {
  /**数据目录*/
  val DATA_DIR: String = System.getProperty("user.dir") + "/../datasets/"
}