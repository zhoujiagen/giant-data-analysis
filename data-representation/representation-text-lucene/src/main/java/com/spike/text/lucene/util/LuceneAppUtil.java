package com.spike.text.lucene.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import com.spike.giantdataanalysis.commons.lang.StringUtils;

/**
 * Lucene Application Utilities, contains indexing and searching functionalities
 * @author zhoujiagen<br/>
 *         Sep 2, 2015 11:00:53 PM
 */
public final class LuceneAppUtil {

  /**
   * @param dirpath
   * @return
   */
  public static final Directory convertToDirecotory(String dirpath) {
    try {
      return FSDirectory.open(Paths.get(dirpath));
    } catch (IOException e) {
      throw new LuceneAppException("convert string path to Directory failed", e);
    }
  }

  /**
   * @param dirpath
   * @return
   */
  public static final Directory convertToDirecotory(Path dirpath) {
    try {
      return FSDirectory.open(Paths.get(dirpath.toAbsolutePath().toString()));
    } catch (IOException e) {
      throw new LuceneAppException("convert  path to Directory failed", e);
    }
  }

  /**
   * get memory directory
   * @return
   */
  public static final Directory getMemDirectory() {
    return new RAMDirectory();
  }

  /**
   * get a memory index writer
   * @param analyzer The {@link Analyzer}
   * @return
   */
  public static final IndexWriter getIndexWriter(Directory directory, Analyzer analyzer) {
    IndexWriter result = null;

    // Directory directory = new RAMDirectory();
    IndexWriterConfig conf = new IndexWriterConfig(analyzer);

    try {
      result = new IndexWriter(directory, conf);
    } catch (IOException e) {
      throw new LuceneAppException("get index write failed", e);
    }

    return result;
  }

  /**
   * get an {@link IndexReader}
   * @param directory
   * @return
   */
  public static final IndexReader getIndexReader(Directory directory) {
    try {
      return DirectoryReader.open(directory);
    } catch (IOException e) {
      throw new LuceneAppException("get index reader failed", e);
    }
  }

  /**
   * get an {@link IndexSearcher}
   * @param directory
   * @return
   */
  public static final IndexSearcher getIndexSearcher(Directory directory) {
    return new IndexSearcher(getIndexReader(directory));
  }

  /**
   * get a {@link QueryParser}
   * @param fieldName
   * @param analyzer
   * @return
   */
  public static final QueryParser getQueryParser(String fieldName, Analyzer analyzer) {
    return new QueryParser(fieldName, analyzer);
  }

  /**
   * @param name
   * @param value
   * @param stored whether store in index
   * @param tokenized whether use analyzer to tokenize
   * @param indexOptions write what to index
   * @param omitNorms whether omit norms, default is false
   * @return
   * @see FieldType#setDocValuesType(org.apache.lucene.index.DocValuesType)
   * @see FieldType#setIndexOptions(IndexOptions)
   * @see FieldType#setNumericType(org.apache.lucene.document.FieldType.NumericType)
   * @see FieldType#setOmitNorms(boolean)
   * @see FieldType#setStored(boolean)
   * @see FieldType#setStoreTermVectorOffsets(boolean)
   * @see FieldType#setStoreTermVectorPayloads(boolean)
   * @see FieldType#setStoreTermVectorPositions(boolean)
   * @see FieldType#setStoreTermVectors(boolean)
   * @see FieldType#setTokenized(boolean)
   */
  public static final Field createStringField(//
      String name, //
      String value, //
      Store stored,//
      boolean tokenized, //
      IndexOptions indexOptions, //
      boolean omitNorms//
      ) {
    FieldType fieldType = new FieldType();
    if (Store.YES.equals(stored)) {
      fieldType.setStored(true);
    } else if (Store.NO.equals(stored)) {
      fieldType.setStored(false);
    }

    fieldType.setTokenized(tokenized);

    fieldType.setOmitNorms(omitNorms);

    fieldType.setIndexOptions(indexOptions);
    fieldType.freeze();

    Field result = new Field(name, value, fieldType);

    return result;
  }

  /**
   * add boost to {@link Field}
   * @param sourceField
   * @param boost
   * @return
   */
  public static final Field wrapWithBoost(final Field sourceField, Float boost) {
    sourceField.setBoost(boost);
    return sourceField;
  }

  /**
   * Since {@link Document}'s setBoost() is removed, we mock this feature with a {@link Field} named
   * `boost`
   * @param value
   * @return
   */
  public static final Field mockDocumentBoost(double value) {
    Field result = new DoubleField("boost", value, Store.YES);

    return result;
  }

  /**
   * get total hit count with Query (fieldName, queryString)
   * @param directory
   * @param fieldName
   * @param queryString
   * @return
   * @throws IOException
   */
  public static final int getHitCount(//
      Directory directory, //
      String fieldName, //
      String queryString//
      ) throws IOException {
    int result = -1;

    IndexSearcher indexSearcher = new IndexSearcher(getIndexReader(directory));

    Term term = new Term(fieldName, queryString);
    Query query = new TermQuery(term);

    result = indexSearcher.search(query, 1).totalHits;

    return result;
  }

  /**
   * render search result
   * @param indexSearcher
   * @param topDocs
   * @param fieldNames
   * @throws IOException
   */
  public static final void renderSearchResult(//
      IndexSearcher indexSearcher, //
      TopDocs topDocs, //
      String... fieldNames//
      ) throws IOException {
    System.out.println(topDocs.totalHits);

    for (int i = 0; i < topDocs.totalHits; i++) {
      System.out.println(topDocs.scoreDocs[i].score);
      Document document = indexSearcher.doc(topDocs.scoreDocs[i].doc);
      for (String fieldName : fieldNames) {
        System.out.println("\t" + document.get(fieldName));
      }
    }
  }

  /**
   * render search result with {@link Explanation}
   * @param query
   * @param indexSearcher
   * @param topDocs
   * @param fieldNames
   * @throws IOException
   */
  public static final void renderSearchResultWithExplain(//
      Query query, //
      IndexSearcher indexSearcher, //
      TopDocs topDocs, //
      String... fieldNames//
      ) throws IOException {
    System.out.println(StringUtils.REPEAT("=", 100));

    System.out.println("Total Hits: " + topDocs.totalHits);

    for (int i = 0; i < topDocs.totalHits; i++) {
      System.out.println("Score: " + topDocs.scoreDocs[i].score);
      Document document = indexSearcher.doc(topDocs.scoreDocs[i].doc);

      for (String fieldName : fieldNames) {
        // handle multiple valued filed
        String[] fieldValuess = document.getValues(fieldName);
        System.out.println("\tMULTIPLE VALUED FILED? " + (fieldValuess.length > 1));
        System.out.println("Text: ");
        if (fieldValuess.length > 0) {
          for (int j = 0; j < fieldValuess.length; j++) {
            System.out.println("\t" + fieldValuess[j]);
          }
        }
        // System.out.println("\t" + document.get(fieldName));
      }
      System.out.println("Explain: ");
      System.out.println(indexSearcher.explain(query, topDocs.scoreDocs[i].doc));
    }

    System.out.println(StringUtils.REPEAT("=", 100));
  }
}
