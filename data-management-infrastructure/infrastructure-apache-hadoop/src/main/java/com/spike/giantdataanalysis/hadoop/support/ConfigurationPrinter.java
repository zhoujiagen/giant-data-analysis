package com.spike.giantdataanalysis.hadoop.support;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

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

  public static void main(String[] args) throws Exception {
    Hadoops.setUpEnvironment();

    Tool tool = new ConfigurationPrinter();
    System.exit(ToolRunner.run(tool, args));
  }

  @Override
  public int run(String[] args) throws Exception {
    Configuration conf = super.getConf();
    Hadoops.output(conf);
    return 0;
  }

}
