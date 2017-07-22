package com.spike.giantdataanalysis.hbase.support;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class HBaseConfigurations {
  /**
   * <pre>
   * 加载默认配置.
   * 
   * 副作用: 生成最终的配置文件, 用于检验.
   * source元素值表示配置来源文件
   * </pre>
   * @throws IOException
   */
  public static Configuration loadDefaultConfiguration() throws IOException {
    Configuration conf = HBaseConfiguration.create();
    // 从类路径加载资源
    conf.addResource("conf/hbase-site.xml");

    String outputPath =
        System.getProperty("user.dir") + "/src/main/resources/conf/hbase-conf-final.xml";
    try (OutputStream os = new FileOutputStream(outputPath);) {
      conf.writeXml(os);
    }

    return conf;
  }
}
