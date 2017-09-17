package com.spike.giantdataanalysis.jena.supports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.jena.ext.com.google.common.base.Preconditions;
// import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.ProfileRegistry;
import org.apache.jena.rdf.model.Alt;
import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Seq;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.ValidityReport.Report;

public class JenaModels {

  /** @return RDF模型 */
  public static Model newModel() {
    return ModelFactory.createDefaultModel();
  }

  /** @return RDFS模型 */
  public static InfModel newInfModel(Model model) {
    return ModelFactory.createRDFSModel(model);
  }

  /**
   * @return OWL模型, 默认为OWL Full, 内存存储, RDFS推理能力.
   * @see ProfileRegistry.OWL_LANG
   * @see OntModelSpec.OWL_MEM
   */
  public static OntModel newOntModel() {
    return ModelFactory.createOntologyModel();
  }

  public static OntModel newOntModel(OntModelSpec modelSpec) {
    return ModelFactory.createOntologyModel(modelSpec);
  }

  /** 模型的并 */
  public static Model union(Model model1, Model model2) {
    Preconditions.checkArgument(model1 != null, "Argument model1 should not be null!");
    Preconditions.checkArgument(model2 != null, "Argument model2 should not be null!");
    return model1.union(model2);
  }

  /** 模型的交 */
  public static Model intersection(Model model1, Model model2) {
    Preconditions.checkArgument(model1 != null, "Argument model1 should not be null!");
    Preconditions.checkArgument(model2 != null, "Argument model2 should not be null!");
    return model1.intersection(model2);
  }

  /** 模型的差 */
  public static Model difference(Model model1, Model model2) {
    Preconditions.checkArgument(model1 != null, "Argument model1 should not be null!");
    Preconditions.checkArgument(model2 != null, "Argument model2 should not be null!");
    return model1.difference(model2);
  }

  /** 创建{@link Resource} */
  public static Resource newResource(Model model, String uri) {
    Preconditions.checkArgument(model != null, "Argument model should not be null!");
    Preconditions.checkArgument(uri != null, "Argument uri should not be null!");
    return model.createResource(uri);
  }

  /** 创建{@link Bag}容器 */
  public static Bag newBag(Model model) {
    Preconditions.checkArgument(model != null, "Argument model should not be null!");
    Bag bag = model.createBag();
    return bag;
  }

  /** 创建{@link Seq}容器 */
  public static Seq newSeq(Model model) {
    Preconditions.checkArgument(model != null, "Argument model should not be null!");
    Seq seq = model.createSeq();
    return seq;
  }

  /** 创建{@link Alt}容器 */
  public static Alt newAlt(Model model) {
    Preconditions.checkArgument(model != null, "Argument model should not be null!");
    Alt alt = model.createAlt();
    return alt;
  }

  /** 显示{@link Statement} @see org.apache.jena.util.PrintUtil */
  public static String asString(Statement stmt) {
    Preconditions.checkArgument(stmt != null, "Argument stmt should not be null!");

    StringBuilder sb = new StringBuilder();
    Resource subject = stmt.getSubject();
    Property predicate = stmt.getPredicate();
    RDFNode object = stmt.getObject();

    sb.append(subject.toString()).append("\t");
    sb.append(predicate.toString()).append("\t");
    if (object.isResource()) {
      sb.append(object.toString()).append("\t");
    } else if (object.isLiteral()) {
      sb.append("\"" + object.toString() + "\"").append("\t");
    } else {
      sb.append(object.toString()).append("\t");
    }

    return sb.toString();
  }

  /** 显示模型中匹配的语句 */
  public static String asString(Model model, Resource subject, Property predicate, Resource object) {
    Preconditions.checkArgument(model != null, "Argument model should not be null!");

    StringBuilder sb = new StringBuilder();
    for (StmtIterator i = model.listStatements(subject, predicate, object); i.hasNext();) {
      Statement stmt = i.nextStatement();
      sb.append(asString(stmt) + "\n");
    }
    return sb.toString();
  }

  /** 显式验证模型的结果 */
  public static String validate(InfModel infModel) {
    Preconditions.checkArgument(infModel != null, "Argument infModel should not be null!");

    StringBuilder sb = new StringBuilder();
    ValidityReport validityReport = infModel.validate();
    if (validityReport.isValid()) {
      sb.append("OK\n");
    } else {
      sb.append("Conflicts:\n");
      Iterator<Report> iter = validityReport.getReports();
      while (iter.hasNext()) {
        sb.append("\t" + iter.next() + "\n");
      }
    }
    return sb.toString();
  }

  /**
   * 转换为Graphviz的dot定义.
   * <p>
   * Well, subClass/type/domain/range/label edges are noisy.
   */
  public static String asGraphviz(Model model, String name, String title) {
    Preconditions.checkArgument(model != null, "Argument model should not be null!");

    if (name == null || "".equals(name.trim())) name = "jena";
    if (title == null || "".equals(title.trim())) title = "jena grapviz presentation";

    StringBuilder sb = new StringBuilder();
    Set<String> nodes = new HashSet<>();
    Set<String> edgLabels = new HashSet<>();
    // List<Triple> triples = new ArrayList<>();
    List<Triple<String, String, String>> triples = new ArrayList<>();
    for (StmtIterator i = model.listStatements(); i.hasNext();) {
      Statement stmt = i.nextStatement();

      Resource subject = stmt.getSubject();
      String subjectStr = fix(subject.getLocalName());
      if ("".equals(subjectStr)) {
        subjectStr = "thisOntology";
      }
      nodes.add(subjectStr);

      Property property = stmt.getPredicate();
      edgLabels.add(fix(property.getLocalName()));

      RDFNode object = stmt.getObject();
      String objectStr = null;
      if (object.isResource()) {
        objectStr = fix(object.asResource().getLocalName());
        nodes.add(objectStr);
      } else if (object.isLiteral()) {
        objectStr = fix(object.asLiteral().toString());
      } else {
        objectStr = fix(object.asNode().getName());
        nodes.add(objectStr);
      }

      // triples.add(new Triple(subject.asNode(), property.asNode(), object.asNode()));
      triples.add(ImmutableTriple.<String, String, String> of(subjectStr,
        fix(property.getLocalName()), objectStr));
    }

    sb.append("digraph " + name + " {\n");
    sb.append("\n");
    sb.append("\n");
    sb.append("\n");
    sb.append("  /********************************************************\n");
    sb.append("  * 标题\n");
    sb.append("  ********************************************************/\n");
    sb.append("  labelloc=t label=\"" + title + "\"\n");
    sb.append("  \n");
    sb.append("  /********************************************************\n");
    sb.append("  * 全局属性\n");
    sb.append("  ********************************************************/\n");
    sb.append("  rankdir=LR\n");
    sb.append("  splines=true\n");
    sb.append("  node [color=skyblue shape=box style=filled]\n");
    sb.append("\n");
    sb.append("  /* 节点 */\n");
    for (String node : nodes) {
      sb.append("  " + node + "\n");
    }
    sb.append("\n");
    sb.append("  /* 边 */ \n");
    for (Triple<String, String, String> triple : triples) {
      sb.append("  " + triple.getLeft() + " -> " + triple.getMiddle() //
          + " [label=\"" + triple.getRight() + "\"]\n");
    }

    sb.append("  \n");
    sb.append("}  ");

    return sb.toString();
  }

  private static String fix(String source) {
    if (source == null) return "";
    return source.replaceAll("\n", "").replaceAll("-", "");
  }

  // for test
  public static void main(String[] args) {
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
    ontModel.read(source, "RDF/XML");

    System.out.println(asGraphviz(ontModel, "", ""));
  }
}
