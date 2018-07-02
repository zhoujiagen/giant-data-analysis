package com.spike.text.lucene.indexing.basic;

import static com.spike.text.lucene.util.LuceneAppUtil.getMemDirectory;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 2, section = { 6 })
public class IndexingNonStringFieldTest {
  // directory in memory
  private Directory directory = getMemDirectory();

  private IndexWriter getIndexWriter() {
    return LuceneAppUtil.getIndexWriter(directory, new WhitespaceAnalyzer());
  }

  @Test
  public void number() throws IOException {
    IndexWriter indexWriter = this.getIndexWriter();

    Document document = new Document();
    DoubleField numbericField = new DoubleField("number", 19.99D, Store.YES);
    // change the value
    numbericField.setDoubleValue(29.99D);
    document.add(numbericField);

    indexWriter.close();
  }

  @Test
  public void timestamp() {
    LongField field = new LongField("timestamp", new Date().getTime(), Store.YES);
    assertNotNull(field.numericValue());
    System.out.println(field.stringValue());

    field.setLongValue(new Date().getTime());
    assertNotNull(field.numericValue());
    System.out.println(field.stringValue());
  }

  @Test
  public void day() {
    int value = (int) (new Date().getTime() / 24 / 3600);
    IntField field = new IntField("day", value, Store.YES);

    assertNotNull(field.numericValue());
    System.out.println(field.numericValue());

    value = (int) (new Date().getTime() / 24 / 3600);
    field.setIntValue(value);
    assertNotNull(field.numericValue());
    System.out.println(field.numericValue());
  }

  /**
   * usage of {@link Calendar}
   */
  @Test
  public void yearMonthDayAsInt() {
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    calendar.setTime(new Date());

    int year = calendar.get(Calendar.YEAR);
    System.out.println(year);
    // count fom 0
    int month = calendar.get(Calendar.MONTH) + 1;
    System.out.println(month);
    // count from 1
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    System.out.println(day);

    int value = year * 10000 + month * 100 + day;
    System.out.println(value);

    IntField field = new IntField("dayOfMonth", value, Store.YES);

    assertNotNull(field.numericValue());
    System.out.println(field.numericValue());
  }
}
