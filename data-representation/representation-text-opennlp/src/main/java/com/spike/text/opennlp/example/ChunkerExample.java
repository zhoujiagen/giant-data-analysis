package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * Chunk
 * @author zhoujiagen
 */
public final class ChunkerExample {
  private static final Logger LOG = LoggerFactory.getLogger(SentenceDetectorExample.class);

  public static final String MODEL_NAME = "en-chunker.bin";

  public static void main(String[] args) {
    tool();
  }

  static void tool() {

    try (FileInputStream modelStream = OpenNLPModels.stream(MODEL_NAME);) {
      ChunkerModel model = new ChunkerModel(modelStream); // 生成模型
      ChunkerME tool = new ChunkerME(model); // 根据模型加载工具

      String sent[] =
          new String[] { "Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
              "signed", "a", "tentative", "agreement", "extending", "its", "contract", "with",
              "Boeing", "Co.", "to", "provide", "structural", "parts", "for", "Boeing", "'s",
              "747", "jetliners", "." };

      String pos[] =
          new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ",
              "NN", "VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP",
              "POS", "CD", "NNS", "." };

      String tag[] = tool.chunk(sent, pos);

      LOG.info(OpenNLPOutputs.render(ChunkerExample.class.getSimpleName(), sent, tag));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }
}
