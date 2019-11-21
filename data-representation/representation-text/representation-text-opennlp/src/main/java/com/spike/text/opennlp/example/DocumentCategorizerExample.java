package com.spike.text.opennlp.example;

import java.io.FileInputStream;
import java.io.IOException;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.text.opennlp.support.OpenNLPModels;
import com.spike.text.opennlp.support.OpenNLPOutputs;

/**
 * <pre>
 * 文档分类
 * 
 * REF: http://technobium.com/sentiment-analysis-using-opennlp-document-categorizer/
 * </pre>
 * @author zhoujiagen
 */
public final class DocumentCategorizerExample {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentCategorizerExample.class);

  // content is copied from tweets.txt
  public static final String TRAIN_DATA_NAME = "en-doccat.train";
  public static final String NEW_MODEL_NAME = "en-doccat.bin";
  public static final String LANG_CODE = "en";

  public static void main(String[] args) {
    // train();
    tool();
  }

  static void train() {
    try (ObjectStream<String> objectStream = OpenNLPModels.objectStream(TRAIN_DATA_NAME);//
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(objectStream);//
    ) {

      TrainingParameters mlParams = TrainingParameters.defaultParams();
      DoccatFactory factory = new DoccatFactory();
      DoccatModel newModel =
          DocumentCategorizerME.train(LANG_CODE, sampleStream, mlParams, factory);

      OpenNLPModels.store(newModel, NEW_MODEL_NAME);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  static void tool() {

    try (FileInputStream modelStream = OpenNLPModels.streamInTrain(NEW_MODEL_NAME);) {
      DoccatModel model = new DoccatModel(modelStream); // 生成模型
      DocumentCategorizerME tool = new DocumentCategorizerME(model); // 根据模型加载工具

      String[] text = new String[] { "Have", "a", "nice", "day", "!" };
      double[] outcome = tool.categorize(text);
      String result = tool.getBestCategory(outcome);
      LOG.info(OpenNLPOutputs.render(DocumentCategorizerExample.class.getSimpleName(),
        new String[] { result }));

    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }

  }
}
