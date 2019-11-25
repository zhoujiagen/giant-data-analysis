package com.spike.text.lucene.util;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;

/**
 * Base Test Case class for test indexing and searching in memory
 * @author zhoujiagen<br/>
 *         Sep 3, 2015 10:49:26 PM
 */
public abstract class SearchingMemIndexTestBase {

  protected Directory directory;
  protected IndexWriter indexWriter;
  protected Analyzer analyzer;

  @Before
  public void setUp() throws IOException {
    directory = LuceneAppUtil.getMemDirectory();

    // analyzer
    analyzer = this.defineAnalyzer();
    if (analyzer == null) {
      analyzer = new WhitespaceAnalyzer();
    }

    indexWriter = LuceneAppUtil.getIndexWriter(directory, analyzer);
    System.out.println("using analyzer: " + analyzer.getClass());

    // create indexes
    this.indexing();
  }

  @After
  public void tearDown() throws IOException {
    directory.close();
  }

  protected IndexSearcher getIndexSearcher() {
    return LuceneAppUtil.getIndexSearcher(directory);
  }

  protected void indexing() throws IOException {
    this.doIndexing();

    indexWriter.close();
  }

  /**
   * if not set use {@link WhitespaceAnalyzer} as default
   * @param analyzer
   */
  protected abstract Analyzer defineAnalyzer();

  /**
   * No need to bother close the {@link IndexWriter}
   */
  protected abstract void doIndexing() throws IOException;

}
