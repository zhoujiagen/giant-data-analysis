package com.spike.text.lucene.indexing.basic;

import static com.spike.text.lucene.util.LuceneAppUtil.createStringField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.junit.Before;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 2, section = { 3 })
public class IndexingTest {

  private String[] ids = new String[] { "1", "2" };
  private String[] unindexed = new String[] { "Netherlands", "Italy" };
  private String[] unstored = new String[] { "Amsterdam has lots of bridges",
      "Venice has lots of canals" };
  private String[] text = new String[] { "Amsterdam", "Venice" };

  // directory in memory
  private Directory directory = LuceneAppUtil.getMemDirectory();

  private IndexWriter getIndexWriter() {
    return LuceneAppUtil.getIndexWriter(directory, new WhitespaceAnalyzer());
  }

  @Before
  public void setUp() throws IOException {

    IndexWriter indexWriter = this.getIndexWriter();

    for (int i = 0; i < ids.length; i++) {
      Document document = new Document();

      // Store.YES, Index.NOT_ANALYZED
      document.add(createStringField("id", ids[i], Store.YES, false, IndexOptions.DOCS, true));
      // Store.YES, Index.NO
      document.add(createStringField("country", unindexed[i], Store.YES, false, IndexOptions.NONE,
        true));
      // Store.NO, Index.ANALYZED
      document.add(createStringField("contents", unstored[i], Store.NO, true, IndexOptions.DOCS,
        true));
      // Store.YES, Index.ANALYZED
      document.add(createStringField("city", text[i], Store.YES, true, IndexOptions.DOCS, true));

      indexWriter.addDocument(document);
    }

    // commit changes
    indexWriter.close();
  }

  @Test
  public void testIndexWriter() throws IOException {
    IndexWriter indexWriter = this.getIndexWriter();

    assertEquals(ids.length, indexWriter.numDocs());

    indexWriter.close();
  }

  @Test
  public void testIndexReader() throws IOException {
    IndexReader indexReader = LuceneAppUtil.getIndexReader(directory);

    assertEquals(ids.length, indexReader.numDocs());
    assertEquals(ids.length, indexReader.maxDoc());

    indexReader.close();
  }

  @Test
  public void testDeleteBeforeForceMergeDeletes() throws IOException {
    IndexWriter indexWriter = this.getIndexWriter();
    assertEquals(2, indexWriter.numDocs());

    indexWriter.deleteDocuments(new Term("id", "1"));
    indexWriter.commit();

    assertTrue(indexWriter.hasDeletions());
    assertEquals(2, indexWriter.maxDoc());
    assertEquals(1, indexWriter.numDocs());

    indexWriter.close();
  }

  @Test
  public void testDeleteAfterForceMergeDeletes() throws IOException {
    IndexWriter indexWriter = this.getIndexWriter();
    assertEquals(2, indexWriter.numDocs());

    indexWriter.deleteDocuments(new Term("id", "1"));

    // 3.0 Version: IndexWriter#optimize
    indexWriter.forceMergeDeletes();
    indexWriter.commit();

    assertFalse(indexWriter.hasDeletions());
    assertEquals(1, indexWriter.maxDoc());
    assertEquals(1, indexWriter.numDocs());

    indexWriter.close();
  }

  @Test
  public void testUpdate() throws IOException {
    assertEquals(1, LuceneAppUtil.getHitCount(directory, "city", "Amsterdam"));

    IndexWriter indexWriter = this.getIndexWriter();

    // update
    Document document = new Document();
    document.add(createStringField("id", "1", Store.YES, false, IndexOptions.DOCS, true));
    document.add(createStringField("country", "Netherlands", Store.YES, false, IndexOptions.NONE,
      true));
    document.add(createStringField("contents", "Den Haag has lots of museums", Store.NO, true,
      IndexOptions.DOCS, true));
    // must not analyzed, i.e. not tokenized
    document.add(createStringField("city", "Den Haag", Store.YES, false, IndexOptions.DOCS, true));

    indexWriter.updateDocument(new Term("id", "1"), document);
    indexWriter.commit();
    indexWriter.close();

    assertEquals(0, LuceneAppUtil.getHitCount(directory, "city", "Amsterdam"));
    assertEquals(1, LuceneAppUtil.getHitCount(directory, "city", "Den Haag"));
  }

}
