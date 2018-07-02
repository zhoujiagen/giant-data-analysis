package com.spike.text.lucene.analysis;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

/**
 * 同义词{@link TokenFilter}
 * @author zhoujiagen<br/>
 *         Sep 14, 2015 10:44:31 PM
 */
public class SynonymFilter extends TokenFilter {
  public static final String TOKEN_TYPE_SYNONYM = "SYNONYM";

  private Stack<String> synonymStack;
  private SynonymEngine synonymEngine;
  private AttributeSource.State currentState;

  private final CharTermAttribute charTermAttribute;
  private final PositionIncrementAttribute positionIncrementAttribute;

  public SynonymFilter(TokenStream input, SynonymEngine synonymEngine) {
    super(input);
    this.synonymStack = new Stack<String>();
    this.synonymEngine = synonymEngine;

    this.charTermAttribute = this.addAttribute(CharTermAttribute.class);
    this.positionIncrementAttribute = this.addAttribute(PositionIncrementAttribute.class);
  }

  @Override
  public boolean incrementToken() throws IOException {
    // handle synonyms
    if (synonymStack.size() > 0) {
      String synonym = synonymStack.pop();
      this.restoreState(currentState);
      charTermAttribute.copyBuffer(synonym.toCharArray(), 0, synonym.length());
      // put in same position
      positionIncrementAttribute.setPositionIncrement(0);

      return true;
    }

    // read next token
    if (!input.incrementToken()) {
      return false;
    }

    if (addAliasToStack()) {
      // store current token
      currentState = this.captureState();
    }

    return true;
  }

  private boolean addAliasToStack() throws IOException {
    String[] synonyms = synonymEngine.getSynonym(charTermAttribute.toString());
    if (synonyms == null) {
      return false;
    }

    // add to stack
    for (String synoym : synonyms) {
      synonymStack.push(synoym);
    }
    return true;
  }
}
