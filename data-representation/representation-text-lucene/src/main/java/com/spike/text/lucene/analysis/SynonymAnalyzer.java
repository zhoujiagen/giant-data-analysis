package com.spike.text.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * 同义词{@link Analyzer}
 * @author zhoujiagen<br/>
 *         Sep 14, 2015 10:30:41 PM
 */
public class SynonymAnalyzer extends Analyzer {
  private SynonymEngine synonymEngine;

  public SynonymAnalyzer(SynonymEngine synonymEngine) {
    this.synonymEngine = synonymEngine;
  }

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {

    Tokenizer tokenizer = new StandardTokenizer();
    TokenStream tokenStream = new SynonymFilter(//
        new StopFilter(//
            new LowerCaseFilter(//
                new StandardFilter(tokenizer)), //
            StopAnalyzer.ENGLISH_STOP_WORDS_SET), //
        this.synonymEngine);

    return new TokenStreamComponents(tokenizer, tokenStream);
  }
}
