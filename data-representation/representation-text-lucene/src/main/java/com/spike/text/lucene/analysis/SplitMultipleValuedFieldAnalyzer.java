package com.spike.text.lucene.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 * Split multiple valued field analyzer
 * @author zhoujiagen<br/>
 *         Sep 16, 2015 10:22:28 PM
 */
public class SplitMultipleValuedFieldAnalyzer extends Analyzer {

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    Tokenizer tokenizer = new LetterTokenizer();
    TokenFilter tokenFilter = new SplitMultiValuedFieldFilter(tokenizer);

    return new TokenStreamComponents(tokenizer, tokenFilter);
  }

  private static class SplitMultiValuedFieldFilter extends TokenFilter {

    private PositionIncrementAttribute positionIncrementAttribute;

    protected SplitMultiValuedFieldFilter(TokenStream input) {
      super(input);
      positionIncrementAttribute = this.addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
      if (!input.incrementToken()) {
        return false;
      }

      // enough distance(100) to separate
      // positionIncrementAttribute.setPositionIncrement(0);
      positionIncrementAttribute.setPositionIncrement(100);

      return false;
    }

  }

}