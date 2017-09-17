package com.spike.giantdataanalysis.jena.rdf;

import java.io.InputStream;

import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;

import com.spike.giantdataanalysis.jena.supports.JenaModels;

/**
 * Jena RDF API示例
 * @author zhoujiagen
 */
public class ExampleJenaRDF {
  // 命名空间
  public static final String NS_URI = "http://gda.jena.spike.com/";

  public static void main(String[] args) {
    // 创建默认模型
    Model model = ModelFactory.createDefaultModel();

    // 创建资源
    Resource ericcartman = model.createResource(NS_URI + "EricCartman")//
        .addProperty(VCARD.FN, "Eric")//
        .addProperty(VCARD.N, model.createResource()//
            .addProperty(VCARD.Given, "Eric")//
            .addProperty(VCARD.Family, "Cartman")//
        );
    System.out.println("Resource: " + ericcartman);

    // 遍历语句
    // iter_statement(model);

    // 输出模型
    // output(model);

    // 从文件加载模型
    // load(model);

    // 命名空间前缀
    // namespace_prefix(model);

    // 模型中导航
    // navigate(model);

    // 模型查询
    // query(model);

    // 模型的并, 交和差
    // operate();

    // 容器: bag(无序), alt(无序, 备选), seq(有序)
    // container();

    // 字面量
    literal();
  }

  // 字面量
  static void literal() {
    Model model = ModelFactory.createDefaultModel();

    model.createResource()//
        .addProperty(RDFS.label, model.createLiteral("chat", "en"))//
        .addProperty(RDFS.label, model.createLiteral("chat", "fr"))//
        .addProperty(RDFS.label, model.createLiteral("<em>chat</em>", true));

    model.write(System.out, "N-TRIPLE");
  }

  // 容器: bag(无序), alt(无序, 备选), seq(有序)
  static void container() {
    Model model = ModelFactory.createDefaultModel();

    model
        .createResource(NS_URI + "BeckySmith")
        .addProperty(VCARD.FN, "Becky Smith")
        .addProperty(
          VCARD.N,
          model.createResource().addProperty(VCARD.Given, "Becky")
              .addProperty(VCARD.Family, "Smith"));

    model
        .createResource(NS_URI + "JohnSmith")
        .addProperty(VCARD.FN, "John Smith")
        .addProperty(
          VCARD.N,
          model.createResource().addProperty(VCARD.Given, "John")
              .addProperty(VCARD.Family, "Smith"));

    // 创建bag
    Bag smiths = model.createBag();

    StmtIterator iter = model.listStatements(new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
      public boolean selects(Statement s) {
        return s.getString().endsWith("Smith");
      }
    });

    while (iter.hasNext()) {
      smiths.add(iter.nextStatement().getSubject());// 添加到bag中
    }

    model.write(System.out, "N-TRIPLE");

    // 遍历bag
    NodeIterator nodeIterator = smiths.iterator();
    if (nodeIterator.hasNext()) {
      while (nodeIterator.hasNext()) {
        System.out.println(nodeIterator.next().asResource().getProperty(VCARD.FN).getLiteral());
      }
    } else {
      System.out.println("Bag smiths is empty.");
    }
  }

  // 模型的并, 交和差
  static void operate() {
    Model model1 = ModelFactory.createDefaultModel();
    Model model2 = ModelFactory.createDefaultModel();
    model1
        .createResource(NS_URI + "JohnSmith")
        //
        .addProperty(
          VCARD.N,
          model1.createResource().addProperty(VCARD.Given, "John")
              .addProperty(VCARD.Family, "Smith"))//
        .addProperty(VCARD.FN, "John Smith");

    model2
        .createResource(NS_URI + "JohnSmith")
        //
        .addProperty(
          VCARD.EMAIL,
          model2.createResource().addProperty(RDF.type, VCARD.Other)
              .addLiteral(RDF.value, "john@exmaple.com"))//
        .addProperty(VCARD.FN, "John Smith");

    // 合并
    Model model = model1.union(model2);
    model.write(System.out, "N-TRIPLE");
  }

  // 模型查询
  static void query(Model model) {
    // 查询具有属性的主题
    ResIterator iter = model.listSubjectsWithProperty(VCARD.N);
    while (iter.hasNext()) {
      System.out.println(iter.next());
    }

    // 使用选择器
    SimpleSelector selector = new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
      @Override
      public boolean selects(Statement s) {
        return s.getObject().asLiteral().toString().endsWith("Eric");
      }
    };
    StmtIterator stmtIterator = model.listStatements(selector);
    while (stmtIterator.hasNext()) {
      System.out.println(JenaModels.asString(stmtIterator.next()));
    }
  }

  // 模型中导航
  static void navigate(Model model) {
    Resource resource = model.getResource(NS_URI + "EricCartman");
    System.out.println(resource.toString());

    Resource N = (Resource) resource.getProperty(VCARD.N).getObject();
    System.out.println(N);

    // 获取字面量
    String givenName = N.getProperty(VCARD.Given).getString();
    System.out.println(givenName);

    // 允许重复属性
    resource.addProperty(VCARD.NICKNAME, "Eric")//
        .addProperty(VCARD.NICKNAME, "Cartman");

    StmtIterator iter = resource.listProperties(VCARD.NICKNAME);
    while (iter.hasNext()) {
      System.out.println(iter.next().getObject().asLiteral());
    }
  }

  // 遍历语句
  // 语句(statement): 主谓宾(subject, predicate, object)
  static void iter_statement(Model model) {
    StmtIterator iter = model.listStatements();
    while (iter.hasNext()) {
      Statement stmt = iter.nextStatement();
      System.out.println(JenaModels.asString(stmt));
    }
  }

  // 输出模型, 格式:
  // Predefined values are "RDF/XML", "RDF/XML-ABBREV", "N-TRIPLE" and "N3".
  // The default value is represented by null is "RDF/XML".
  static void output(Model model) {
    model.write(System.out, "N-TRIPLE");
  }

  // 从文件加载模型
  static void load(Model model) {
    String filename = System.getProperty("user.dir") + "/data/sample.rdf";
    System.out.println(filename);
    InputStream is = FileManager.get().open(filename);
    model.read(is, null);
    model.write(System.out, "N-TRIPLE");
  }

  // 命名空间前缀
  static void namespace_prefix(Model model) {
    String nsA = "http://gda.jena.spike.com/nsA#";
    String nsB = "http://gda.jena.spike.com/nsB#";
    Resource root = model.createResource(nsA + "root");
    Property P = model.createProperty(nsA + "P");
    Property Q = model.createProperty(nsB + "Q");
    Resource x = model.createResource(nsA + "x");
    Resource y = model.createResource(nsA + "y");
    Resource z = model.createResource(nsA + "z");
    model.add(root, P, x).add(root, P, y).add(y, Q, z);
    System.out.println("# ========================== no special prefixes defined");
    /** xmlns:j.0="http://gda.jena.spike.com/nsA#" xmlns:j.1="http://gda.jena.spike.com/nsB#" */
    model.write(System.out);
    System.out.println("# ========================== nsA defined");
    /** xmlns:nsA="http://gda.jena.spike.com/nsA#" xmlns:j.0="http://gda.jena.spike.com/nsB#" */
    model.setNsPrefix("nsA", nsA);
    model.write(System.out);
    System.out.println("# ========================== nsA and cat defined");
    /** xmlns:nsA="http://gda.jena.spike.com/nsA#" xmlns:cat="http://gda.jena.spike.com/nsB#" */
    model.setNsPrefix("cat", nsB);
    model.write(System.out);
  }

}
