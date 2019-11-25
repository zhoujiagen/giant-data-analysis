package com.spike.text.opennlp.support;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.spike.giantdataanalysis.commons.lang.RandomUtils;
import com.spike.giantdataanalysis.commons.lang.StringUtils;

import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.model.BaseModel;

/**
 * OpenNLP模型工具类
 * @author zhoujiagen
 */
public final class OpenNLPModels {
  public static String MODEL_ROOT_PATH = "../text/dataset/opennlp-models/";
  public static String TRAIN_ROOT_PATH = "../text/dataset/opennlp-train/";

  /**
   * <pre>
   * 获取模型文件流
   * 
   * 注意: 流关闭操作由调用者执行.
   * </pre>
   * @param modelName 模型文件名称
   * @return
   * @throws FileNotFoundException
   */
  public static FileInputStream stream(String modelName) throws FileNotFoundException {
    File modelFile = new File(MODEL_ROOT_PATH, modelName);
    FileInputStream modelStream = new FileInputStream(modelFile);
    return modelStream;
  }

  /**
   * <pre>
   * 从训练文件夹中获取模型文件流
   * 
   * 注意: 流关闭操作由调用者执行.
   * </pre>
   * @param modelName 模型文件名称
   * @return
   * @throws FileNotFoundException
   */
  public static FileInputStream streamInTrain(String modelName) throws FileNotFoundException {
    File modelFile = new File(TRAIN_ROOT_PATH, modelName);
    FileInputStream modelStream = new FileInputStream(modelFile);
    return modelStream;
  }

  /**
   * <pre>
   * 获取训练数据文件的对象流
   * 
   * 注意: 流关闭操作由调用者执行.
   * </pre>
   * @param trainDataName 训练数据文件名称
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static ObjectStream<String> objectStream(String trainDataName)
      throws FileNotFoundException, IOException {
    Charset charset = Charset.forName("UTF-8");
    ObjectStream<String> lineStream = new PlainTextByLineStream(//
        new MarkableFileInputStreamFactory(new File(TRAIN_ROOT_PATH, trainDataName)), charset);
    return lineStream;
  }

  /**
   * 存储模型
   * @param model 模型
   * @param modelName 指定的模型文件名称
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static void store(BaseModel model, String modelName) throws FileNotFoundException,
      IOException {
    try (OutputStream modelOut =
        new BufferedOutputStream(new FileOutputStream(new File(TRAIN_ROOT_PATH, modelName)));) {

      model.serialize(modelOut);
    }
  }

  /**
   * 生成句子检测训练数据
   * @param sentenceCount 待生成的句子数量
   * @param trainDataName 待生成的训练数据文件名称
   * @throws IOException
   */
  public static void generateSentenceDetectorTrainData(long sentenceCount, String trainDataName)
      throws IOException {
    try (BufferedWriter writer =
        new BufferedWriter(new FileWriter(new File(TRAIN_ROOT_PATH, trainDataName)))) {

      String subject = null, verb = null, object = null, eos = null;
      PoSConstants poSConstants = PoSConstants.getInstance();
      int subjectSize = poSConstants.SUBJECTS().size();
      int verbSize = poSConstants.VERBS().size();
      int objectSize = poSConstants.OBJECTS().size();
      int eosSize = poSConstants.EOS().size();
      for (int i = 0; i < sentenceCount; i++) {
        subject = poSConstants.SUBJECTS().get(RandomUtils.nextInt(0, subjectSize));
        verb = poSConstants.VERBS().get(RandomUtils.nextInt(0, verbSize));
        object = poSConstants.OBJECTS().get(RandomUtils.nextInt(0, objectSize));
        eos = poSConstants.EOS().get(RandomUtils.nextInt(0, eosSize));
        writer.write(subject);
        writer.write(StringUtils.SPACE);
        writer.write(verb);
        writer.write(StringUtils.SPACE);
        writer.write(object);
        writer.write(eos);
        writer.write(StringUtils.NEWLINE);
      }
    }

  }

  public static void main(String[] args) throws IOException {
    generateSentenceDetectorTrainData(10, "en-sent.train");
  }

}
