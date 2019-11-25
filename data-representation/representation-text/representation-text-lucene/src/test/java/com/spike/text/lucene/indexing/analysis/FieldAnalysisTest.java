package com.spike.text.lucene.indexing.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Assert;
import org.junit.Test;

import com.spike.text.lucene.analysis.SplitMultipleValuedFieldAnalyzer;
import com.spike.text.lucene.util.LuceneAppAnalyzerUtil;
import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.SearchingMemIndexTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 4, section = { 7 })
public class FieldAnalysisTest extends SearchingMemIndexTestBase {
  // value for #multipleValuedFieldAnalysis()
  private static final String author_fld = "author";
  private static final String author = "McCandless M., Hatcher E., Gospodnetic O.";

  // value for #splitedMultiValuedFieldAnalysis
  private static final String sentence_fld = "content";
  private static final String sentence =
      "it's time to pay incoming tax, return library books on time";

  @Test
  public void differentAnalyzersForEachField() {
    Map<String, Analyzer> analyzerPerField = new HashMap<>();
    analyzerPerField.put(author_fld, new KeywordAnalyzer());
    analyzerPerField.put(sentence_fld, new SplitMultipleValuedFieldAnalyzer());
    PerFieldAnalyzerWrapper analyzerWrapper =
        new PerFieldAnalyzerWrapper(this.analyzer, analyzerPerField);

    Assert.assertTrue(analyzerWrapper instanceof Analyzer);

    analyzerWrapper.close();
  }

  /**
   * when using {@link StandardAnalyzer} should match<br/>
   * while using {@link SplitMultipleValuedFieldAnalyzer} should not match anything
   * @throws IOException
   */
  @Test
  public void splitedMultiValuedFieldAnalysis() throws IOException {
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(this.analyzer, sentence);

    PhraseQuery query = new PhraseQuery();
    query.add(new Term(sentence_fld, "tax".toLowerCase()));
    query.add(new Term(sentence_fld, "return".toLowerCase()));

    // query.setSlop(0);

    IndexSearcher indexSearcher = this.getIndexSearcher();

    TopDocs topDocs = indexSearcher.search(query, 10);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, sentence_fld);
  }

  @Test
  public void multipleValuedFieldAnalysis() throws IOException {
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(this.analyzer, author);

    PhraseQuery query = new PhraseQuery();
    query.add(new Term(author_fld, "M".toLowerCase()));
    query.add(new Term(author_fld, "Hatcher".toLowerCase()));

    // query.setSlop(0);

    IndexSearcher indexSearcher = this.getIndexSearcher();

    TopDocs topDocs = indexSearcher.search(query, 10);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, author_fld);
  }

  @Override
  protected Analyzer defineAnalyzer() {
    // return new StandardAnalyzer();
    return new SplitMultipleValuedFieldAnalyzer();
  }

  @Override
  protected void doIndexing() throws IOException {
    IndexWriter indexWriter = this.indexWriter;

    String[] authors = author.split(",");
    Document document = new Document();
    for (String a : authors) {
      document.add(LuceneAppUtil.createStringField(author_fld, a.trim(), Store.YES, true,
        IndexOptions.DOCS_AND_FREQS_AND_POSITIONS, false));
    }

    String[] sentences = sentence.split(",");
    for (String s : sentences) {
      document.add(LuceneAppUtil.createStringField(sentence_fld, s.trim(), Store.YES, true,
        IndexOptions.DOCS_AND_FREQS_AND_POSITIONS, false));
    }

    indexWriter.addDocument(document);
  }

}
