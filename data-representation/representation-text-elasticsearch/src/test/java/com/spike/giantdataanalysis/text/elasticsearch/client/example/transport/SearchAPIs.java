package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.MyStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/**
 * 搜索API
 * @author zhoujiagen
 * @see QueryBuilders#termQuery(String, String)
 * @see QueryBuilders#rangeQuery(String)
 * @see SortBuilders#fieldSort(String)
 * @see AggregationBuilders#terms(String)
 * @see AggregationBuilders#dateHistogram(String)
 */
public final class SearchAPIs {
  private static final Logger LOG = LoggerFactory.getLogger(SearchAPIs.class);

  static final String index = "twitter";// 索引名称
  static final String type = "tweet";// 文档类型

  public static void main(String[] args) {
    try (TransportClient client = Clients.defaultClient();) {

      // 搜索
      // search(client);
      // 分页搜索
      // scrollSearch(client);
      // 一次执行多个搜索
      // multiSearch(client);
      // 聚合搜索
      // aggregationSearch(client);
      // 限制每个分片获取文档的最大数量, 以提前终止
      terminateAfter(client);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  static void search(TransportClient client) {
    LOG.debug("搜索");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//
        .setQuery(QueryBuilders.termQuery("user", "kimchy"))//
        .setPostFilter(QueryBuilders.rangeQuery("postDate").from("2017-01-01").to("2017-12-31"))//
        .setFrom(0).setSize(10)//
        .setExplain(true)//
        .execute()//
        .actionGet();

    LOG.info(Responses.asString(searchResponse));
  }

  /**
   * <pre>
   * 分页搜索
   * Scroll: https://www.elastic.co/guide/en/elasticsearch/reference/2.4/search-request-scroll.html
   * </pre>
   * @param client
   */
  static void scrollSearch(TransportClient client) {
    LOG.debug("分页搜索");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setTypes(type)//
        .addSort(SortBuilders.fieldSort("postDate").order(SortOrder.ASC))// 排序
        .setScroll(TimeValue.timeValueMillis(60000))//
        .setQuery(QueryBuilders.termQuery("user", "kimchy"))//
        .setSize(100)//
        .execute()//
        .actionGet();

    LOG.info(Responses.asString(searchResponse));

    // scroll while no hits are returned
    while (true) {
      for (SearchHit searchHit : searchResponse.getHits().getHits()) {
        LOG.info(Responses.asString(searchHit));
      }

      // search again
      String scrollId = searchResponse.getScrollId();
      searchResponse = client.prepareSearchScroll(scrollId)//
          .setScroll(TimeValue.timeValueMillis(60000))//
          .execute()//
          .actionGet();
      LOG.info(Responses.asString(searchResponse));

      // break condition
      if (searchResponse.getHits().getHits().length == 0) {
        break;
      }
    }

  }

  static void multiSearch(TransportClient client) {
    LOG.debug("一次执行多个搜索");

    SearchRequest searchRequest1 = client.prepareSearch(index)//
        .setQuery(QueryBuilders.queryStringQuery("elasticsearch"))//
        .setSize(1)//
        .request();
    SearchRequest searchRequest2 = client.prepareSearch(index)//
        .setQuery(QueryBuilders.matchQuery("user", "kimchy"))//
        .setSize(1)//
        .request();

    MultiSearchResponse multiSearchResponse = client.prepareMultiSearch()//
        .add(searchRequest1)//
        .add(searchRequest2)//
        .execute()//
        .actionGet();

    long totalHits = 0;
    for (MultiSearchResponse.Item responseItem : multiSearchResponse.getResponses()) {
      SearchResponse searchResponse = responseItem.getResponse();
      totalHits += searchResponse.getHits().getTotalHits();
      LOG.info(Responses.asString(searchResponse));
    }
    LOG.info("totalHits={}", totalHits);
  }

  static void aggregationSearch(TransportClient client) {
    LOG.debug("聚合搜索");

    // 聚合的名称
    String termAggName = "user-agg";
    String dateHistogramAggName = "postDate-agg";

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setQuery(QueryBuilders.matchAllQuery())//
        .addAggregation(AggregationBuilders.terms(termAggName).field("user"))//
        .addAggregation(AggregationBuilders.dateHistogram(dateHistogramAggName)//
            .field("postDate")//
            .interval(DateHistogramInterval.YEAR))//
        .execute()//
        .actionGet();

    Aggregations aggregations = searchResponse.getAggregations();
    StringTerms terms = aggregations.get(termAggName);
    LOG.info(Responses.asString(new MyStringTerms(terms)));
    InternalHistogram<?> dateHistogram = aggregations.get(dateHistogramAggName);
    LOG.info(Responses.asString(dateHistogram));
  }

  static void terminateAfter(TransportClient client) {
    LOG.debug("限制每个分片获取文档的最大数量");

    SearchResponse searchResponse = client.prepareSearch(index)//
        .setQuery(QueryBuilders.matchAllQuery())//
        .setTerminateAfter(10)//
        .get();

    LOG.info(Responses.asString(searchResponse));
    LOG.info("isTerminatedEarly={}", searchResponse.isTerminatedEarly());
  }

}
