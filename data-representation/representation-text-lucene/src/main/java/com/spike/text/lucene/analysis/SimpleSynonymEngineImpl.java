package com.spike.text.lucene.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 内存方式实现的同义词引擎<br/>
 * 单向的同义
 * @author zhoujiagen<br/>
 *         Sep 14, 2015 10:39:33 PM
 */
public class SimpleSynonymEngineImpl implements SynonymEngine {
  private static Map<String, String[]> synonymMap = new HashMap<String, String[]>();

  static {
    synonymMap.put("quick", new String[] { "fast", "speddy" });
    synonymMap.put("jumps", new String[] { "leaps", "hops" });
    synonymMap.put("over", new String[] { "above" });
    synonymMap.put("lazy", new String[] { "apathetic", "sluggish" });
    synonymMap.put("dog", new String[] { "canine", "pooch" });
  }

  protected void addSynonymMap(Map<String, String[]> addtionalSynonymMap) {
    if (synonymMap.size() > 0) {
      synonymMap.putAll(addtionalSynonymMap);
    }
  }

  /**
   * CAUTION: may return null
   */
  @Override
  public String[] getSynonym(String word) throws IOException {
    return synonymMap.get(word);
  }

}
