package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * 解析
 * @author zhoujiagen
 */
public final class ParserExample {
  private static final Logger LOG = LoggerFactory.getLogger(ParserExample.class);

  public static final String MODEL_NAME = "en-parser-chunking.bin";

  public static void main(String[] args) {
    tool();
  }

  static void tool() {

    try (FileInputStream modelStream = OpenNLPModels.stream(MODEL_NAME);) {
      ParserModel model = new ParserModel(modelStream); // 生成模型
      Parser tool = ParserFactory.create(model); // 根据模型加载工具

      String text = "The quick brown fox jumps over the lazy dog .";

      Parse[] result = ParserTool.parseLine(text, tool, 1);

      StringBuffer sb = new StringBuffer();
      for (Parse res : result) {
        res.show(sb);
      }
      LOG.info(OpenNLPOutputs.render(SentenceDetectorExample.class.getSimpleName(),
        new String[] { sb.toString() }));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }

  }
}
