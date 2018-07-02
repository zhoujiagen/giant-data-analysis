package com.spike.text.lucene.analysis;

import java.io.IOException;

/**
 * 同义词引擎
 * @author zhoujiagen<br/>
 *         Sep 14, 2015 10:31:20 PM
 */
public interface SynonymEngine {

  String[] getSynonym(String word) throws IOException;
}
