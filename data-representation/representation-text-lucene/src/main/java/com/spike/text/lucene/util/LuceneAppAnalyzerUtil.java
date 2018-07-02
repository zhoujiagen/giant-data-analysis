package com.spike.text.lucene.util;

import static com.spike.giantdataanalysis.commons.lang.StringUtils.LP;
import static com.spike.giantdataanalysis.commons.lang.StringUtils.RP;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * Lucene Application Utility, <br/>
 * dedicated user for {@link Analyzer} and related resources
 * @author zhoujiagen<br/>
 *         Sep 4, 2015 5:48:31 PM
 */
public final class LuceneAppAnalyzerUtil {
  private static final String DEFAULT_FILE_NAME = "contents";

  /**
   * render token after analyzer analysised
   * @param analyzer
   * @param text
   */
  public static final void renderTokens(Analyzer analyzer, String text) {
    System.out.println("Using Analyzer[" + analyzer.getClass() + "]");

    try {
      TokenStream tokenStream = analyzer.tokenStream(DEFAULT_FILE_NAME, text);
      tokenStream.reset();

      CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
      // or
      // tokenStream.addAttributeImpl(new CharTermAttributeImpl());

      while (tokenStream.incrementToken()) {
        System.out.print("[" + charTermAttribute.toString() + "] ");
      }
      System.out.println();

    } catch (IOException e) {
      throw new LuceneAppException(
          "render tokens in text with " + analyzer.getClass().getSimpleName() + "failed", e);
    }
  }

  /**
   * render token and its detail informations after analyzer analysised
   * @param analyzer
   * @param text
   */
  public static final void renderTokensWithFullDetails(Analyzer analyzer, String text) {
    System.out.println("Using Analyzer[" + analyzer.getClass() + "]");

    try {
      // get token stream of Analyzer
      TokenStream tokenStream = analyzer.tokenStream(DEFAULT_FILE_NAME, text);
      tokenStream.reset();

      CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
      PositionIncrementAttribute positionIncrementAttribute =
          tokenStream.addAttribute(PositionIncrementAttribute.class);
      OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
      TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);

      int position = 0;
      while (tokenStream.incrementToken()) {
        int increment = positionIncrementAttribute.getPositionIncrement();
        if (increment > 0) {
          position += increment;
          System.out.println();
          System.out.print("POS[" + LP(String.valueOf(position), 4, null) + "]: ");
        }

        System.out.print("[" + RP(charTermAttribute.toString(), 10, null) + ":"
            + RP(String.valueOf(offsetAttribute.startOffset()), 4, null) + "->"
            + RP(String.valueOf(offsetAttribute.endOffset()), 4, null) + ":"
            + RP(typeAttribute.type(), 10, null) + "] ");
      }
      System.out.println();

    } catch (IOException e) {
      throw new LuceneAppException(
          "render tokens in text with " + analyzer.getClass().getSimpleName() + "failed", e);
    }
  }

  /**
   * construct utilities of different analyzers for different fields
   * @param defaultAnalyzer
   * @param analyzerPerField
   * @return
   * @see PerFieldAnalyzerWrapper
   */
  public static final PerFieldAnalyzerWrapper constructPerFieldAnalyzerWrapper(//
      Analyzer defaultAnalyzer, //
      Map<String, Analyzer> analyzerPerField) {
    return new PerFieldAnalyzerWrapper(defaultAnalyzer, analyzerPerField);
  }

}
