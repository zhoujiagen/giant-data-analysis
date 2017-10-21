package com.spike.giantdataanalysis.jena.arq;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.apache.jena.ext.com.google.common.collect.Lists;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class ExampleJenaARQ {

  public static void main(String[] args) {
    Model model = init();

    StringBuilder sb = new StringBuilder();
    sb.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
    sb.append("PREFIX owl: <http://www.w3.org/2002/07/owl#>\n");
    sb.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
    sb.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
    sb.append("PREFIX eswc2006: <http://www.eswc2006.org/technologies/ontology#>\n");
    sb.append("SELECT * \n");
    sb.append("WHERE\n");
    // sb.append("{ ?sub rdfs:subClassOf eswc2006:Artefact }\n");
    sb.append("{ ?sub rdfs:subClassOf ?sup }\n");

    List<String> varNames = Lists.newArrayList("sub", "sup");
    List<Class<?>> classes = Lists.newArrayList(Resource.class, Resource.class);
    List<List<Object>> lists = select(model, sb.toString(), varNames, classes);
    for (String varName : varNames) {
      System.out.print(Strings.padStart(varName, 100, ' '));
    }
    System.out.println();
    for (List<Object> list : lists) {
      for (int i = 0, len = list.size(); i < len; i++) {
        System.out.print(Strings.padStart(list.get(i).toString(), 100, ' '));
      }
      System.out.println();
    }
  }

  /**
   * SELECT查询
   * @param query
   * @see {@link QueryExecution#execSelect()}
   * @see {@link QueryExecution#execConstruct()}
   * @see {@link QueryExecution#execAsk()()}
   * @see {@link QueryExecution#execDescribe()()}
   */
  static List<List<Object>> select(Model model, String queryString, //
      List<String> varNames, List<Class<?>> classes) {
    List<List<Object>> result = new ArrayList<>();

    Query query = QueryFactory.create(queryString);
    try (QueryExecution execution = QueryExecutionFactory.create(query, model);) {
      ResultSet rs = execution.execSelect(); // ~

      // ResultSetFormatter.out(ResultSetFactory.copyResults(rs), query); // 拷贝结果集, 并输出

      int valuesSize = varNames.size();
      while (rs.hasNext()) {

        QuerySolution qs = rs.next();
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < valuesSize; i++) {

          String varName = varNames.get(i);
          Class<?> clazz = classes.get(i);
          if (Resource.class.equals(clazz)) {
            Resource resource = qs.getResource(varName);
            values.add(resource);
          } else if (Literal.class.equals(clazz)) {
            Literal literal = qs.getLiteral(varName);
            values.add(literal);
          } else {
            RDFNode rdfNode = qs.get(varName);
            values.add(rdfNode);
          }
        }
        result.add(values);
      }
    }
    return result;
  }

  static Model init() {
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

    ontModel.read("http://www.eswc2006.org/technologies/ontology", "RDF/XML");
    // ontModel.write(System.out, "N3");

    return ontModel;
  }
}
