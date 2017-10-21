package com.spike.giantdataanalysis.neo4j.example.procedure;

import org.neo4j.graphdb.Node;

public class SearchHit {
  // This records contain a single field named 'nodeId'
  public long nodeId;

  public SearchHit(Node node) {
    this.nodeId = node.getId();
  }
}
