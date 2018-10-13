package com.spike.text.lucene.searching;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.LuceneTestBookIndexingUtil.FIELD_NAMES;
import com.spike.text.lucene.util.SearchingBookTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

/**
 * @author zhoujiagen<br/>
 *         Sep 3, 2015 4:45:51 PM
 * @see IndexSearcher
 * @see Query
 * @see QueryParser
 * @see TopDocs
 * @see ScoreDoc
 */
@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 3, section = { 1, 2, 3 })
public class BasicSearchingTest extends SearchingBookTestBase {

  /**
   * @throws IOException
   * @see Term
   * @see TermQuery
   */
  @Test
  public void test_Term() throws IOException {

    Term term = new Term("subject", "ant");
    Query query = new TermQuery(term);
    TopDocs topDocs = indexSearcher.search(query, 10);
    System.out.println(topDocs.totalHits);
    System.out.println(topDocs.getMaxScore());

    term = new Term("subject", "junit");
    query = new TermQuery(term);
    topDocs = indexSearcher.search(query, 10);
    System.out.println(topDocs.totalHits);
    System.out.println(topDocs.getMaxScore());

  }

  /**
   * @throws ParseException
   * @throws IOException
   * @see QueryParser
   */
  @Test
  public void test_QueryParser() throws ParseException, IOException {
    QueryParser queryParser =
        LuceneAppUtil.getQueryParser(FIELD_NAMES.contents, new SimpleAnalyzer());
    Query query = queryParser.parse("+JUNIT +ANT -MOCK");
    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    System.out.println(topDocs.getMaxScore());
    Document document = indexSearcher.doc(topDocs.scoreDocs[0].doc);
    System.out.println(document.get(FIELD_NAMES.title));

    query = queryParser.parse("mock OR junit");
    topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    System.out.println(topDocs.getMaxScore());
    document = indexSearcher.doc(topDocs.scoreDocs[0].doc);
    System.out.println(document.get(FIELD_NAMES.title));

  }

  /**
   * @throws ParseException
   * @throws IOException
   * @see Explanation#toHtml()
   * @see Explanation#toString()
   */
  @Test
  public void test_explain() throws ParseException, IOException {
    QueryParser queryParser =
        LuceneAppUtil.getQueryParser(FIELD_NAMES.contents, new SimpleAnalyzer());
    Query query = queryParser.parse("+JUNIT +ANT -MOCK");
    TopDocs topDocs = indexSearcher.search(query, 10);

    assertTrue(topDocs.totalHits > 0);
    System.out.println(topDocs.getMaxScore());

    Document document = indexSearcher.doc(topDocs.scoreDocs[0].doc);
    System.out.println(document.get(FIELD_NAMES.title));

    Explanation explain = indexSearcher.explain(query, topDocs.scoreDocs[0].doc);
    System.out.println(explain.toString());

  }

}
