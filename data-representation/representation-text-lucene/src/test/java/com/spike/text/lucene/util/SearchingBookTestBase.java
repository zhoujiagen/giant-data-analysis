package com.spike.text.lucene.util;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;

/**
 * The basic class of Searching Books
 * @author zhoujiagen<br/>
 *         Sep 3, 2015 10:49:58 PM
 */
public class SearchingBookTestBase {
  protected Directory directory;

  protected IndexSearcher indexSearcher;

  @Before
  public void setUp() {
    directory = LuceneTestBookUtil.getBookIndexDirectory();
    indexSearcher = LuceneAppUtil.getIndexSearcher(directory);
  }

  @After
  public void tearDown() throws IOException {
    directory.close();
  }

}
