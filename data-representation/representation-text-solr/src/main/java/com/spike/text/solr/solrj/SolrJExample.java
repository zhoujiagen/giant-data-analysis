package com.spike.text.solr.solrj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.commons.lang.RandomUtils;
import com.spike.giantdataanalysis.commons.lang.RandomUtils.IdPattern;
import com.spike.text.solr.domain.BookSchema;

/**
 * SolrJ示例
 * @author zhoujiagen
 */
public class SolrJExample {

  private static final Logger LOG = LoggerFactory.getLogger(SolrJExample.class);

  public static void main(String[] args) {
    String solrHome = "data/solr";
    String coreName = "book";

    try (EmbeddedSolrInstance solrInstance = new EmbeddedSolrInstance(solrHome, coreName); //
        SolrClient client = solrInstance.getServer();//
    ) {

      cleanAllData(client);

      index(client);

      query(client);

      closeClient(client);

    } catch (Exception e) {
      LOG.error("SolrJExample fail", e);
    }
  }

  /**
   * @param client
   */
  static void index(SolrClient client) {

    List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();

    SolrInputDocument document = new SolrInputDocument();
    document.addField(BookSchema.Field.id.name(), RandomUtils.nextId(IdPattern.LowerCase));
    document.addField(BookSchema.Field.title.name(), "Pattern Recognition and Machine Learning");
    document.addField(BookSchema.Field.author.name(), "Bishop, Christopher");
    document.addField(BookSchema.Field.foreword.name(),
      "This is the first textbook on pattern recognition to present the Bayesian viewpoint."
          + " The book presents approximate inference algorithms that permit fast approximate answers in situations where "
          + "exact answers are not feasible. It uses graphical models to describe probability distributions when "
          + "no other books apply graphical models to machine learning. No previous knowledge of pattern recognition or "
          + "machine learning concepts is assumed. Familiarity with multivariate calculus and basic linear algebra is required, "
          + "and some experience in the use of probabilities would be helpful though not essential as the book includes "
          + "a self-contained introduction to basic probability theory.");

    documents.add(document);

    try {
      LOG.debug("index...");
      client.add(documents);
      client.commit(true, true);
    } catch (SolrServerException | IOException e) {
      LOG.error("index fail", e);
    }

  }

  static void query(SolrClient client) {
    SolrQuery query = new SolrQuery();

    String queryString = null;
    // query all
    queryString = "*:*";

    // query field foreward
    queryString = BookSchema.Field.foreword.name() + ":Bayesian";

    query.setQuery(queryString);

    try {
      LOG.debug("query...");
      QueryResponse response = client.query(query);
      SolrDocumentList documents = response.getResults();

      if (CollectionUtils.isEmpty(documents)) {
        LOG.info(BookSchema.renderBook(null));
      } else {
        for (SolrDocument document : documents) {
          LOG.info(BookSchema.renderBook(document));
        }
      }

    } catch (SolrServerException | IOException e) {
      LOG.error("query fail", e);
    }
  }

  /**
   * 关闭客户端
   * @param client
   */
  static void closeClient(SolrClient client) {
    try {
      LOG.debug("closeClient...");
      client.close();
    } catch (IOException e) {
      LOG.error("closeClient fail", e);
    }
  }

  /**
   * 清空所有数据
   * @param client
   */
  static void cleanAllData(SolrClient client) {
    try {
      LOG.debug("cleanAllData...");
      client.deleteByQuery("*:*");
    } catch (SolrServerException | IOException e) {
      LOG.error("cleanAllData fail", e);
      e.printStackTrace();
    }
  }

}
