package com.spike.text.lucene.searching.queryparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.QueryBuilder;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.LuceneTestBookIndexingUtil.FIELD_NAMES;
import com.spike.text.lucene.util.SearchingBookTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 3, section = { 5 })
public class QueryParserInBookTest extends SearchingBookTestBase {

  /**
   * @see Query#toString()
   */
  @Test
  public void Query_toString() {
    BooleanQuery query = new BooleanQuery();
    query.add(new FuzzyQuery(new Term("field", "kountry")), BooleanClause.Occur.MUST);
    query.add(new TermQuery(new Term("title", "western")), BooleanClause.Occur.SHOULD);

    assertEquals("+field:kountry~2 title:western", query.toString());
    System.out.println(query.toString());

    assertEquals("+kountry~2 title:western", query.toString("field"));
    System.out.println(query.toString("field"));
  }

  @Test
  public void termQuery() throws ParseException {
    // with default field
    QueryParser queryParser = new QueryParser(FIELD_NAMES.subject, new StandardAnalyzer());

    Query query = queryParser.parse("computers");
    System.out.println(query.toString());
  }

  /**
   * @throws ParseException
   * @throws IOException
   * @see TermRangeQuery
   */
  @Test
  public void termRangeQuery() throws ParseException, IOException {
    QueryParser queryParser = new QueryParser(FIELD_NAMES.subject, new StandardAnalyzer());

    // include
    Query query = queryParser.parse("title2:[Q TO V]");
    assertTrue(query instanceof TermRangeQuery);
    System.out.println(query.toString());

    // exclude
    query = queryParser.parse("title2:{Q TO \"Tapestry in Action\"}");
    assertTrue(query instanceof TermRangeQuery);
    System.out.println(query.toString());

    TopDocs topDocs = this.indexSearcher.search(query, 10);
    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAMES.subject,
      FIELD_NAMES.title2);
  }

  /**
   * @throws ParseException
   * @throws IOException
   * @see NumericRangeQuery
   */
  @Test
  public void numericRangeQuery() throws ParseException, IOException {
    QueryParser queryParser = new QueryParser(FIELD_NAMES.pubmonth, new StandardAnalyzer());

    NumericRangeQuery<Integer> query =
        NumericRangeQuery.newIntRange(FIELD_NAMES.pubmonth, 200605, 200609, true, true);
    TopDocs topDocs = this.indexSearcher.search(query, 10);
    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAMES.subject,
      FIELD_NAMES.title2);

    Query query2 = queryParser.parse("pubmonth:[200605 TO 200609]");
    assertFalse(query2 instanceof NumericRangeQuery<?>);
    assertTrue(query2 instanceof TermRangeQuery);
    System.out.println(query2.toString());

    topDocs = this.indexSearcher.search(query2, 10);
    assertFalse(topDocs.totalHits > 0);
  }

  /**
   * @throws ParseException
   * @see PrefixQuery
   * @see WildcardQuery
   */
  @Test
  public void prefixQueryAndWildcardQuery() throws ParseException {
    QueryParser queryParser = new QueryParser("field", new StandardAnalyzer());
    Query query = queryParser.parse("PrefixQuery*");

    assertTrue(query instanceof PrefixQuery);
    assertEquals("prefixquery*", query.toString("field"));

    // off lowercase expand
    queryParser.setLowercaseExpandedTerms(false);
    query = queryParser.parse("PrefixQuery*");
    assertEquals("PrefixQuery*", query.toString("field"));

    query = queryParser.parse("ild?*");
    assertTrue(query instanceof WildcardQuery);
  }

  /**
   * @throws ParseException
   * @see BooleanQuery
   */
  @Test
  public void booleanQuery() throws ParseException {
    QueryParser queryParser = new QueryParser("field", new StandardAnalyzer());

    // default is OR, change to AND
    queryParser.setDefaultOperator(Operator.AND);

    Query query1 = queryParser.parse("+a +b");
    assertTrue(query1 instanceof BooleanQuery);
    Query query2 = queryParser.parse("a AND b");
    assertEquals(query1.toString(), query2.toString());
    Query query3 = queryParser.parse("a b");
    assertEquals(query2.toString(), query3.toString());

    Query query4 = queryParser.parse("+a -b");
    Query query5 = queryParser.parse("a AND NOT b");
    assertEquals(query4.toString(), query5.toString());

    // a complex example
    Query query = queryParser.parse("(agile OR extreme) AND methodology");
    assertTrue(query instanceof BooleanQuery);
    assertEquals("+(agile extreme) +methodology", query.toString("field"));
  }

  /**
   * @throws ParseException
   * @see PhraseQuery
   */
  @Test
  public void phraseQuery() throws ParseException {
    QueryParser queryParser = new QueryParser("field", new StandardAnalyzer());

    Query query = queryParser.parse("\"This is some phrase\"");
    assertTrue(query instanceof PhraseQuery);
    assertEquals("\"? ? some phrase\"", query.toString("field"));

    // reduction to TermQuery
    query = queryParser.parse("\"Term\"");
    assertTrue(query instanceof TermQuery);
    assertEquals("term", query.toString("field"));

    // slop
    queryParser.setPhraseSlop(5);
    query = queryParser.parse("\"This is some phrase\"");
    assertEquals("\"? ? some phrase\"~5", query.toString("field"));
  }

  /**
   * Lucene supports fuzzy searches based on Damerau-Levenshtein Distance<br
   * / To do a fuzzy search use the tilde, "~", symbol at the end of a Single word Term.<br/>
   * The value is between 0 and 2, <br/>
   * and he default that is used if the parameter is not given is 2 edit distances
   * @throws ParseException
   * @see FuzzyQuery
   */
  @Test
  public void fuzzyQuery() throws ParseException {
    QueryParser queryParser = new QueryParser("field", new StandardAnalyzer());

    Query query = queryParser.parse("kountry~");
    assertTrue(query instanceof FuzzyQuery);
    assertEquals("kountry~2", query.toString("field"));

    query = queryParser.parse("kountry~1");
    assertTrue(query instanceof FuzzyQuery);
    assertEquals("kountry~1", query.toString("field"));
  }

  /**
   * @see QueryBuilder
   */
  @Test
  public void queryBuilder() {
    QueryBuilder builder = new QueryBuilder(new StandardAnalyzer());
    Query a = builder.createBooleanQuery("body", "just a test");
    System.out.println(a.toString());
    System.out.println(a.getClass());
    Query b = builder.createPhraseQuery("body", "another test");
    System.out.println(b.toString());
    System.out.println(b.getClass());
    Query c = builder.createMinShouldMatchQuery("body", "another test", 0.5f);
    System.out.println(c.toString());
    System.out.println(c.getClass());
  }

  @Test
  public void matchAllDocsQuery() throws ParseException {
    QueryParser queryParser = new QueryParser("field", new StandardAnalyzer());

    Query query = queryParser.parse("*:*");
    assertTrue(query instanceof MatchAllDocsQuery);
    assertEquals("*:*", query.toString());
    assertEquals("*:*", query.toString("field"));
  }

  /**
   * add weight to query clause using `^`
   * @throws ParseException
   */
  @Test
  public void weightedQuery() throws ParseException {
    QueryParser queryParser = new QueryParser("field", new StandardAnalyzer());

    Query query = queryParser.parse("computer^2");
    assertTrue(query instanceof TermQuery);
    assertEquals("computer^2.0", query.toString("field"));

    query = queryParser.parse("computer");
    assertTrue(query instanceof TermQuery);
    assertEquals("computer", query.toString("field"));
  }

}
