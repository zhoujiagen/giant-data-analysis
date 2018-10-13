package com.spike.text.lucene.util;

/**
 * Copyright Manning Publications Co. Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific lan
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

/**
 * #1 Get category <br/>
 * #2 Pull fields <br/>
 * #3 Add fields to Document instance <br/>
 * #4 Flag subject field <br/>
 * #5 Add catch-all contents field <br/>
 * #6 Custom analyzer to override multi-valued position increment
 */
@LuceneInAction2ndBook
public class LuceneTestBookIndexingUtil {

  public static void main(String[] args) throws IOException {
    String dataDir = LuceneAppConstants.BOOK_DATA_DIR;
    String indexDir = LuceneAppConstants.BOOK_INDEX_DIR;

    List<File> results = new ArrayList<File>();
    findFiles(results, new File(dataDir));
    System.out.println(results.size() + " books to index");
    Directory directory = FSDirectory.open(Paths.get(indexDir));

    IndexWriterConfig config = new IndexWriterConfig(new MyStandardAnalyzer());
    config.setCommitOnClose(true);
    IndexWriter indexWriter = new IndexWriter(directory, config);

    for (File file : results) {
      Document document = getDocument(dataDir, file);
      indexWriter.addDocument(document);
    }

    indexWriter.close();
    directory.close();
  }

  /**
   * field names used for indexing
   * @author zhoujiagen<br/>
   *         Sep 3, 2015 7:23:22 PM
   */
  public static interface FIELD_NAMES {
    String isbn = "isbn";
    String title = "title";
    String author = "author";
    String url = "url";
    String subject = "subject";
    String pubmonth = "pubmonth";
    String category = "category";
    String title2 = "title2";
    String pubmonthAsDay = "pubmonthAsDay";
    String contents = "contents";
  }

  public static Document getDocument(String rootDir, File file) throws IOException {
    Properties props = new Properties();
    props.load(new FileInputStream(file));

    Document doc = new Document();

    // category comes from relative path below the base directory
    String category = file.getParent().substring(rootDir.length());
    category = category.replace(File.separatorChar, '/');

    String isbn = props.getProperty("isbn");
    String title = props.getProperty("title");
    String author = props.getProperty("author");
    String url = props.getProperty("url");
    String subject = props.getProperty("subject");

    String pubmonth = props.getProperty("pubmonth");

    System.out.println(title + "\n" + author + "\n" + subject + "\n" + pubmonth + "\n" + category
        + "\n---------");

    // doc.add(new Field("isbn", isbn, Field.Store.YES,
    // Field.Index.NOT_ANALYZED));
    doc.add(LuceneAppUtil.createStringField("isbn", isbn, Store.YES, false, IndexOptions.DOCS,
      false));

    // doc.add(new Field("category", category, Field.Store.YES,
    // Field.Index.NOT_ANALYZED));
    doc.add(LuceneAppUtil.createStringField("category", category, Store.YES, false,
      IndexOptions.DOCS, false));

    // doc.add(new Field("title", title, Field.Store.YES,
    // Field.Index.ANALYZED,
    // Field.TermVector.WITH_POSITIONS_OFFSETS));
    doc.add(LuceneAppUtil.createStringField("title", title, Store.YES, true,
      IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, false));

    // doc.add(new Field("title2", title.toLowerCase(), Field.Store.YES,
    // Field.Index.NOT_ANALYZED_NO_NORMS,
    // Field.TermVector.WITH_POSITIONS_OFFSETS));
    doc.add(LuceneAppUtil.createStringField("title2", title.toLowerCase(), Store.YES, false,
      IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, true));

    // split multiple authors into unique field instances
    String[] authors = author.split(",");
    for (String a : authors) {
      // doc.add(new Field("author", a, Field.Store.YES,
      // Field.Index.NOT_ANALYZED,
      // Field.TermVector.WITH_POSITIONS_OFFSETS));
      doc.add(LuceneAppUtil.createStringField("author", a, Store.YES, false,
        IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, false));
    }

    // doc.add(new Field("url", url, Field.Store.YES,
    // Field.Index.NOT_ANALYZED_NO_NORMS));
    doc.add(LuceneAppUtil.createStringField("url", url, Store.YES, false, IndexOptions.DOCS, true));
    // doc.add(new Field("subject", subject, Field.Store.YES,
    // Field.Index.ANALYZED,
    // Field.TermVector.WITH_POSITIONS_OFFSETS));
    doc.add(LuceneAppUtil.createStringField("subject", subject, Store.YES, true,
      IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, true));

    Field pubmonthField = new IntField("pubmonth", Integer.parseInt(pubmonth), Store.YES);
    doc.add(pubmonthField);

    Date d;
    try {
      d = DateTools.stringToDate(pubmonth);
    } catch (ParseException pe) {
      throw new RuntimeException(pe);
    }

    Field pubmonthAsDayField =
        new IntField("pubmonthAsDay", (int) (d.getTime() / (1000 * 3600 * 24)), Store.YES);
    doc.add(pubmonthAsDayField);

    for (String text : new String[] { title, subject, author, category }) {
      // doc.add(new Field("contents", text, Field.Store.NO,
      // Field.Index.ANALYZED,
      // Field.TermVector.WITH_POSITIONS_OFFSETS));
      doc.add(LuceneAppUtil.createStringField("contents", text, Store.NO, true,
        IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, false));

    }

    return doc;
  }

  /**
   * A Recursive method to find the dedicated files in directory
   * @param result
   * @param dir
   */
  private static void findFiles(List<File> result, File dir) {
    for (File file : dir.listFiles()) {
      if (file.getName().endsWith(".properties")) {
        result.add(file);
      } else if (file.isDirectory()) {
        findFiles(result, file);
      }
    }
  }

  private static class MyStandardAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
      StandardTokenizer tokenizer = new StandardTokenizer();
      PositionIncrementAttribute posIncrAtt =
          tokenizer.addAttribute(PositionIncrementAttribute.class);
      if ("contents".equals(fieldName)) {
        posIncrAtt.setPositionIncrement(100);
      } else {
        posIncrAtt.setPositionIncrement(0);
      }

      TokenStreamComponents result = new TokenStreamComponents(tokenizer);

      return result;
    }
  }

}
