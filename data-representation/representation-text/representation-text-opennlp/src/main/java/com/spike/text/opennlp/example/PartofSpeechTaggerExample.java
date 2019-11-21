package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * 词性(part of speech, POS)标注示例
 * @author zhoujiagen
 */
public final class PartofSpeechTaggerExample {
  private static final Logger LOG = LoggerFactory.getLogger(PartofSpeechTaggerExample.class);

  public static final String MODEL_NAME = "en-pos-maxent.bin";

  public static void main(String[] args) {
    tool();
  }

  static void tool() {
    try (FileInputStream modelStream = OpenNLPModels.stream(MODEL_NAME);) {
      POSModel model = new POSModel(modelStream); // 生成模型
      POSTaggerME tool = new POSTaggerME(model); // 根据模型加载工具

      String sent = "The quick red fox jumped over the lazy brown dogs.";
      String[] words = SimpleTokenizer.INSTANCE.tokenize(sent);
      String[] result = tool.tag(words); // 工具执行

      LOG.info(OpenNLPOutputs.render(PartofSpeechTaggerExample.class.getSimpleName(), words, result));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }
}
