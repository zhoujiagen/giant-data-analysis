package com.spike.text.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

/**
 * 同音词替换{@link Analyzer}
 * @author zhoujiagen<br/>
 *         Sep 14, 2015 10:11:41 PM
 */
public class MetaphoneReplacementAnalyzer extends Analyzer {

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    Tokenizer tokenizer = new LetterTokenizer();
    TokenFilter tokenFilter = new MetaphoneReplacementFilter(tokenizer);

    return new TokenStreamComponents(tokenizer, tokenFilter);
  }

}
