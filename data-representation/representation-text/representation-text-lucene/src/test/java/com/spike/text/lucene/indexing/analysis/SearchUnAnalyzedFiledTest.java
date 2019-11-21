package com.spike.text.lucene.indexing.analysis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppAnalyzerUtil;
import com.spike.text.lucene.util.LuceneAppUtil;
import com.spike.text.lucene.util.SearchingMemIndexTestBase;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 4, section = { 7 })
public class SearchUnAnalyzedFiledTest extends SearchingMemIndexTestBase {

  private static final String fld_partnum = "partnum";
  private static final String fldValue_partnum = "Q36";

  private static final String fld_description = "description";
  private static final String fldValue_description = "Illidium Space Modulator";

  /**
   * @throws IOException
   * @throws ParseException
   * @see SimpleAnalyzer
   * @see QueryParser
   * @see PerFieldAnalyzerWrapper
   * @see KeywordAnalyzer
   */
  @Test
  public void searchUnanalyzedField() throws IOException, ParseException {
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(new SimpleAnalyzer(), fldValue_partnum);
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(new SimpleAnalyzer(), fldValue_description);

    IndexSearcher indexSearcher = this.getIndexSearcher();

    Query query = new TermQuery(new Term(fld_partnum, fldValue_partnum));
    TopDocs topDocs = indexSearcher.search(query, 10);
    assertEquals(1, topDocs.totalHits);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, fld_partnum);

    query =
        new QueryParser(fld_description, new SimpleAnalyzer())
            .parse(fld_partnum + ":Q36 AND SPACE");
    assertEquals("+partnum:q +description:space", query.toString());
    topDocs = indexSearcher.search(query, 10);
    assertEquals(0, topDocs.totalHits);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, fld_description);

    Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
    // KeywordAnalyzer treat `Q36` as a complete word,
    // just as tokenized=false in indexing phase
    analyzerPerField.put(fld_partnum, new KeywordAnalyzer());
    PerFieldAnalyzerWrapper analyzerWrapper =
        LuceneAppAnalyzerUtil.constructPerFieldAnalyzerWrapper(new SimpleAnalyzer(),
          analyzerPerField);
    query = new QueryParser(fld_description, analyzerWrapper).parse(fld_partnum + ":Q36 AND SPACE");
    System.out.println(query.toString());
    assertEquals("+partnum:Q36 +description:space", query.toString());
    topDocs = indexSearcher.search(query, 10);
    assertEquals(1, topDocs.totalHits);
    LuceneAppUtil.renderSearchResultWithExplain(query, indexSearcher, topDocs, fld_description);
  }

  @Override
  protected Analyzer defineAnalyzer() {
    return new SimpleAnalyzer();
  }

  @Override
  protected void doIndexing() throws IOException {
    IndexWriter indexWriter = this.indexWriter;

    Document document = new Document();
    document.add(LuceneAppUtil.createStringField(fld_partnum, fldValue_partnum, Store.NO, false,
      IndexOptions.DOCS, true));
    document.add(LuceneAppUtil.createStringField(fld_description, fldValue_description, Store.YES,
      true, IndexOptions.DOCS, true));

    indexWriter.addDocument(document);

  }

}
