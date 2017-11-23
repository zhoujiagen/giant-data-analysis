package com.spike.giantdataanalysis.commons.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.spike.giantdataanalysis.commons.configuration.bean.Invoice;
import com.spike.giantdataanalysis.commons.configuration.bean.Person;

// REF: https://bitbucket.org/asomov/snakeyaml/wiki/Documentation
public class TestSnakeYaml {
  public static void main(String[] args) throws FileNotFoundException {
    // readFromFile();
    // constructJavaBean();
    dump();
  }

  // 从文件读取
  static void readFromFile() throws FileNotFoundException {

    InputStream is = new FileInputStream(new File("src/test/resources/yaml/single.yaml"));
    Yaml yaml = new Yaml();
    for (Object data : yaml.loadAll(is)) {
      System.out.println("list? " + (data instanceof ArrayList<?>));
      System.out.println("map? " + (data instanceof LinkedHashMap<?, ?>));
      System.out.println(data);
    }

  }

  // 构造对象
  static void constructJavaBean() throws FileNotFoundException {
    String data =
        "--- !!com.spike.giantdataanalysis.commons.configuration.bean.Person\ngiven: Chris\nfamily: Dumars";
    Yaml yaml = new Yaml();
    Object object = yaml.load(data);
    System.out.println(object);
    Person person = (Person) object;
    System.out.println(person);

    InputStream is = new FileInputStream(new File("src/test/resources/yaml/invoice.yaml"));
    yaml = new Yaml(new Constructor(Invoice.class));
    Invoice invoice = yaml.loadAs(is, Invoice.class);
    System.out.println(invoice);
  }

  // 输出
  static void dump() {
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("name", "Silenthand Olleander");
    data.put("race", "Human");
    data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
    Yaml yaml = new Yaml();
    String output = yaml.dump(data);
    System.out.println(output);
  }

}
