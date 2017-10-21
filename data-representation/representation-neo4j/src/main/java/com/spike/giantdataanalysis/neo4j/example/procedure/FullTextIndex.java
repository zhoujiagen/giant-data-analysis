package com.spike.giantdataanalysis.neo4j.example.procedure;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

public class FullTextIndex {

  private static final Map<String, String> FULL_TEXT = //
      MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext");

  @Context
  public GraphDatabaseService db;
  @Context
  public Log log;

  /**
   * 通过Label索引搜索
   * @param label
   * @param query
   * @return
   */
  @Procedure(name = "example.search", mode = Mode.WRITE)
  public Stream<SearchHit> search(@Name("label") String label, @Name("query") String query) {
    String indexName = this.indexName(label);
    if (!db.index().existsForNodes(indexName)) {
      log.debug("index `%s` does not exist!", indexName);
      return Stream.empty();
    }

    return db.index().forNodes(indexName)//
        .query(query)//
        .stream().map(SearchHit::new);
  }

  /**
   * 创建Schema索引(Label)
   * @param nodeId
   * @param propKeys
   */
  @Procedure(name = "example.index", mode = Mode.SCHEMA)
  public void index(//
      @Name("nodeId") long nodeId, //
      @Name(value = "properties", defaultValue = "[]") List<String> propKeys) {
    Node node = db.getNodeById(nodeId);

    Map<String, Object> properties = node.getProperties(propKeys.toArray(new String[0]));
    for (Label label : node.getLabels()) {
      Index<Node> index = db.index().forNodes(this.indexName(label.name()), FULL_TEXT);

      index.remove(node);
      for (String propKey : properties.keySet()) {
        index.add(node, propKey, properties.get(propKey));
      }
    }
  }

  private String indexName(String label) {
    return "label-" + label;
  }
}
