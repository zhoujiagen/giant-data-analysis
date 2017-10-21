package com.spike.giantdataanalysis.neo4j.example.extension;

import java.io.IOException;
import java.util.function.Function;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilder;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.helpers.collection.Iterators;
import org.neo4j.kernel.configuration.ssl.LegacySslPolicyConfig;
import org.neo4j.server.ServerTestUtils;
import org.neo4j.string.UTF8;
import org.neo4j.test.server.HTTP;

/**
 * REF:https://github.com/neo4j/neo4j-documentation/blob/3.2/neo4j-harness-test/src/test/java/org/
 * neo4j/harness/doc/ExtensionTestingDocTest.java
 */
public class ExtensionExampleTest {

  @Path("")
  public static class MyUnmanagedExtension {
    @GET
    public Response myEndpoint() {
      return Response.ok().build();
    }
  }

  @Test
  public void testMyExtension() throws Exception {
    // Given
    try (ServerControls server = this.getServerBuilder()//
        .withExtension("/myExtension", MyUnmanagedExtension.class)//
        .newServer()) {
      // When
      System.out.println(server.httpURI().resolve("myExtension").toString());
      HTTP.Response response =
          HTTP.GET(HTTP.GET(server.httpURI().resolve("myExtension").toString()).location());

      // Then
      Assert.assertEquals(200, response.status());
    }
  }

  @Test
  public void testMyExtensionWithFunctionFixture() throws Exception {
    // Given
    try (ServerControls server =
        this.getServerBuilder().withExtension("/myExtension", MyUnmanagedExtension.class)
            .withFixture(new Function<GraphDatabaseService, Void>() {
              @Override
              public Void apply(GraphDatabaseService graphDatabaseService) throws RuntimeException {
                try (Transaction tx = graphDatabaseService.beginTx()) {
                  graphDatabaseService.createNode(Label.label("User"));
                  tx.success();
                }
                return null;
              }
            }).newServer()) {
      // When
      Result result = server.graph().execute("MATCH (n:User) return n");

      // Then
      Assert.assertEquals(1, Iterators.count(result));
    }
  }

  @Path("/hw")
  public class ExtensionExample2 {

    // private final GraphDatabaseService db;
    //
    // public ExtensionExample2(@Context GraphDatabaseService db) {
    // this.db = db;
    // }

    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{nodeId}")
    @GET
    public Response helloworld(@PathParam("nodeId") long nodeId) {
      try {

        System.out.println(nodeId);

        // do some stuff
        // try (Transaction tx = db.beginTx()) {
        // Node node = db.getNodeById(nodeId);
        // if (node == null) {
        // System.err.println("Node[" + nodeId + "] does not exist!");
        // } else {
        // System.err.println(node);
        // }
        // tx.success();
        // }

        Response response = Response//
            .ok().entity(UTF8.encode("Hello World, nodeId=" + nodeId))//
            .build();
        return response;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  }

  // FIXME(zhoujiagen) 自定义扩展的测试 why HTTP 500???
  @Test
  public void test_ExtensionExample2() {

    try (ServerControls server = this.getServerBuilder()//
        .withExtension("/e/u", ExtensionExample2.class)// 自定义扩展
        // .withFixture(new Function<GraphDatabaseService, Void>() {
        // @Override
        // public Void apply(GraphDatabaseService db) throws RuntimeException {
        // try (Transaction tx = db.beginTx()) {
        // Node node = db.createNode();
        // System.out.println(node.getId());
        // tx.success();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return null;
        // }
        // })//
        .newServer()) {

      System.out.println(server.httpURI().resolve("e/u/hw/0").toString());
      HTTP.Response response = HTTP.GET(server.httpURI().resolve("e/u/hw/0").toString());

      System.out.println(response.toString());
      System.out.println();
      System.out.println(response.rawContent());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private TestServerBuilder getServerBuilder() throws IOException {
    TestServerBuilder serverBuilder = TestServerBuilders.newInProcessBuilder();

    serverBuilder.withConfig(//
      LegacySslPolicyConfig.certificates_directory.name(),//
      ServerTestUtils.getRelativePath(ServerTestUtils.getSharedTestTemporaryFolder(),
        LegacySslPolicyConfig.certificates_directory));

    return serverBuilder;
  }

}
