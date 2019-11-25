package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * 名称识别
 * @author zhoujiagen
 */
public final class NameFinderExample {
  private static final Logger LOG = LoggerFactory.getLogger(NameFinderExample.class);

  public static final String MODEL_NAME = "en-ner-person.bin";// 人名模型

  public static void main(String[] args) {
    tool();
  }

  static void tool() {

    try (FileInputStream modelStream = OpenNLPModels.stream(MODEL_NAME);) {
      TokenNameFinderModel model = new TokenNameFinderModel(modelStream); // 生成模型
      NameFinderME tool = new NameFinderME(model); // 根据模型加载工具

      String[] text = new String[] { "Pierre", "Vinken", "is", "61", "years", "old", "." };

      Span[] result = tool.find(text);
      LOG.info(OpenNLPOutputs.render(NameFinderExample.class.getSimpleName(),
        Span.spansToStrings(result, text)));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }

  }
}
