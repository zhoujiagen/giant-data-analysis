package com.spike.text.lucene.analysis;

import java.io.IOException;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * 同义词替换{@link TokenFilter}
 * @author zhoujiagen<br/>
 *         Sep 14, 2015 10:11:03 PM
 */
public class MetaphoneReplacementFilter extends TokenFilter {
  public static final String METAPHONE = "metaphone";

  // a class from apache commoms codec
  private Metaphone metaphone = new Metaphone();

  private TypeAttribute typeAttribute;
  private CharTermAttribute charTermAttribute;

  protected MetaphoneReplacementFilter(TokenStream input) {
    super(input);
    typeAttribute = addAttribute(TypeAttribute.class);
    charTermAttribute = addAttribute(CharTermAttribute.class);
  }

  @Override
  public boolean incrementToken() throws IOException {
    if (!input.incrementToken()) {
      return false;
    }

    String encoded = metaphone.encode(charTermAttribute.toString());
    // charTermAttribute.append(encoded);
    charTermAttribute.copyBuffer(encoded.toCharArray(), 0, encoded.length());
    typeAttribute.setType(METAPHONE);

    return true;
  }

}
