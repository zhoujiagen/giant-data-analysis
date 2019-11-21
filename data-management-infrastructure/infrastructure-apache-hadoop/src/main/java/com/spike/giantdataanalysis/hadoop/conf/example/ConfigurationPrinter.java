package com.spike.giantdataanalysis.hadoop.conf.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * <pre>
 * 输出配置内容
 * 
 * {@link Tool}内部使用{@link GenericOptionsParser};
 * {@link ToolRunner}执行{@link Tool}.
 * </pre>
 * 
 * @author zhoujiagen
 * @see Tool
 */
public class ConfigurationPrinter extends Configured implements Tool {

  // 加载配置文件
  static {
    // 文件必须在类路径中
    Configuration.addDefaultResource("hdfs-default.xml");
    Configuration.addDefaultResource("hdfs-site.xml");
    Configuration.addDefaultResource("yarn-default.xml");
    Configuration.addDefaultResource("yarn-site.xml");
    Configuration.addDefaultResource("mapred-default.xml");
    Configuration.addDefaultResource("mapred-site.xml");
  }

  public static void main(String[] args) throws Exception {
    Hadoops.SETUP_ENV();

    Tool tool = new ConfigurationPrinter();
    System.exit(ToolRunner.run(tool, args));
  }

  @Override
  public int run(String[] args) throws Exception {
    Configuration conf = super.getConf();
    Hadoops.RENDER(conf);
    return 0;
  }

}
