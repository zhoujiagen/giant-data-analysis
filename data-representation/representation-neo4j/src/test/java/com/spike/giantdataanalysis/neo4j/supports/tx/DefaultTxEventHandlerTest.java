package com.spike.giantdataanalysis.neo4j.supports.tx;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNLabels;
import com.spike.giantdataanalysis.neo4j.example.SocialNetworkDomain.SNPropertyKeys;
import com.spike.giantdataanalysis.neo4j.supports.Neo4js;

// NOT WORKING!!!
public class DefaultTxEventHandlerTest {

  public static void main(String[] args) {

    GraphDatabaseService db = Neo4js.defaultDatabase();
    db.registerTransactionEventHandler(new DefaultTxEventHandler());

    try (Transaction tx = db.beginTx();) {
      Node john = db.findNode(SNLabels.Users, SNPropertyKeys.name.name(), "John Johnson");
      System.out.println(Neo4js.asShortString(john));

      Node tempNode = db.createNode();
      System.out.println(Neo4js.asShortString(tempNode));
      tempNode.delete();
    }

    try {
      Thread.sleep(5000l);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    db.shutdown();

  }

}
