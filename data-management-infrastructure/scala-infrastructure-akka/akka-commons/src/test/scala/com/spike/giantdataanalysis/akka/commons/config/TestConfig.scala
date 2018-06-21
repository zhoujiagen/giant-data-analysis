package com.spike.giantdataanalysis.akka.commons.config

import com.typesafe.config.ConfigFactory
import org.scalatest.{FunSpec, Matchers}

class ConfigSpec extends FunSpec with Matchers {

  describe("Basic usage of Typesafe Config Library") {
    it("Load default config") {
      println("=== empty config")
      val emptyConfig = ConfigFactory.empty();
      println(emptyConfig)

      // stack
      println("=== overrides")
      println(ConfigFactory.defaultOverrides()) // 系统属性
      println("=== application")
      println(ConfigFactory.defaultApplication()) // 应用设置
      println("=== reference")
      println(ConfigFactory.defaultReference()) // reference.conf

      println("=== all")
      println(ConfigFactory.load()) // 加载stack
    }
  }

}
