package com.spike.text.lucene.searching;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Assert;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.SearchingMemIndexTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 3, section = { 4 })
public class MatchAllDocsQueryTest extends SearchingMemIndexTestBase {
  private static final String FIELD_NAME = "contents";

  @Test
  public void test_MatchAllDocsQuery() throws IOException {

    IndexSearcher indexSearcher = this.getIndexSearcher();
    MatchAllDocsQuery query = new MatchAllDocsQuery();

    TopDocs topDocs = indexSearcher.search(query, 10);

    Assert.assertTrue(topDocs.totalHits > 0);

    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, FIELD_NAME);

  }

  @Override
  protected Analyzer defineAnalyzer() {
    return null;
  }

  @Override
  protected void doIndexing() throws IOException {

    IndexWriter indexWriter = this.indexWriter;

    Document document = new Document();
    document.add(LuceneAppUtil.createStringField(FIELD_NAME, "fuzzy", Store.YES, true,
      IndexOptions.DOCS, false));
    indexWriter.addDocument(document);

    document = new Document();
    document.add(LuceneAppUtil.createStringField(FIELD_NAME, "wuzzy", Store.YES, true,
      IndexOptions.DOCS, false));
    indexWriter.addDocument(document);

  }

}
