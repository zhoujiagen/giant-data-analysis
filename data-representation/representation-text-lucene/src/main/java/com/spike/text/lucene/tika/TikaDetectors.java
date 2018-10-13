package com.spike.text.lucene.tika;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.tika.detect.NameDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import com.spike.giantdataanalysis.commons.lang.StringUtils;
import com.spike.text.lucene.util.LuceneAppConstants;

public class TikaDetectors {
  static final String FILE_PATH = LuceneAppConstants.ROOT_DATA_DIR + "/image/unknown.jpg";

  public static void main(String[] args) throws IOException {
    // minetypes(FILE_PATH);

    // nameDetector(FILE_PATH);

    autoDetector(PDFParse.PDF_FILE_PATH);
  }

  /**
   * 自动检测的解析器
   * @param filepath
   * @throws IOException
   * @see AutoDetectParser
   */
  static void autoDetector(String filepath) throws IOException {
    try (InputStream input = new BufferedInputStream(new FileInputStream(filepath));) {

      BodyContentHandler handler = new BodyContentHandler();
      AutoDetectParser parser = new AutoDetectParser();
      Metadata metadata = new Metadata();
      parser.parse(input, handler, metadata);

      System.out.println("Title:" + metadata.get(TikaCoreProperties.TITLE));
      System.out.println("Content: " + handler.toString().substring(0, 1000) + "...");

      System.out.println(StringUtils.REPEAT("-", 500));

      // meta data
      String[] metadataNames = metadata.names();
      for (String mdn : metadataNames) {
        System.out.println(mdn + "=" + metadata.get(mdn));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 根据名称获取资源类型
   * @param filepath
   * @throws IOException
   * @see NameDetector
   */
  static void nameDetector(String filepath) {
    Map<Pattern, MediaType> patterns = new HashMap<Pattern, MediaType>();
    patterns.put(Pattern.compile(".*\\.jpg"), MediaType.image("jpeg"));

    NameDetector detector = new NameDetector(patterns);
    Metadata metadata = new Metadata();
    metadata.set("resourceName", "unknown.jpg"); //  设置
    try (InputStream paramInputStream = new BufferedInputStream(new FileInputStream(filepath));) {

      MediaType result = detector.detect(paramInputStream, metadata);

      System.out.println(result);

      // meta data
      String[] metadataNames = metadata.names();
      for (String mdn : metadataNames) {
        System.out.println(mdn + "=" + metadata.get(mdn));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 根据媒体类型获取资源类型
   * @param filepath
   * @throws IOException
   */
  static void minetypes(String filepath) {
    MimeTypes tikaDetector = new MimeTypes();

    Metadata metadata = new Metadata();
    try (InputStream input = new BufferedInputStream(new FileInputStream(filepath));) {
      MediaType result = tikaDetector.detect(input, metadata);

      System.out.println(result);

      // meta data
      String[] metadataNames = metadata.names();
      for (String mdn : metadataNames) {
        System.out.println(mdn + "=" + metadata.get(mdn));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
