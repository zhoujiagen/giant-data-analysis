package com.spike.text.lucene.indexing.analysis;

import org.junit.Test;

import com.spike.text.lucene.analysis.MetaphoneReplacementAnalyzer;
import com.spike.text.lucene.util.LuceneAppAnalyzerUtil;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 4, section = { 4 })
public class MetaphoneReplacementAnalyzerTest {
  static final String SENTENCE_1 = "The quick brown fox jumped over the lazy dog";
  static final String SENTENCE_2 = "The quik brown phox jumpd ovvar tha lazi dag";

  @Test
  public void metaphone() {

    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(new MetaphoneReplacementAnalyzer(),
      SENTENCE_1);

    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(new MetaphoneReplacementAnalyzer(),
      SENTENCE_2);
  }

}
