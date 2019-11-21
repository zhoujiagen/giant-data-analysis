package com.spike.giantdataanalysis.hadoop.support;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

/**
 * <pre>
 * Hadoop配置文件解析类
 * 
 * 参考：
 * http://my.oschina.net/jack230230/blog/57171?fromerr=V4OBdFtr
 * https://commons.apache.org/proper/commons-configuration/userguide_v1.10/user_guide.html
 * </pre>
 * 
 * @author zhoujiagen
 */
public class HadoopConfigXMLFileParser {

  public static final String NAME_KEY = "name";
  public static final String VALUE_KEY = "value";
  public static final String DESCRIPTION_KEY = "description";

  public static void main(String[] args) throws ConfigurationException {
    String dir = System.getProperty("user.dir") + "/src/main/resources/";
    // dir += "hadoop1/conf/"; // V1
    // dir += "hadoop2/etc/hadoop/"; // V2
    // dir += "hadoop2/_default/";// V2 default
    dir += "hbase1.2.6/";

    // String fileName = dir + "core-site.xml";
    // String fileName = dir + "hdfs-site.xml";
    // String fileName = dir + "mapred-site.xml";
    // String fileName = dir + "yarn-site.xml";
    // String fileName = dir + "hdfs-default.xml";
    String fileName = dir + "hbase-default.xml";

    XMLConfiguration conf = new XMLConfiguration(fileName);

    // Iterator<?> keys = conf.getKeys();
    // while (keys.hasNext()) {
    // Object key = keys.next();
    // System.out.println(conf.getString(key.toString()));
    // }

    // conf.setExpressionEngine(new XPathExpressionEngine());

    ConfigurationNode rootNode = conf.getRootNode();

    int childCount = rootNode.getChildrenCount();
    for (int i = 0; i < childCount; i++) {
      ConfigurationNode childNode = rootNode.getChild(i);

      int subChildCount = childNode.getChildrenCount();

      String nameValue = null;
      StringBuilder valueSb = new StringBuilder();
      StringBuilder descriptionSb = new StringBuilder();
      for (int j = 0; j < subChildCount; j++) {
        ConfigurationNode subChildNode = childNode.getChild(j);

        if (NAME_KEY.equals(subChildNode.getName())) {

          nameValue = subChildNode.getValue().toString();

        } else if (VALUE_KEY.equals(subChildNode.getName())) {

          valueSb.append(subChildNode.getValue() + ",");// 应对多个值的情况

        } else if (DESCRIPTION_KEY.equals(subChildNode.getName())) {

          descriptionSb.append(subChildNode.getValue() + " ");
        }

      }
      // 自定义输出
      System.out.println(nameValue.replaceAll("\\.", "_").replaceAll("\\-", "_"));
      System.out.println("(");
      System.out.println("\"" + nameValue + "\",");

      String valueValue = valueSb.toString();
      if (valueValue.length() > 0) {
        valueValue = valueValue.substring(0, valueValue.length() - 1);
      }
      System.out.println("\"" + valueValue + "\",");

      String descriptionValue = descriptionSb.toString();
      descriptionValue = descriptionValue.replaceAll(System.lineSeparator(), "")
          .replaceAll("\\s+", " ").replaceAll("\"", "");
      System.out.println("\"" + descriptionValue + "\"");

      if (i == childCount - 1) {
        System.out.println(");");
      } else {
        System.out.println("),");
      }

      System.out.println("");
    }
  }
}
