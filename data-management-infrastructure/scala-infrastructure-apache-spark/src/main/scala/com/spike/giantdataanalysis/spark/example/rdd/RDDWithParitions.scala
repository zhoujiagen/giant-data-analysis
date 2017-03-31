package com.spike.giantdataanalysis.spark.example.rdd

import org.apache.hadoop.io.IntWritable
import org.apache.spark.rdd.RDD.rddToSequenceFileRDDFunctions

import com.spike.giantdataanalysis.spark.support.Datasets
import com.google.common.collect.Lists
import org.apache.spark.HashPartitioner

//object writables {
//  //自定义Writable实现
//  @SerialVersionUID(-1L)
//  class UserID(@BeanProperty var id: Int) extends Writable with Serializable {
//    def this() = {
//      this(-1)
//    }
//    def readFields(in: java.io.DataInput): Unit = new IntWritable(id).readFields(in)
//    def write(out: java.io.DataOutput): Unit = new IntWritable(id).write(out)
//  }
//  @SerialVersionUID(-1L)
//  class UserInfo(@BeanProperty var id: Int, @BeanProperty var subscribeTopics: List[String]) extends Writable with Serializable {
//    private var idIntWritable: IntWritable = _
//    private var subscribeTopicsTexts: List[Text] = _
//    def this() = {
//      this(-1, List.empty)
//    }
//
//    def readFields(in: java.io.DataInput): Unit = {
//      new IntWritable(id).readFields(in)
//      subscribeTopics.map(topic ⇒ new Text(topic)).foreach(_.readFields(in))
//    }
//    def write(out: java.io.DataOutput): Unit = {
//      new IntWritable(id).write(out)
//      subscribeTopics.map(topic ⇒ new Text(topic)).foreach(_.write(out))
//    }
//  }
//  @SerialVersionUID(-1L)
//  class LinkInfo(@BeanProperty var urls: List[String]) extends Writable with Serializable {
//    private var urlsTexts: List[Text] = _
//    def this() = {
//      this(List.empty)
//    }
//
//    def readFields(in: java.io.DataInput): Unit = urls.map(new Text(_)).foreach(_.readFields(in))
//    def write(out: java.io.DataOutput): Unit = urls.map(new Text(_)).foreach(_.write(out))
//  }
//}

object RDDWithParitions {
  //import writables._

  def peapareData() : Unit = {
    // large
    val userId_userInfo = sc.parallelize(
      List(
        (new UserID(1), new UserInfo(1, Lists.newArrayList("topic-a", "topic-b"))),
        (new UserID(2), new UserInfo(2, Lists.newArrayList("topic-c", "topic-d"))),
        (new UserID(3), new UserInfo(3, Lists.newArrayList("topic-a", "topic-e")))
      ))

    // small
    val userId_linkIfo = sc.parallelize(
      List(
        (new UserID(1), new LinkInfo(Lists.newArrayList("www.a.com", "www.b.com"))),
        (new UserID(2), new LinkInfo(Lists.newArrayList("www.a.com", "www.c.com")))
      ))

    val a : IntWritable = null
    userId_userInfo.saveAsSequenceFile(Datasets.OUTPUT_DATA_DIR + "userId_userInfo")
    userId_linkIfo.saveAsSequenceFile(Datasets.OUTPUT_DATA_DIR + "userId_linkIfo")
  }

  def main(args : Array[String]) : Unit = {
    //peapareData()

    // large RDD
    val userId_userInfo = sc.sequenceFile(Datasets.OUTPUT_DATA_DIR + "userId_userInfo/part-00000", classOf[UserID], classOf[UserInfo])
    // small RDD
    val userId_linkIfo = sc.sequenceFile(Datasets.OUTPUT_DATA_DIR + "userId_linkIfo/part-00000", classOf[UserID], classOf[LinkInfo])

    // join: with shuffle to join
    val joined = userId_userInfo.join(userId_linkIfo)
    val count = joined.filter {
      case (userId, (userInfo, linkInfo)) ⇒
        !userInfo.getSubscribeTopics().contains(linkInfo.getUrls())
    }.count()
    println(count)

    // partition large RDD
    val userId_userInfo2 = sc.sequenceFile(Datasets.OUTPUT_DATA_DIR + "userId_userInfo/part-00000", classOf[UserID], classOf[UserInfo])
      .partitionBy(new HashPartitioner(100))
      .persist()
    val count2 = userId_userInfo2.join(userId_linkIfo)
      .filter {
        case (userId, (userInfo, linkInfo)) ⇒
          !userInfo.getSubscribeTopics().contains(linkInfo.getUrls())
      }.count()
    println(count2)
  }
}