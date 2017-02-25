package com.spike.giantdataanalysis.hadoop.support;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public final class Hadoops {

  public static final String HADOOP_HOME_VALUE = "/Users/jiedong/software/hadoop-2.7.2";

  public static final String HADOOP_HOME_PROP_NAME = "hadoop.home.dir";
  public static final String HADOOP_HOME_ENV_NAME = "HADOOP_HOME";

  /**
   * 设置系统/环境属性
   */
  public static void SETUP_ENV() {
    String home = System.getProperty(HADOOP_HOME_PROP_NAME);
    if (home == null) {
      System.setProperty(HADOOP_HOME_PROP_NAME, HADOOP_HOME_VALUE);
      // home = System.getenv(HADOOP_HOME_ENV_NAME);
    }
  }

  /**
   * 输出配置内容
   * @param conf
   */
  public static void RENDER(Configuration conf) {
    if (conf == null) return;

    StringBuilder sb = new StringBuilder();
    sb.append(StringUtils.repeat("=", 50) + "\n");
    sb.append("Configuration\n");
    sb.append(StringUtils.repeat("-", 50) + "\n");

    // 这种方式变量不会展开
    // Iterator<Entry<String, String>> it = conf.iterator();
    // Entry<String, String> elem;
    // while (it.hasNext()) {
    // elem = it.next();
    // sb.append(elem.getKey() + "=" + elem.getValue() + "\n");
    // }

    // 支持变量展开
    Map<String, String> map = conf.getValByRegex(".*");
    TreeMap<String, String> treeMap = new TreeMap<String, String>(map);
    for (String key : treeMap.keySet()) {
      sb.append(key + "=" + treeMap.get(key) + "\n");
    }

    sb.append(StringUtils.repeat("=", 50) + "\n");

    System.out.print(sb.toString());
  }

  /**
   * 删除路径
   * @param conf
   * @param path
   * @param recursive 是否递归删除
   * @throws IOException
   * @see {@link FileSystem#get(Configuration)}
   */
  public static void DELETE(Configuration conf, Path path, boolean recursive) throws IOException {
    if (conf == null || path == null) return;

    FileSystem fs = FileSystem.get(conf);
    fs.delete(path, recursive);
  }

  /**
   * 删除本地文件路径
   * @param conf
   * @param path
   * @param recursive 是否递归删除
   * @throws IOException
   * @see {@link FileSystem#getLocal(Configuration)}
   */
  public static void DELETE_LOCAL(Configuration conf, Path path, boolean recursive)
      throws IOException {
    if (conf == null || path == null) return;

    FileSystem fs = FileSystem.getLocal(conf);
    fs.delete(path, recursive);
  }
}
