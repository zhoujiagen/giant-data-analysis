package com.spike.giantdataanalysis.neo4j.example.extension;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.string.UTF8;

/**
 * JAX-RS端点
 * <p>
 * see ExtensionExampleTest. NOT WORKING!!!
 */
@Path("/helloworld")
public class ExtensionExample {

  private final GraphDatabaseService db;

  public ExtensionExample(@Context GraphDatabaseService db) {
    this.db = db;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/{nodeId}")
  public Response helloworld(@PathParam("nodeId") long nodeId) {
    // do some stuff
    try (Transaction tx = db.beginTx()) {
      Node node = db.getNodeById(nodeId);
      if (node == null) {
        System.err.println("Node[" + nodeId + "] does not exist!");
      } else {
        System.err.println(node);
      }
      tx.success();
    }

    return Response//
        .status(Status.OK)//
        .entity(UTF8.encode("Hello World, nodeId=" + nodeId))//
        .build();
  }

}
