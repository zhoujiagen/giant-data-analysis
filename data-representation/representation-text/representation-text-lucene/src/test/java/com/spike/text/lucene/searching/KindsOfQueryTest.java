package com.spike.text.lucene.searching;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneTestBookIndexingUtil.FIELD_NAMES;
import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.SearchingBookTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 3, section = { 4 })
public class KindsOfQueryTest extends SearchingBookTestBase {

  @Test
  public void test_TermQuery() throws IOException {
    Term term = new Term(FIELD_NAMES.contents, "java");
    Query query = new TermQuery(term);
    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResult(indexSearcher, topDocs, FIELD_NAMES.title,
      FIELD_NAMES.contents);

    term = new Term(FIELD_NAMES.isbn, "1933988177");
    query = new TermQuery(term);
    topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResult(indexSearcher, topDocs, FIELD_NAMES.isbn);
  }

  /**
   * @throws IOException
   * @see TermRangeQuery
   */
  @Test
  public void test_TermRangeQuery() throws IOException {
    BytesRef lowerTerm = new BytesRef("d");
    BytesRef upperTerm = new BytesRef("j");
    TermRangeQuery query = new TermRangeQuery(FIELD_NAMES.title2, lowerTerm, upperTerm, true, true);

    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResult(indexSearcher, topDocs, FIELD_NAMES.title2);
  }

  /**
   * @throws IOException
   * @see NumericRangeQuery
   */
  @Test
  public void test_NumericRangeQuery() throws IOException {
    NumericRangeQuery<Integer> query =
        NumericRangeQuery.newIntRange(FIELD_NAMES.pubmonth, 200605, 200609, true, true);

    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResult(indexSearcher, topDocs, FIELD_NAMES.pubmonth);
  }

  /**
   * @throws IOException
   * @see PrefixQuery
   */
  @Test
  public void test_PrefixQuery() throws IOException {
    Term term = new Term(FIELD_NAMES.category, "/technology/computers/programming");
    PrefixQuery query = new PrefixQuery(term);

    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResult(indexSearcher, topDocs, FIELD_NAMES.title,
      FIELD_NAMES.category);
  }

  /**
   * @throws IOException
   * @see BooleanQuery
   * @see BooleanClause.Occur
   */
  @Test
  public void test_BooleanQuery() throws IOException {
    // 1 and
    TermQuery query1 = new TermQuery(new Term(FIELD_NAMES.subject, "search"));
    NumericRangeQuery<Integer> query2 =
        NumericRangeQuery.newIntRange(FIELD_NAMES.pubmonth, 201001, 201012, true, true);
    BooleanQuery query = new BooleanQuery();
    query.add(query1, BooleanClause.Occur.MUST);
    query.add(query2, BooleanClause.Occur.MUST);

    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    // LuceneAppUtil.renderSearchResult(indexSearcher, topDocs,
    // FIELD_NAMES.subject,
    // FIELD_NAMES.pubmonth);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAMES.subject,
      FIELD_NAMES.pubmonth);

    // 2 or
    TermQuery query3 =
        new TermQuery(new Term(FIELD_NAMES.category,
            "/technology/computers/programming/methodology"));
    TermQuery query4 = new TermQuery(new Term(FIELD_NAMES.category, "/philosophy/eastern"));
    query = new BooleanQuery();
    query.add(query3, BooleanClause.Occur.SHOULD);
    query.add(query4, BooleanClause.Occur.SHOULD);

    topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    // LuceneAppUtil.renderSearchResult(indexSearcher, topDocs,
    // FIELD_NAMES.category,
    // FIELD_NAMES.title);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs,
      FIELD_NAMES.category, FIELD_NAMES.title);
  }

}
