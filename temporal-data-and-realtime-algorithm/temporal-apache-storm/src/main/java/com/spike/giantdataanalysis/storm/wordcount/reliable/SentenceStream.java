package com.spike.giantdataanalysis.storm.wordcount.reliable;

import java.io.Serializable;

/**
 * 模拟语句流
 * @author zhoujiagen
 */
public class SentenceStream implements Serializable {
  private static final long serialVersionUID = -3461377598768779296L;

  public static final String[] SENTENCES = {//
      "my dog has fleas",//
          "i like cold beverages",//
          "the dog ate my homework",//
          "don't have a cow man",//
          "i don't think i like fleas" };

  private static int SENTENCES_LEN = SENTENCES.length;
  private static int CURRENT_INDEX = 0;

  public static synchronized String next() {
    String sentence = SENTENCES[CURRENT_INDEX];
    CURRENT_INDEX = (CURRENT_INDEX + 1) % SENTENCES_LEN;
    return sentence;
  }

}
