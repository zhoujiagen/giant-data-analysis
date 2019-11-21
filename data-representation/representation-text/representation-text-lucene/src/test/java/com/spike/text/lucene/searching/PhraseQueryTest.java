package com.spike.text.lucene.searching;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Assert;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.SearchingMemIndexTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 3, section = { 4 })
public class PhraseQueryTest extends SearchingMemIndexTestBase {

  private static final String FIELD_NAME = "field";
  private static final String CONTENT = "the quick brown fox jumped over the lazy dog";

  @Test
  public void test_PhraseQuery() throws IOException {
    String[] phrase = new String[] { "quick", "fox" };
    IndexSearcher indexSearcher = this.getIndexSearcher();

    PhraseQuery query = new PhraseQuery();
    query.setSlop(1);
    for (String p : phrase) {
      query.add(new Term(FIELD_NAME, p));
    }

    TopDocs topDocs = indexSearcher.search(query, 10);
    Assert.assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);

    // multiple term phrases
    phrase = new String[] { "quick", "jumped", "lazy" };
    indexSearcher = this.getIndexSearcher();

    query = new PhraseQuery();
    query.setSlop(4);
    for (String p : phrase) {
      query.add(new Term(FIELD_NAME, p));
    }

    topDocs = indexSearcher.search(query, 10);
    Assert.assertTrue(topDocs.totalHits > 0);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);
  }

  @Override
  protected void doIndexing() throws IOException {
    Document document = new Document();

    // should index with postion data
    Field field =
        LuceneAppUtil.createStringField(FIELD_NAME, CONTENT, Store.YES, true,
          IndexOptions.DOCS_AND_FREQS_AND_POSITIONS, false);
    Assert.assertFalse(IndexOptions.NONE.equals(field.fieldType()));
    Assert.assertFalse(IndexOptions.DOCS.equals(field.fieldType()));
    Assert.assertFalse(IndexOptions.DOCS_AND_FREQS.equals(field.fieldType()));
    document.add(field);

    this.indexWriter.addDocument(document);
  }

  @Override
  protected Analyzer defineAnalyzer() {
    return new WhitespaceAnalyzer();

  }

}
