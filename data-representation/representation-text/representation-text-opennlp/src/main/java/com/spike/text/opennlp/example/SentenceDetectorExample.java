package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.ml.SequenceTrainer;
import opennlp.tools.ml.model.AbstractModel.ModelType;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * 句子检测
 * @author zhoujiagen
 */
public final class SentenceDetectorExample {
  private static final Logger LOG = LoggerFactory.getLogger(SentenceDetectorExample.class);

  public static final String MODEL_NAME = "en-sent.bin";
  public static final String TRAIN_DATA_NAME = "en-sent.train";
  public static final String NEW_MODEL_NAME = "en-sent-new.bin";
  public static final String LANG_CODE = "en";

  public static void main(String[] args) {
    // tool();

    train();
  }

  /**
   * <pre>
   * 工具
   * 
   * 命令行: $ bin/opennlp SentenceDetector en-sent.bin < input.txt > output.txt
   * </pre>
   */
  static void tool() {

    try (FileInputStream modelStream = OpenNLPModels.stream(MODEL_NAME);) {
      SentenceModel model = new SentenceModel(modelStream); // 生成模型
      SentenceDetectorME tool = new SentenceDetectorME(model); // 根据模型加载工具

      String text = "  First sentence. Second sentence. ";

      // String[] result = tool.sentDetect(text);
      // LOG.info(OpenNLPOutputs.render(SentenceDetectorExample.class.getSimpleName(), result));

      // or

      Span[] result = tool.sentPosDetect(text);
      LOG.info(OpenNLPOutputs.render(SentenceDetectorExample.class.getSimpleName(),
        Span.spansToStrings(result, text)));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }

  /**
   * <pre>
   * 训练
   * 
   * WARNING: NOT WORKING!!!
   * 
   * 命令行: $ bin/opennlp SentenceDetectorTrainer -model en-sent.bin -lang en -data en-sent.train -encoding UTF-8
   * </pre>
   */
  static void train() {

    // 生成训练数据
    try {
      OpenNLPModels.generateSentenceDetectorTrainData(10000l, "en-sent.train");
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }

    try (ObjectStream<String> lineStream = OpenNLPModels.objectStream(TRAIN_DATA_NAME);
        ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);) {

      // char[] eosCharacters = new char[] { '.', '?', '!' };
      SentenceDetectorFactory sdFactory = new SentenceDetectorFactory(LANG_CODE, true, null, null);

      /**
       * mlParams.put(TrainingParameters.ALGORITHM_PARAM, "MAXENT");
       * mlParams.put(TrainingParameters.TRAINER_TYPE_PARAM, EventTrainer.EVENT_VALUE);
       * mlParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(100));
       * mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(5));
       */
      TrainingParameters trainingParameters = TrainingParameters.defaultParams();

      trainingParameters.put(TrainingParameters.CUTOFF_PARAM, "2");
      // opennlp.tools.ml.model.AbstractModel.ModelType
      trainingParameters.put(TrainingParameters.ALGORITHM_PARAM, ModelType.Maxent.name()
          .toUpperCase());
      // opennlp.tools.ml.TrainerFactory.TrainerType
      trainingParameters.put(TrainingParameters.TRAINER_TYPE_PARAM, SequenceTrainer.SEQUENCE_VALUE);

      SentenceModel model =
          SentenceDetectorME.train(LANG_CODE, sampleStream, sdFactory, trainingParameters);

      OpenNLPModels.store(model, NEW_MODEL_NAME);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }

  }

  static void evaluate() {
  }

}
