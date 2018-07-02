package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.LemmaSampleStream;
import opennlp.tools.lemmatizer.LemmatizerFactory;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * Lemmatisation
 * @author zhoujiagen
 */
public final class LemmatizerExample {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentCategorizerExample.class);

  public static final String TRAIN_DATA_NAME = "en-lemmatizer.train";
  public static final String NEW_MODEL_NAME = "en-lemmatizer.bin";
  public static final String DICT_DATA_NAME = "en-lemmatizer.dict";// for DictionaryLemmatizer
  public static final String LANG_CODE = "en";

  public static void main(String[] args) {
    // train();
    // tool();
    tool2();
  }

  // WARNING: Insufficient training data to create model.
  // NO WORKAROUND EXCEPT THE DATA.
  static void train() {
    try (ObjectStream<String> objectStream = OpenNLPModels.objectStream(TRAIN_DATA_NAME);//
        LemmaSampleStream sampleStream = new LemmaSampleStream(objectStream);//
    ) {

      // 构造训练参数
      // (1) load config file: CmdLineUtil.loadTrainingParameters(paramFile, false);
      // (2) ModelUtil.createDefaultTrainingParameters();
      // (3) default
      TrainingParameters mlParams = TrainingParameters.defaultParams();
      mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(1));// for too small train data
      LemmatizerFactory factory = new LemmatizerFactory();
      LemmatizerModel newModel = LemmatizerME.train(LANG_CODE, sampleStream, mlParams, factory);

      OpenNLPModels.store(newModel, NEW_MODEL_NAME);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  static void tool() {
    try (FileInputStream modelStream = OpenNLPModels.streamInTrain(NEW_MODEL_NAME);) {
      LemmatizerModel model = new LemmatizerModel(modelStream); // 生成模型
      LemmatizerME tool = new LemmatizerME(model); // 根据模型加载工具

      String[] tokens =
          new String[] { "Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
              "signed", "a", "tentative", "agreement", "extending", "its", "contract", "with",
              "Boeing", "Co.", "to", "provide", "structural", "parts", "for", "Boeing", "'s",
              "747", "jetliners", "." };

      String[] postags =
          new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ",
              "NN", "VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP",
              "POS", "CD", "NNS", "." };

      String[] lemmas = tool.lemmatize(tokens, postags);
      String[] decodedLemmas = tool.decodeLemmas(tokens, lemmas);
      LOG.info(OpenNLPOutputs.render(LemmatizerExample.class.getSimpleName(), tokens, postags,
        lemmas, decodedLemmas));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }

  // USE DictionaryLemmatizer
  // DATASET:
  // https://raw.githubusercontent.com/richardwilly98/elasticsearch-opennlp-auto-tagging/master/src/main/resources/models/en-lemmatizer.dict
  // REF: http://stackoverflow.com/questions/38982423/opennlp-lemmatization-example
  static void tool2() {

    try (FileInputStream dictStream = OpenNLPModels.streamInTrain(DICT_DATA_NAME);) {
      DictionaryLemmatizer tool = new DictionaryLemmatizer(dictStream);

      String[] tokens =
          new String[] { "Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
              "signed", "a", "tentative", "agreement", "extending", "its", "contract", "with",
              "Boeing", "Co.", "to", "provide", "structural", "parts", "for", "Boeing", "'s",
              "747", "jetliners", "." };

      // or use a PoS Tagger
      String[] postags =
          new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ",
              "NN", "VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP",
              "POS", "CD", "NNS", "." };

      String[] lemmas = tool.lemmatize(tokens, postags);
      LOG.info(OpenNLPOutputs.render(LemmatizerExample.class.getSimpleName(), tokens, postags,
        lemmas));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }
}
