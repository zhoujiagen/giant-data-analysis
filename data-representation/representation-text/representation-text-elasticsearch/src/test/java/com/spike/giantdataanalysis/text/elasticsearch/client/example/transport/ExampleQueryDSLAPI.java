package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/**
 * 查询构造DSL示例
 * @author zhoujiagen
 * @see QueryBuilders
 */
@RunWith(RandomizedRunner.class)
public final class ExampleQueryDSLAPI {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleQueryDSLAPI.class);

  static final String index = "twitter";// 索引名称
  static final String type = "tweet";// 文档类型

  static final String field_likes = "likes";// 字段likes
  static final String field_user = "user";// 字段user
  static final String field_message = "message";// 字段message

  @Test
  public void main() {

    try (TransportClient client = Clients.defaultClient();) {
      // 匹配所有查询
      match_all_query(client);
      // 全文检索
      full_text_query(client);
      // 词项级别的查询
      term_level_query(client);
      // 复合查询
      compound_query(client);
      // 联接查询
      joining_query(client);
      // 特殊的查询
      specialized_query(client);
      // 跨度查询
      span_query(client);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  private static void match_all_query(TransportClient client) {
    LOG.debug("匹配所有查询");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        .setQuery(QueryBuilders.matchAllQuery())//
        .get();
    LOG.info(Responses.asString(searchResponse));
  }

  /**
   * @param client
   * @see QueryBuilders#matchQuery(String, Object)
   * @see QueryBuilders#multiMatchQuery(Object, String...)
   * @see QueryBuilders#commonTermsQuery(String, Object)
   * @see QueryBuilders#queryStringQuery(String)
   * @see QueryBuilders#simpleQueryStringQuery(String)
   */
  private static void full_text_query(TransportClient client) {
    LOG.debug("全文检索");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        // (1) matchQuery
        // .setQuery(QueryBuilders.matchQuery(field_user, "catrtman elasticsearch"))//
        // (2) multiMatchQuery
        // .setQuery(
        // QueryBuilders.multiMatchQuery("catrtman elasticsearch", field_user, field_message))//
        // (3) commonTermsQuery
        // .setQuery(QueryBuilders.commonTermsQuery(field_user, "cartman"))//
        // (4) queryStringQuery
        // .setQuery(QueryBuilders.queryStringQuery("+cartman -elasticsearch").field(field_user))//
        // (5) simpleQueryStringQuery
        .setQuery(QueryBuilders.simpleQueryStringQuery("+cartman -elasticsearch").field(field_user))//
        .get();
    LOG.info(Responses.asString(searchResponse));
  }

  /**
   * @param client
   * @see QueryBuilders#termQuery(String, String)
   * @see @see {@link QueryBuilders#termsQuery(String, String...)}
   * @see QueryBuilders#rangeQuery(String)
   * @see QueryBuilders#existsQuery(String)
   * @see QueryBuilders#prefixQuery(String, String)
   * @see QueryBuilders#wildcardQuery(String, String)
   * @see QueryBuilders#regexpQuery(String, String)
   * @see QueryBuilders#fuzzyQuery(String, String)
   * @see QueryBuilders#typeQuery(String)
   * @see QueryBuilders#idsQuery(String...)
   */
  private static void term_level_query(TransportClient client) {
    LOG.debug("词项级别的查询");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        .setQuery(QueryBuilders.termQuery(field_user, "cartman"))//
        .get();
    LOG.info(Responses.asString(searchResponse));
  }

  /**
   * @param client
   * @see QueryBuilders#constantScoreQuery(org.elasticsearch.index.query.QueryBuilder)
   * @see QueryBuilders#boolQuery()
   * @see QueryBuilders#disMaxQuery()
   * @see QueryBuilders#functionScoreQuery()
   * @see ScoreFunctionBuilders
   * @see QueryBuilders#boostingQuery()
   * @see QueryBuilders#indicesQuery(org.elasticsearch.index.query.QueryBuilder, String...)
   * @see QueryBuilders#limitQuery(int)
   */
  private static void compound_query(TransportClient client) {
    LOG.debug("复合查询");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        .setQuery(//
          QueryBuilders//
              .constantScoreQuery(QueryBuilders.termQuery(field_user, "cartman"))//
              .boost(1.0f))//
        .get();
    LOG.info(Responses.asString(searchResponse));
  }

  /**
   * <pre>
   * 文档可以有nested类型的字段;
   * 单个索引中两个文档类型之间可以有parent-child关系.
   * </pre>
   * 
   * @param client
   * @see QueryBuilders#nestedQuery(String, QueryBuilder)
   * @see QueryBuilders#hasChildQuery(String, QueryBuilder)
   * @see QueryBuilders#hasParentQuery(String, QueryBuilder)
   */
  private static void joining_query(TransportClient client) {
    LOG.debug("联接查询");
  }

  /**
   * @param client
   * @see QueryBuilders#moreLikeThisQuery(String...)
   * @see QueryBuilders#templateQuery(String, java.util.Map)
   * @see QueryBuilders#scriptQuery(org.elasticsearch.script.Script)
   */
  private static void specialized_query(TransportClient client) {
    LOG.debug("特殊的查询");
  }

  /**
   * @param client
   * @see QueryBuilders#spanTermQuery(String, String)
   * @see QueryBuilders#spanMultiTermQueryBuilder(org.elasticsearch.index.query.MultiTermQueryBuilder)
   * @see QueryBuilders#spanFirstQuery(org.elasticsearch.index.query.SpanQueryBuilder, int)
   * @see QueryBuilders#spanNearQuery()
   * @see QueryBuilders#spanOrQuery()
   * @see QueryBuilders#spanNotQuery()
   * @see QueryBuilders#spanContainingQuery()
   * @see QueryBuilders#spanWithinQuery()
   */
  private static void span_query(TransportClient client) {
    LOG.debug("跨度查询");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        .setQuery(//
          QueryBuilders.spanTermQuery(field_user, "cartman"))//
        .get();
    LOG.info(Responses.asString(searchResponse));
  }
}
