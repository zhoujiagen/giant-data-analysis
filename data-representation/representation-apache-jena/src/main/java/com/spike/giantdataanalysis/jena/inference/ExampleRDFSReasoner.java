package com.spike.giantdataanalysis.jena.inference;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

import com.spike.giantdataanalysis.jena.supports.JenaModels;

/**
 * RDFS推理示例
 * @author zhoujiagen
 * @see ReasonerRegistry
 * @see RDFSRuleReasonerFactory
 * @see RDF
 * @see XSDDatatype
 */
public class ExampleRDFSReasoner {

  static final String NS = "http;//spike.com/test/";

  public static void main(String[] args) {
    Model model = schemaAndDate();

    // 方法1
    // InfModel infModel = ModelFactory.createRDFSModel(model);
    // 方法2
    Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
    reasoner.setDerivationLogging(true); // 开启推导日志
    InfModel infModel = ModelFactory.createInfModel(reasoner, model);
    // 方法3
    // RDFSRuleReasonerFactory.theInstance().create(configuration)

    System.out.println(JenaModels.validate(infModel));

    Resource colin = infModel.getResource(NS + "colin");
    System.out.println(JenaModels.asString(infModel, colin, RDF.type, null));

    Resource Person = infModel.getResource(NS + "Person");
    System.out.println(JenaModels.asString(infModel, Person, RDF.type, null));
  }

  static Model schemaAndDate() {
    OntModel model = JenaModels.newOntModel();

    OntClass Person = model.createClass(NS + "Person");
    OntClass Teenager = model.createClass(NS + "Teenager");
    Teenager.addSuperClass(Person);

    // mum, parent, age
    ObjectProperty parent = model.createObjectProperty(NS + "parent");
    parent.addDomain(Person);
    parent.addRange(Person);
    ObjectProperty mum = model.createObjectProperty(NS + "mum");
    mum.addSuperProperty(parent);
    DatatypeProperty age = model.createDatatypeProperty(NS + "age");
    age.addRange(XSD.integer);

    Individual colin = model.createIndividual(NS + "colin", Teenager);
    Individual rosy = model.createIndividual(NS + "rosy", Person);
    colin.addProperty(mum, rosy);
    colin.addProperty(age, "13", XSDDatatype.XSDinteger);

    return model;
  }

}
