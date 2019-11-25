package com.spike.text.lucene.indexing.analysis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.junit.Test;

import com.spike.text.lucene.util.LuceneAppAnalyzerUtil;
import com.spike.text.lucene.util.anno.BookPartEnum;
import com.spike.text.lucene.util.anno.LuceneInAction2ndBook;

@LuceneInAction2ndBook(part = BookPartEnum.CORE_LUCENE, chapter = 4, section = { 1, 2, 3 })
public class AnalyzerBasicTest {
  static final String SENTENCE_1 = "The quick brown fox jumped over the lazy dog";
  static final String SENTENCE_2 = "XY&Z Corporation - xyz@example.com";

  /**
   * @throws IOException
   * @see WhitespaceAnalyzer
   * @see SimpleAnalyzer
   * @see StopAnalyzer
   * @see StandardAnalyzer
   */
  @Test
  public void commonAnalyzers() throws IOException {
    Analyzer[] analyzers =
        new Analyzer[] { new WhitespaceAnalyzer(), new SimpleAnalyzer(), new StopAnalyzer(),
            new StandardAnalyzer() };
    for (Analyzer analyzer : analyzers) {
      // LuceneAppAnalyzerUtil.renderTokens(analyzer, SENTENCE_2);
      LuceneAppAnalyzerUtil.renderTokensWithFullDetails(analyzer, SENTENCE_1);
      System.out.println();
    }
  }

  /**
   * caution of the order of filters
   */
  @Test
  public void chainOfAnalyzer() {
    // 2 result should not the same
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(new MyStopAnalyzer(), SENTENCE_1);
    LuceneAppAnalyzerUtil.renderTokensWithFullDetails(new MyStopAnalyzer2(), SENTENCE_1);
  }

  static final List<String> stopWordList = Arrays.asList("a", "an", "and", "are", "as", "at", "be",
    "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with");

  /**
   * Customize an {@link Analyzer}
   * @author zhoujiagen<br/>
   *         Sep 14, 2015 9:16:53 PM
   */
  static class MyStopAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
      Tokenizer tokenizer = new LetterTokenizer();

      // ignore cast
      CharArraySet stopWords = StopFilter.makeStopSet(stopWordList);

      // lower case filterthen stop filter
      TokenStream tokenstream = new StopFilter(new LowerCaseFilter(tokenizer), stopWords);
      return new TokenStreamComponents(tokenizer, tokenstream);
    }

  }

  static class MyStopAnalyzer2 extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
      Tokenizer tokenizer = new LetterTokenizer();

      // ignore cast
      CharArraySet stopWords = StopFilter.makeStopSet(stopWordList);

      // stop filter then lower case filter
      TokenStream tokenstream = new LowerCaseFilter(new StopFilter(tokenizer, stopWords));
      return new TokenStreamComponents(tokenizer, tokenstream);
    }
  }
}
