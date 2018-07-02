package com.spike.text.opennlp.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.google.common.collect.Lists;

public final class PoSConstants {

  private static final PoSConstants INSTANCE = new PoSConstants();

  public static String ENGLISH_NAME_ROOT_PATH = "../text/dataset/english-names/";
  public static String POS_WORD_ROOT_PATH = "../text/dataset/parts-of-speech-word-files/";

  private static List<String> SUBJECTS = Lists.newArrayList();
  private static List<String> VERBS = Lists.newArrayList();
  private static List<String> OBJECTS = Lists.newArrayList();
  private static List<String> EOS = Lists.newArrayList(".", "!", "?");

  // 初始化
  static {
    try (BufferedReader subjectReader =
        new BufferedReader(new FileReader(new File(ENGLISH_NAME_ROOT_PATH, "names.txt")));//
        BufferedReader verbsReader =
            new BufferedReader(new FileReader(new File(POS_WORD_ROOT_PATH,
                "verbs/1syllableverbs.txt")));//
        BufferedReader objectReader =
            new BufferedReader(new FileReader(new File(POS_WORD_ROOT_PATH,
                "nouns/1syllablenouns.txt")));//

    ) {

      String line = null;
      while ((line = subjectReader.readLine()) != null) {
        SUBJECTS.add(line);
      }
      while ((line = verbsReader.readLine()) != null) {
        VERBS.add(line);
      }
      while ((line = objectReader.readLine()) != null) {
        OBJECTS.add(line);
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("初始化POS数据失败.");
    }
  }

  private PoSConstants() {
  }

  public static PoSConstants getInstance() {
    return INSTANCE;
  }

  public List<String> SUBJECTS() {
    return SUBJECTS;
  }

  public List<String> VERBS() {
    return VERBS;
  }

  public List<String> OBJECTS() {
    return OBJECTS;
  }

  public List<String> EOS() {
    return EOS;
  }

}
