package com.spike.text.lucene.tutorial;

import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

/**
 * Search with indexes constructed with {@link TxtIndexer}
 * @author zhoujiagen<br/>
 *         Aug 26, 2015 9:23:14 PM
 */
@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 1)
public class TxtSearcher {
  public static void main(String[] args) {

    String indexDir = TxtIndexer.COMPLETE_INDEX_DIR;
    String query = "patent";

    TxtSearcher txtSearcher = new TxtSearcher();
    txtSearcher.search(indexDir, query);
  }

  public void search(String indexDir, String queryInput) {
    try {
      Directory directory = FSDirectory.open(Paths.get(indexDir));
      IndexReader indexReader = DirectoryReader.open(directory);
      IndexSearcher indexSearcher = new IndexSearcher(indexReader);

      QueryParser queryParser = new QueryParser(TxtIndexer.DEFAULT_FIELD, new StandardAnalyzer());

      Query query = queryParser.parse(queryInput);
      TopDocs topDocs = indexSearcher.search(query, 10);

      System.out.println("Found " + topDocs.totalHits + " files");

      for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        Document document = indexSearcher.doc(scoreDoc.doc);
        System.out.println(document.get("fullpath"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
