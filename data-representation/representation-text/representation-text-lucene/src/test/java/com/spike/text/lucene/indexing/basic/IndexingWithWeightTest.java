package com.spike.text.lucene.indexing.basic;

import static com.spike.text.lucene.util.LuceneAppUtil.createStringField;
import static com.spike.text.lucene.util.LuceneAppUtil.getMemDirectory;
import static com.spike.text.lucene.util.LuceneAppUtil.mockDocumentBoost;
import static com.spike.text.lucene.util.LuceneAppUtil.wrapWithBoost;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 2, section = { 5 })
public class IndexingWithWeightTest {

  // directory in memory
  private Directory directory = getMemDirectory();

  private IndexWriter getIndexWriter() {
    return LuceneAppUtil.getIndexWriter(directory, new WhitespaceAnalyzer());
  }

  /**
   * not support in Version 5.2.0
   * @throws IOException
   * @see {@link Field#boost()} for more informations
   */
  @Test
  public void documentWithWeight() throws IOException {
    IndexWriter indexWriter = this.getIndexWriter();

    Document document = new Document();

    String senderEmail = "jiagenzhou@gmail.com";
    String senderDomain = "gmail.com";
    String senderName = "jiagenzhou";
    String subject = "Work with Lucene";
    String body = "...";

    document.add(createStringField("senderEmail", senderEmail, Store.YES, false, IndexOptions.DOCS,
      true));
    document.add(createStringField("senderName", senderName, Store.YES, true, IndexOptions.DOCS,
      true));
    document.add(createStringField("subject", subject, Store.YES, true, IndexOptions.DOCS, true));
    document.add(createStringField("body", body, Store.NO, true, IndexOptions.DOCS, true));

    if (senderDomain.toLowerCase().contains("gmail")) {
      // add a field to mock
      document.add(mockDocumentBoost(2D));
    } else {
      document.add(mockDocumentBoost(0.1D));
    }

    indexWriter.addDocument(document);
    indexWriter.close();
  }

  @Test
  public void fieldWithWeight() throws IOException {
    IndexWriter indexWriter = this.getIndexWriter();

    String subject = "Work with Lucene";

    Document document = new Document();

    // these field must indexed and not omit norms
    Field subjectField =
        createStringField("subject", subject, Store.YES, true, IndexOptions.DOCS, false);
    FieldType fieldType = subjectField.fieldType();
    assertFalse(IndexOptions.NONE.equals(fieldType.indexOptions()));
    assertFalse(fieldType.omitNorms());

    subjectField = wrapWithBoost(subjectField, 1.2F);
    document.add(subjectField);

    indexWriter.addDocument(document);
    indexWriter.close();
  }

}
