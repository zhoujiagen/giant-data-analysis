package com.spike.giantdataanalysis.hadoop.support;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 生成HadoopDefaultConstant.
 * 
 * <p>注意: 需要手动修改生成的文件.
 * </pre>
 * 
 * @author zhoujiagen
 * @see HadoopDefaultConstant
 */
public class HadoopDefaultConstantGenerator {
  private static final Logger LOG = LoggerFactory.getLogger(HadoopDefaultConstantGenerator.class);

  public static final String KEY_NAME = "name";
  public static final String KEY_VALUE = "value";
  public static final String KEY_DESCRIPTION = "description";

  public static final String CONFIG_CORE = "configuration.url.core";
  public static final String CONFIG_MAPREDUCE = "configuration.url.mapreduce";
  public static final String CONFIG_HDFS = "configuration.url.hdfs";
  public static final String CONFIG_YARN = "configuration.url.yarn";

  public static void main(String[] args) {
    StringBuilder sb = new StringBuilder();

    String enumName = "Core";
    XMLConfiguration conf = getConf(CONFIG_CORE);
    sb.append("enum " + enumName + " {").append(System.lineSeparator());
    String parseResult = parseConf(conf);
    sb.append(parseResult).append(System.lineSeparator());
    sb.append(getConstructor(enumName)).append(System.lineSeparator());
    sb.append("}").append(System.lineSeparator());

    enumName = "MapReduce";
    conf = getConf(CONFIG_MAPREDUCE);
    sb.append("enum " + enumName + " {").append(System.lineSeparator());
    parseResult = parseConf(conf);
    sb.append(parseResult).append(System.lineSeparator());
    sb.append(getConstructor(enumName)).append(System.lineSeparator());
    sb.append("}").append(System.lineSeparator());

    enumName = "Hdfs";
    conf = getConf(CONFIG_HDFS);
    sb.append("enum " + enumName + " {").append(System.lineSeparator());
    parseResult = parseConf(conf);
    sb.append(parseResult).append(System.lineSeparator());
    sb.append(getConstructor(enumName)).append(System.lineSeparator());
    sb.append("}").append(System.lineSeparator());

    enumName = "Yarn";
    conf = getConf(CONFIG_YARN);
    sb.append("enum " + enumName + " {").append(System.lineSeparator());
    parseResult = parseConf(conf);
    sb.append(parseResult).append(System.lineSeparator());
    sb.append(getConstructor(enumName)).append(System.lineSeparator());
    sb.append("}").append(System.lineSeparator());

    String className = "HadoopDefaultConstant";
    Path path = Paths.get(className + ".java");
    FileWriter fw = null;
    try {
      fw = new FileWriter(path.toFile());

      fw.append("// generated at " + new Date().getTime());
      fw.write("//@formatter:off\n");
      fw.write("public interface " + className + " {\n");

      fw.write(sb.toString());

      fw.write("}\n");
      fw.close();
    } catch (IOException e) {
      LOG.error("write file failed", e);
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          LOG.error("write file failed", e);
        }
      }
    }
  }

  private static String getConstructor(String enumName) {
    return "   private String key;\n" + //
        "    private String value;\n" + //
        "    private String description;\n" + //
        "\n" + //
        "    private " + enumName + "(String key, String value, String description) {\n" + //
        "      this.key = key;\n" + //
        "      this.value = value;\n" + //
        "      this.description = description;\n" + //
        "    }\n" + //
        "\n" + //
        "    public String key() {\n" + //
        "      return key;\n" + //
        "    }\n" + //
        "\n" + //
        "    public String value() {\n" + //
        "      return value;\n" + //
        "    }\n" + //
        "\n" + //
        "    public String description() {\n" + //
        "      return description;\n" + //
        "    }";
  }

  private static XMLConfiguration getConf(String project) {
    URL fileUrl = null;
    try {
      ResourceBundle rb = ResourceBundle.getBundle(Hadoops.HADOOP_ENV_PB);
      fileUrl = new URL(rb.getString(project));
      return new XMLConfiguration(fileUrl);
    } catch (MalformedURLException e) {
      LOG.error("get conf failed", e);
    } catch (ConfigurationException e) {
      LOG.error("invalid conf", e);
    }
    return null;
  }

  private static String parseConf(XMLConfiguration conf) {
    StringBuilder result = new StringBuilder();

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
        if (KEY_NAME.equals(subChildNode.getName())) {
          nameValue = subChildNode.getValue().toString();
        } else if (KEY_VALUE.equals(subChildNode.getName())) {
          valueSb.append(subChildNode.getValue() + ",");// 应对多个值的情况
        } else if (KEY_DESCRIPTION.equals(subChildNode.getName())) {
          descriptionSb.append(subChildNode.getValue() + " ");
        }
      } // end of sub child

      // 自定义输出
      result.append(nameValue.replaceAll("\\.", "_").replaceAll("\\-", "_"));
      result.append(System.lineSeparator());
      result.append("(");
      result.append(System.lineSeparator());
      result.append("\"" + nameValue + "\",");
      result.append(System.lineSeparator());

      String valueValue = valueSb.toString();
      if (valueValue.length() > 0) {
        valueValue = valueValue.substring(0, valueValue.length() - 1);
      }
      result.append("\"" + valueValue + "\",");
      result.append(System.lineSeparator());

      String descriptionValue = descriptionSb.toString();
      descriptionValue = descriptionValue.replaceAll(System.lineSeparator(), "")
          .replaceAll("\\s+", " ").replaceAll("\"", "");
      result.append("\"" + descriptionValue + "\"");
      result.append(System.lineSeparator());

      if (i == childCount - 1) {
        result.append(");");
        result.append(System.lineSeparator());
      } else {
        result.append("),");
        result.append(System.lineSeparator());
      }

      result.append(System.lineSeparator());
    } // end of child

    return result.toString();
  }

}
