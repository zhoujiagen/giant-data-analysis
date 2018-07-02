package com.spike.text.lucene.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.spike.text.lucene.util.LuceneTestBookIndexingUtil.FIELD_NAMES;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook
public final class LuceneTestBookUtil {

  public static boolean hitsIncludeTitle(IndexSearcher searcher, TopDocs hits, String title)
      throws IOException {
    for (ScoreDoc match : hits.scoreDocs) {
      Document doc = searcher.doc(match.doc);
      if (title.equals(doc.get(FIELD_NAMES.title))) {
        return true;
      }
    }
    System.out.println("title '" + title + "' not found");
    return false;
  }

  public static int hitCount(IndexSearcher searcher, Query query) throws IOException {
    return searcher.search(query, 1).totalHits;
  }

  @Deprecated
  public static int hitCount(IndexSearcher searcher, Query query, Filter filter) throws IOException {
    return searcher.search(query, filter, 1).totalHits;
  }

  public static void dumpHits(IndexSearcher searcher, TopDocs hits) throws IOException {
    if (hits.totalHits == 0) {
      System.out.println("No hits");
    }

    for (ScoreDoc match : hits.scoreDocs) {
      Document doc = searcher.doc(match.doc);
      System.out.println(match.score + ":" + doc.get(FIELD_NAMES.title));
    }
  }

  public static Directory getBookIndexDirectory() {
    try {
      return FSDirectory.open(Paths.get(LuceneAppConstants.BOOK_INDEX_DIR));
    } catch (IOException e) {
      throw new LuceneAppException("get book related index directory faild", e);
    }
  }

  public static void rmDir(File dir) throws IOException {
    if (dir.exists()) {
      File[] files = dir.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (!files[i].delete()) {
          throw new IOException("could not delete " + files[i]);
        }
      }
      dir.delete();
    }
  }

}
