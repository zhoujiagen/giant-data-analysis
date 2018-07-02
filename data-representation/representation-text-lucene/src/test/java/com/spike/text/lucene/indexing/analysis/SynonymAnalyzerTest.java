package com.spike.text.lucene.indexing.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Assert;
import org.junit.Test;

import com.spike.text.lucene.analysis.SimpleSynonymEngineImpl;
import com.spike.text.lucene.analysis.SynonymAnalyzer;
import com.spike.text.lucene.util.LuceneAppAnalyzerUtil;
import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.SearchingMemIndexTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 4, section = { 5 })
public class SynonymAnalyzerTest extends SearchingMemIndexTestBase {
  static final String SENTENCE_1 = "The quick brown fox jumps over the lazy dog";

  static final String FIELD_NAME = "content";

  @Override
  protected Analyzer defineAnalyzer() {
    return new SynonymAnalyzer(new SimpleSynonymEngineImpl());
  }

  @Override
  protected void doIndexing() throws IOException {
    IndexWriter indexWriter = this.indexWriter;

    Document document = new Document();
    document.add(LuceneAppUtil.createStringField(FIELD_NAME, SENTENCE_1, Store.YES, true,
      IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, true));

    indexWriter.addDocument(document);
  }

  /**
   * view the analysis result
   */
  @Test
  public void analysis() {
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(this.defineAnalyzer(), SENTENCE_1);
  }

  /**
   * use {@link Query}
   * @throws IOException
   */
  @Test
  public void searchByAPI() throws IOException {

    TermQuery query = new TermQuery(new Term(FIELD_NAME, "hops"));// 1

    IndexSearcher indexSearcher = this.getIndexSearcher();

    TopDocs topDocs = indexSearcher.search(query, 10);
    Assert.assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);

    PhraseQuery query2 = new PhraseQuery();// 2
    query2.add(new Term(FIELD_NAME, "fox"));
    query2.add(new Term(FIELD_NAME, "hops"));

    Assert.assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);
  }

  /**
   * use {@link QueryParser}
   * @throws IOException
   * @throws ParseException
   */
  @Test
  public void searchByQuerParser() throws IOException, ParseException {
    IndexSearcher indexSearcher = this.getIndexSearcher();

    Query query = new QueryParser(FIELD_NAME, this.defineAnalyzer()).parse("\"fox jumps\"");// 1

    TopDocs topDocs = indexSearcher.search(query, 10);
    Assert.assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);

    query = new QueryParser(FIELD_NAME, new StandardAnalyzer()).parse("\"fox jumps\"");// 2
    topDocs = indexSearcher.search(query, 10);
    Assert.assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);
  }

}
