package com.spike.text.lucene.tutorial;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.spike.text.lucene.util.LuceneAppConstants;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

/**
 * Construct indexes on .txt files, <br/>
 * migration from Lucene v3 to v5
 * @author zhoujiagen<br/>
 *         Aug 26, 2015 9:22:57 PM
 */
@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 1)
public class TxtIndexer {
  static final String SUB_DATA_DIR = "licences";
  static final String COMPLETE_DATA_DIR = LuceneAppConstants.ROOT_DATA_DIR + SUB_DATA_DIR;

  static final String SUB_INDEX_DIR = "licences";
  static final String COMPLETE_INDEX_DIR = LuceneAppConstants.ROOT_INDEX_DIR + SUB_INDEX_DIR;

  static final String DEFAULT_FIELD = "contents";

  private IndexWriter indexWriter;

  public static void main(String[] args) {
    TxtIndexer txtIndexer = new TxtIndexer(COMPLETE_INDEX_DIR);

    int numIndexed = txtIndexer.index(COMPLETE_DATA_DIR, new TxtFileFiler());

    txtIndexer.close();

    System.out.println(numIndexed + " files indexed.");
  }

  /**
   * A *.txt file filter
   */
  static class TxtFileFiler implements FileFilter {
    @Override
    public boolean accept(File pathname) {
      return pathname.getName().toLowerCase().endsWith(".txt");
    }

  }

  public TxtIndexer(String indexDir) {
    try {
      Directory directory = FSDirectory.open(Paths.get(indexDir));

      IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
      conf.setCommitOnClose(true);

      indexWriter = new IndexWriter(directory, conf);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * commit index changes, MUST be called
   */
  public void close() {
    try {
      indexWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int index(String dataDir, FileFilter fileFilter) {
    File[] files = new File(dataDir).listFiles();

    for (File file : files) {
      if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()
          && (fileFilter == null || fileFilter.accept(file))) {
        this.indexFile(file);
      }
    }

    return indexWriter.numDocs();
  }

  private void indexFile(File file) {
    try {
      System.out.println("Indexing " + file.getCanonicalPath());
      indexWriter.addDocument(this.generateDocument(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * see {@link Field} and its subclass for more details
   * @param file
   * @return
   */
  protected Document generateDocument(File file) {
    Document result = new Document();

    try {
      // source: Create a tokenized and indexed field that is not stored.
      // Term vectors will not be stored.
      result.add(new Field(DEFAULT_FIELD, new FileReader(file), TextField.TYPE_NOT_STORED));
      // source: Store.YES, Index.NOT_ANALYZED
      result.add(new StringField("filename", file.getName(), Field.Store.YES));
      // source: Stor.YES, Index.NOT_ANALYZED
      result.add(new StringField("fullpath", file.getCanonicalPath(), Field.Store.YES));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }
}
