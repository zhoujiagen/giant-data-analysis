package com.spike.text.solr.solrj;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;

/**
 * 嵌入的测试用Solr实例
 * @author zhoujiagen
 */
public class EmbeddedSolrInstance implements AutoCloseable {

  private String solrHome;
  private String coreName;

  private EmbeddedSolrServer server;

  /**
   * 嵌入的测试用Solr实例
   * @param solrHome solr.home
   * @param coreName core name
   */
  public EmbeddedSolrInstance(String solrHome, String coreName) {
    this.solrHome = solrHome;
    this.coreName = coreName;

    CoreContainer coreContainer = new CoreContainer(solrHome);
    coreContainer.load();
    server = new EmbeddedSolrServer(coreContainer, coreName);
  }

  public EmbeddedSolrServer getServer() {
    return server;
  }

  public String getSolrHome() {
    return solrHome;
  }

  public String getCoreName() {
    return coreName;
  }

  @Override
  public void close() throws Exception {
    if (server != null) {
      server.close();
    }
  }

}
