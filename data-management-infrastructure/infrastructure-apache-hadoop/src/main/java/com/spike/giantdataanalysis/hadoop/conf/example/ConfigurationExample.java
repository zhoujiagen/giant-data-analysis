package com.spike.giantdataanalysis.hadoop.conf.example;

import org.apache.hadoop.conf.Configuration;

import com.spike.giantdataanalysis.hadoop.support.Hadoops;

/**
 * 配置示例
 * @author zhoujiagen
 */
public class ConfigurationExample {
  public static void main(String[] args) {
    // false: 不加载默认的配置
    Configuration conf = new Configuration(false);
    conf.addResource("conf/configuration-1.xml");

    Hadoops.RENDER(conf);

    // 多个配置文件组合
    // 后加载的配置文件中属性值会覆盖之前加载的配置文件中属性值
    // 覆盖final的属性会抛出警告, 见weight
    conf.addResource("conf/configuration-2.xml");

    Hadoops.RENDER(conf);

    System.out.println(conf.get("non-existed"));
    // 带默认值获取
    System.out.println(conf.get("non-existed", "NULL"));

    // 系统属性
    // 具有最高优先级
    System.setProperty("size", "14");
    Hadoops.RENDER(conf);
    // 仅在变量扩展中有效, 不影响原有属性值
    System.out.println(conf.getInt("size", -1) == 12);

    // 编程方式设置属性值
    conf.set("size", "15");
    System.out.println(conf.getInt("size", -1) == 15);
    // 不影响变量扩展
    System.out.println(conf.get("size-weight", "NULL").equals("14,heavy"));

    // 系统属性在配置文件中不存在时无效
    System.setProperty("non-existed", "NULL");
    System.out.println(conf.get("non-existed") == null);
  }
}
