package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * 分词
 * @author zhoujiagen
 */
public final class TokenizerExample {
  private static final Logger LOG = LoggerFactory.getLogger(TokenizerExample.class);

  public static final String MODEL_NAME = "en-token.bin";

  public static void main(String[] args) {
    tool();
  }

  /**
   * <pre>
   * 工具
   * 
   * 命令行: $ bin/opennlp TokenizerME en-token.bin < article.txt > article-tokenized.txt
   * </pre>
   */
  static void tool() {

    try (FileInputStream modelStream = OpenNLPModels.stream(MODEL_NAME);) {
      TokenizerModel model = new TokenizerModel(modelStream); // 生成模型
      Tokenizer tool = new TokenizerME(model); // 根据模型加载工具

      String text = "An input sample sentence.";

      String[] result = tool.tokenize(text);
      LOG.info(OpenNLPOutputs.render(TokenizerExample.class.getSimpleName(), result));

      // or use Span
      
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }

  }
}
