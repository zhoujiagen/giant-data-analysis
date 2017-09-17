package com.spike.giantdataanalysis.jena.owl;

import java.util.List;

import org.apache.jena.ontology.AllValuesFromRestriction;
import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.UnionClass;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.XSD;

/**
 * Jena OWL API示例.
 * @author zhoujiagen
 */
public class ExampleJenaOWL {
  public static final String DEFAULT_LANG = "en";

  public static void main(String[] args) {
    // initESWCOntologyFromFile();

    initESWCOntologyManually();
  }

  // 手动创建ESWC2006本体
  static void initESWCOntologyManually() {
    // 创建本体模型
    OntModelSpec modelSpec = OntModelSpec.OWL_MEM;
    OntDocumentManager docManager = OntDocumentManager.getInstance();// 本体文档管理器
    docManager.setCacheModels(true); // 设置策略
    docManager.setProcessImports(true);
    docManager.addAltEntry(// 直接读取本地的文档
      "http://www.eswc2006.org/technologies/ontology", //
      "file:" + System.getProperty("user.dir") + "/data/eswc2006.rdf");
    docManager.addAltEntry("http://purl.org/dc/elements/1.1/",//
      "file:" + System.getProperty("user.dir") + "/data/dcelements.rdf");
    modelSpec.setDocumentManager(docManager);
    OntModel ontModel = ModelFactory.createOntologyModel(modelSpec);

    // http://dublincore.org/schemas/rdfs/
    String prefix_dc = "http://purl.org/dc/elements/1.1/";
    // NOT MAINTAIN
    String prefix_wordnet = "http://xmlns.com/wordnet/1.6/";

    ontModel.read(prefix_dc);

    String NS = "http://www.eswc2006.org/technologies/ontology#";

    // (1) 类
    OntClass Artefact = ontModel.createClass(NS + "Artefact");
    Artefact.addLabel("Artefact", DEFAULT_LANG);
    Artefact.addSuperClass(ontModel.getResource(prefix_wordnet + "Document"));

    OntClass Programme = ontModel.createClass(NS + "Programme");
    Programme.addLabel("Programme", DEFAULT_LANG);
    Programme.addSuperClass(Artefact); // 声明父类
    OntClass Proceedings = ontModel.createClass(NS + "Proceedings");
    Proceedings.addLabel("Proceedings", DEFAULT_LANG);
    Proceedings.addSuperClass(Artefact);
    OntClass SlideSet = ontModel.createClass(NS + "SlideSet");
    SlideSet.addLabel("Slide Set", DEFAULT_LANG);
    SlideSet.addSuperClass(Artefact);
    OntClass Poster = ontModel.createClass(NS + "Poster");
    Poster.addLabel("Poster", DEFAULT_LANG);
    Poster.addSuperClass(Artefact);
    OntClass Paper = ontModel.createClass(NS + "Paper");
    Paper.addLabel("Paper", DEFAULT_LANG);
    Paper.addSuperClass(Artefact);

    List<OntClass> subclasses = Artefact.listSubClasses().toList(); // 获取子类
    for (OntClass subclass : subclasses) {
      System.out.println(subclass.getLocalName());
    }

    // (2) 对象属性
    OntClass OrganizedEvent = ontModel.createClass(NS + "OrganizedEvent");
    ObjectProperty hasProgramme = ontModel.createObjectProperty(NS + "hasProgramme");
    hasProgramme.addLabel("has programme", DEFAULT_LANG);
    hasProgramme.addDomain(OrganizedEvent);
    hasProgramme.addRange(Programme);

    // (3) 数值属性
    OntClass Call = ontModel.createClass(NS + "Call");
    DatatypeProperty hasSubmissionDeadline =
        ontModel.createDatatypeProperty(NS + "hasSubmissionDeadline");
    hasSubmissionDeadline.addDomain(Call);
    hasSubmissionDeadline.addRange(XSD.dateTime);

    // (4) 复杂的类表示, 这里本体中没有相关表述, 采用简单的示例
    // value restriction
    OntClass D = ontModel.createClass(NS + "D");
    OntClass R = ontModel.createClass(NS + "R");
    OntClass C = ontModel.createClass(NS + "C");
    ObjectProperty P = ontModel.createObjectProperty(NS + "P");
    AllValuesFromRestriction restriction1 = ontModel.createAllValuesFromRestriction(null, P, R);
    System.out.println(restriction1.getAllValuesFrom());
    CardinalityRestriction restriction2 = ontModel.createCardinalityRestriction(null, P, 2);
    System.out.println(restriction2.getCardinality());
    OntClass REST2 = ontModel.createClass(NS + "REST2");
    REST2.addEquivalentClass(restriction2);
    System.out.println(REST2.isRestriction());
    List<OntClass> E_REST2 = REST2.listEquivalentClasses().toList();
    for (OntClass eqv : E_REST2) {
      if (eqv.isRestriction()) { // 检测转换
        if (eqv.asRestriction().isCardinalityRestriction()) {
          CardinalityRestriction rest = eqv.asRestriction().asCardinalityRestriction();
          System.out.println(rest.getOnProperty());
          System.out.println(rest.getCardinality());
        }
      }
    }

    // boolean union class
    RDFList members = ontModel.createList();
    members = members.cons(C);
    members = members.cons(D);
    UnionClass E = ontModel.createUnionClass(NS + "E", members);
    System.out.println(E.getOperands().asJavaList());
  }

  // 从文件加载ESWC2006本体
  static void initESWCOntologyFromFile() {
    // 创建本体模型
    OntModelSpec modelSpec = OntModelSpec.OWL_MEM;
    OntDocumentManager docManager = OntDocumentManager.getInstance();// 本体文档管理器
    docManager.setCacheModels(true); // 设置策略
    docManager.setProcessImports(true);
    docManager.addAltEntry(// 直接读取本地的文档
      "http://www.eswc2006.org/technologies/ontology", //
      "file:" + System.getProperty("user.dir") + "/data/eswc2006.rdf");
    modelSpec.setDocumentManager(docManager);
    OntModel ontModel = ModelFactory.createOntologyModel(modelSpec);

    // 读取模型数据
    String source = "http://www.eswc2006.org/technologies/ontology";
    String NS = source + "#"; // 命名空间
    ontModel.read(source, "RDF/XML");

    // 获取基础RDF模型
    // Model baseModel = ontModel.getBaseModel();
    // baseModel.write(System.out, "N3");

    // 执行推理
    OntModel infOntModel =
        ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, ontModel);

    // 添加断言
    OntClass paperOntClass = ontModel.getOntClass(NS + "Paper");
    Individual paper1 = paperOntClass.createIndividual(NS + "paper1");
    List<Resource> classes = paper1.listRDFTypes(false).toList();
    if (classes != null && classes.size() > 0) {
      for (Resource r : classes) {
        System.out.println(r.as(OntClass.class).getLocalName() + ": " + r.getURI());
      }
    } else {
      System.out.println("none");
    }
    System.out.println();

    // 获取推理结果
    paper1 = infOntModel.getIndividual(NS + "paper1");
    classes = paper1.listRDFTypes(false).toList();
    if (classes != null && classes.size() > 0) {
      for (Resource r : classes) {
        System.out.println(r.as(OntClass.class).getLocalName() + ": " + r.getURI());
      }
    } else {
      System.out.println("none");
    }
  }
}
