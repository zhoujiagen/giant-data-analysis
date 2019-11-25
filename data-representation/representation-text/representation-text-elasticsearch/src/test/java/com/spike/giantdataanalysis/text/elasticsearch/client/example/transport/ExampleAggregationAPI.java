package com.spike.giantdataanalysis.text.elasticsearch.client.example.transport;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.aggregations.Children;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanksAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetric;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetricAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Clients;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Jsons;
import com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Responses;

/**
 * <pre>
 * 聚合API
 * 
 * 两类聚合: bucket, metric.
 * </pre>
 * 
 * @author zhoujiagen
 * @see com.spike.giantdataanalysis.text.elasticsearch.client.example.support.Datas#TwitterTweet()
 */
@RunWith(RandomizedRunner.class)
public class ExampleAggregationAPI {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleAggregationAPI.class);

  static final String index = "twitter";// 索引名称
  static final String type = "tweet";// 文档类型

  static final String field_likes = "likes";// 字段likes
  static final String field_user = "user";// 字段user

  @Test
  public void main() {

    try (TransportClient client = Clients.defaultClient();) {
      // 多层聚合
      structuringAggregation(client);
      // 度量值聚合
      metricAggregation(client);
      // 桶聚合
      bucketAggregation(client);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  /**
   * @see Global
   * @see Filter
   * @see Filters
   * @see Missing
   * @see Nested
   * @see AggregationBuilders#reverseNested(String)
   * @see Children
   * @see Terms
   * @see SignificantTerms
   * @see Range
   * @see Histogram
   * @param client
   */
  // REF:
  // https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/_bucket_aggregations.html
  static void bucketAggregation(TransportClient client) {
    LOG.debug("桶聚合");

    bucketAggregation_global(client);
    bucketAggregation_filter(client);
    bucketAggregation_term(client);
    bucketAggregation_order(client);
    bucketAggregation_range(client);
    bucketAggregation_histogram(client);
  }

  private static void bucketAggregation_global(TransportClient client) {
    LOG.debug("桶聚合-global");
    // Fielddata is disabled on text fields by default. Set fielddata=true on [user] in order to
    // load fielddata in memory by uninverting the inverted index. Note that this can however use
    // significant memory. Alternatively use a keyword field instead.
    // PUT /twitter/_mapping/tweet
    // {
    // "properties": {
    // "user": {
    // "type": "text",
    // "fielddata": true
    // }
    // }
    // }
    
    GlobalAggregationBuilder builder = AggregationBuilders.global("aggName")//
        .subAggregation(AggregationBuilders.terms("subAggName").field(field_user));

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    Global aggregationResult = searchResponse.getAggregations().get("aggName");
    long value = aggregationResult.getDocCount();
    LOG.info("value={}", value);
  }

  private static void bucketAggregation_filter(TransportClient client) {
    LOG.debug("桶聚合-filter");

    FilterAggregationBuilder builder = AggregationBuilders//
        .filter("aggName", QueryBuilders.termQuery(field_user, "cartman"));

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    Filter aggregationResult = searchResponse.getAggregations().get("aggName");
    long value = aggregationResult.getDocCount();
    LOG.info("value={}", value);
  }

  private static void bucketAggregation_term(TransportClient client) {
    LOG.debug("桶聚合-term");

    TermsAggregationBuilder builder = AggregationBuilders//
        .terms("aggName")//
        .field(field_user);

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    Terms aggregationResult = searchResponse.getAggregations().get("aggName");
    for (Terms.Bucket bucket : aggregationResult.getBuckets()) {
      LOG.info("key={}, docCount={}", bucket.getKeyAsString(), bucket.getDocCount());
    }
  }

  private static void bucketAggregation_order(TransportClient client) {
    LOG.debug("桶聚合-order");

    TermsAggregationBuilder builder = AggregationBuilders//
        .terms("aggName")//
        .field(field_user)//
        .order(BucketOrder.count(false));

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    Terms aggregationResult = searchResponse.getAggregations().get("aggName");
    for (Terms.Bucket bucket : aggregationResult.getBuckets()) {
      LOG.info("key={}, docCount={}", bucket.getKeyAsString(), bucket.getDocCount());
    }
  }

  private static void bucketAggregation_range(TransportClient client) {
    LOG.debug("桶聚合-range");

    RangeAggregationBuilder builder = AggregationBuilders//
        .range("aggName")//
        .field(field_likes)//
        .addUnboundedTo(1)//
        .addRange(1l, 4l)//
        .addUnboundedFrom(5l);

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    Range aggregationResult = searchResponse.getAggregations().get("aggName");
    for (Range.Bucket bucket : aggregationResult.getBuckets()) {
      LOG.info("key={}[{}->{}], docCount={}", //
        bucket.getKeyAsString(), bucket.getFrom(), bucket.getTo(), bucket.getDocCount());
    }
  }

  private static void bucketAggregation_histogram(TransportClient client) {
    LOG.debug("桶聚合-histogram");

    HistogramAggregationBuilder builder = AggregationBuilders//
        .histogram("aggName")//
        .field(field_likes)//
        .interval(3l);

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    Histogram aggregationResult = searchResponse.getAggregations().get("aggName");
    for (Histogram.Bucket bucket : aggregationResult.getBuckets()) {
      LOG.info("key={}, docCount={}", bucket.getKeyAsString(), bucket.getDocCount());
    }
  }

  /**
   * @param client
   * @see Min
   * @see Max
   * @see Sum
   * @see Avg
   * @see Stats
   * @see ValueCount
   * @see Percentile
   * @see Cardinality
   * @see TopHits
   * @see ScriptedMetric
   */
  // REF:
  // https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/_metrics_aggregations.html
  static void metricAggregation(TransportClient client) {
    LOG.debug("度量值聚合");

    // min
    metricAggregation_min(client);
    // max
    metricAggregation_max(client);
    // sum
    metricAggregation_sum(client);
    // avg
    metricAggregation_avg(client);
    // stats
    // extended stats
    metricAggregation_stats(client);
    // value count
    metricAggregation_valuecount(client);
    // percentile
    // percentile rank
    metricAggregation_percentile(client);
    // cardinality
    metricAggregation_cardinality(client);
    // top hits
    metricAggregation_tophits(client);
    // scripted
    metricAggregation_scripted(client);
  }

  // Em, a facility.
  private static SearchRequestBuilder _searchRequestBuilder(TransportClient client) {
    return client.prepareSearch(index)//
        .setTypes(type)//
        .setQuery(QueryBuilders.matchAllQuery());
  }

  private static void metricAggregation_min(TransportClient client) {
    LOG.debug("度量值聚合-min");

    MinAggregationBuilder builder = AggregationBuilders.min("aggName").field(field_likes);

    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Min aggregationResult = searchResponse.getAggregations().get("aggName");
    double value = aggregationResult.getValue();
    LOG.info("value={}", value);
  }

  private static void metricAggregation_max(TransportClient client) {
    LOG.debug("度量值聚合-max");

    MaxAggregationBuilder builder = AggregationBuilders.max("aggName").field(field_likes);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Max aggregationResult = searchResponse.getAggregations().get("aggName");
    double value = aggregationResult.getValue();
    LOG.info("value={}", value);
  }

  private static void metricAggregation_sum(TransportClient client) {
    LOG.debug("度量值聚合-sum");

    SumAggregationBuilder builder = AggregationBuilders.sum("aggName").field(field_likes);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Sum aggregationResult = searchResponse.getAggregations().get("aggName");
    double value = aggregationResult.getValue();
    LOG.info("value={}", value);
  }

  private static void metricAggregation_avg(TransportClient client) {
    LOG.debug("度量值聚合-avg");

    AvgAggregationBuilder builder = AggregationBuilders.avg("aggName").field(field_likes);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Avg aggregationResult = searchResponse.getAggregations().get("aggName");
    double value = aggregationResult.getValue();
    LOG.info("value={}", value);
  }

  private static void metricAggregation_stats(TransportClient client) {
    LOG.debug("度量值聚合-stats");

    // StatsBuilder builder = AggregationBuilders.stats("aggName").field(field);
    ExtendedStatsAggregationBuilder builder =
        AggregationBuilders.extendedStats("aggName").field(field_likes);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Stats aggregationResult = searchResponse.getAggregations().get("aggName");
    LOG.info("value={}", Jsons.asJson(aggregationResult));
  }

  private static void metricAggregation_valuecount(TransportClient client) {
    LOG.debug("度量值聚合-valuecount");

    ValueCountAggregationBuilder builder = AggregationBuilders.count("aggName").field(field_likes);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    ValueCount aggregationResult = searchResponse.getAggregations().get("aggName");
    double value = aggregationResult.getValue();
    LOG.info("value={}", value);
  }

  private static void metricAggregation_percentile(TransportClient client) {
    LOG.debug("度量值聚合-percentile");
    PercentilesAggregationBuilder builder =
        AggregationBuilders.percentiles("aggName").field(field_likes)//
            .percentiles(1.0, 5.0, 10.0, 20.0, 30.0, 75.0, 95.0, 99.0);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Percentiles aggregationResult = searchResponse.getAggregations().get("aggName");
    LOG.info("value={}", Jsons.asJson(aggregationResult));
    for (Percentile entry : aggregationResult) {
      LOG.info("entry={}", Jsons.asJson(entry));
    }

    // rank
    PercentileRanksAggregationBuilder builder2 = AggregationBuilders
        .percentileRanks("aggName", new double[] { 1.45, 7.75, 9.5 }).field(field_likes);
    searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder2).get();
    LOG.info(Responses.asString(searchResponse));

    PercentileRanks aggregationResult2 = searchResponse.getAggregations().get("aggName");
    LOG.info("value={}", Jsons.asJson(aggregationResult));
    for (Percentile entry : aggregationResult2) {
      LOG.info("entry={}", Jsons.asJson(entry));
    }
  }

  private static void metricAggregation_cardinality(TransportClient client) {
    LOG.debug("度量值聚合-cardinality");

    CardinalityAggregationBuilder builder =
        AggregationBuilders.cardinality("aggName").field(field_likes);
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    LOG.info(Responses.asString(searchResponse));

    Cardinality aggregationResult = searchResponse.getAggregations().get("aggName");
    double value = aggregationResult.getValue();
    LOG.info("value={}", value);
  }

  private static void metricAggregation_tophits(TransportClient client) {
    LOG.debug("度量值聚合-tophits");

    TermsAggregationBuilder builder = AggregationBuilders//
        .terms("aggName").field(field_likes)//
        .subAggregation(//
          AggregationBuilders.topHits("topHitsAggName")//
              .explain(true)//
              .from(0)//
              .size(10)//
    );
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();
    // LOG.info(Responses.asString(searchResponse));

    Terms aggregationResult = searchResponse.getAggregations().get("aggName");
    for (Terms.Bucket entry : aggregationResult.getBuckets()) {
      String key = entry.getKeyAsString(); // bucket key
      long docCount = entry.getDocCount(); // Doc count
      LOG.info("key [{}], doc_count [{}]", key, docCount);

      TopHits topHits = entry.getAggregations().get("topHitsAggName");
      for (SearchHit hit : topHits.getHits().getHits()) {
        LOG.info(" -> id [{}], _source [{}]", hit.getId(), hit.getSourceAsString());
      }
    }
  }

  /**
   * <pre>
   * add to config/elasticsearch.yml
   * 
   * # 使用script字段
   * script.inline: true
   * script.indexed: true
   * </pre>
   * 
   * @param client
   */
  private static void metricAggregation_scripted(TransportClient client) {
    LOG.debug("度量值聚合-scripted");

    ScriptedMetricAggregationBuilder builder = AggregationBuilders.scriptedMetric("aggName")
        // (1) init
        .initScript(new Script("params._agg['likes'] = []"))
        // (2) map
        .mapScript(new Script("if (doc['user'].value == \"cartman\") " + //
            "{ params._agg.likes.add(doc['likes'].value) } " + //
            "else " + //
            "{ params._agg.likes.add(-1 * doc['likes'].value) }"))
        // (3*) combine
        .combineScript(new Script(
            "double likes_sum = 0; for (t in params._agg.likes) { likes_sum += t } return likes_sum"))
        // (4*) reduce
        .reduceScript(//
          new Script(
              "double likes_sum = 0; for (a in params._aggs) { likes_sum += a } return likes_sum"));
    SearchResponse searchResponse = _searchRequestBuilder(client)//
        .addAggregation(builder).get();

    ScriptedMetric aggregationResult = searchResponse.getAggregations().get("aggName");
    Object scriptedResult = aggregationResult.aggregation();
    LOG.info("scriptedResult={}", Jsons.asJson(scriptedResult));
  }

  // REF:
  // https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/_structuring_aggregations.html
  static void structuringAggregation(TransportClient client) {
    LOG.debug("多层聚合");

    AggregationBuilder aggregation = //
        AggregationBuilders.terms("by_country").field("country")// (1) bucket
            .subAggregation(//
              AggregationBuilders.dateHistogram("by_year").field("dateOfBirth")// (2) bucket
                  .dateHistogramInterval(DateHistogramInterval.YEAR)//
                  .subAggregation(//
                    AggregationBuilders.avg("avg_children").field("children")// (3) metric
              )//
        );
    SearchResponse searchResponse = client.prepareSearch(index)//
        .setQuery(QueryBuilders.matchAllQuery())//
        .addAggregation(aggregation)//
        .execute()//
        .actionGet();

    LOG.info(Responses.asString(searchResponse));
  }
}
