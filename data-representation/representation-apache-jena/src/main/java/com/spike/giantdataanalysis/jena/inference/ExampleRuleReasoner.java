package com.spike.giantdataanalysis.jena.inference;

import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

import com.spike.giantdataanalysis.jena.supports.JenaModels;

public class ExampleRuleReasoner {

  static final String NS = "http://spike.com/rule/";

  public static void main(String[] args) {
    // 准备模型
    Model model = JenaModels.newModel();
    model.setNsPrefix("eg", NS);
    Resource A = model.createResource("eg:A");
    Resource B = model.createResource("eg:B");
    Resource C = model.createResource("eg:C");
    Resource D = model.createResource("eg:D");
    Property p = model.createProperty("eg:p");
    model.add(A, p, B);
    model.add(B, p, C);
    model.add(C, p, D);
    model.write(System.out, "N-TRIPLE");
    System.out.println();

    // 规则推理
    // 规则中预绑定的实体需要与模型输出中一致, 例如这里的<eg:p>
    String rule = "[rule1: (?a <eg:p> ?b) (?b <eg:p> ?c) -> (?a <eg:p> ?c)]";
    Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rule));
    reasoner.setDerivationLogging(true);
    InfModel infModel = ModelFactory.createInfModel(reasoner, model);
    System.out.println(infModel.getNsPrefixURI("eg"));
    infModel.write(System.out, "N-TRIPLE");
    System.out.println();

    PrintWriter printWriter = new PrintWriter(System.out);
    for (Statement stmt : infModel.listStatements().toList()) {
      System.out.println(JenaModels.asString(stmt));
      Iterator<Derivation> iter = infModel.getDerivation(stmt);
      while (iter.hasNext()) {
        iter.next().printTrace(printWriter, true);
        printWriter.flush();
        System.out.println();
      }
    }
    printWriter.flush();
  }
}
