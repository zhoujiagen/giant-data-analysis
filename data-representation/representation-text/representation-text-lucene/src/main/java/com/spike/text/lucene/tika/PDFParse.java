package com.spike.text.lucene.tika;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.spike.giantdataanalysis.commons.lang.StringUtils;
import com.spike.text.lucene.util.LuceneAppConstants;

/**
 * Tika解析pdf示例
 * @author zhoujiagen
 */
public class PDFParse {

  static final String PDF_FILE_PATH = //
      LuceneAppConstants.ROOT_DATA_DIR + "/pdf/tika-sample.pdf";

  public static void main(String[] args) throws IOException, SAXException, TikaException {
    System.out.println("file[" + PDF_FILE_PATH + "] exist? " + new File(PDF_FILE_PATH).exists());

    PDFParser tikaParser = new PDFParser();

    try (InputStream is = new BufferedInputStream(new FileInputStream(PDF_FILE_PATH));) {
      ContentHandler contentHandler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      ParseContext context = new ParseContext();
      tikaParser.parse(is, contentHandler, metadata, context);

      // the content
      // System.out.println(contentHandler.toString());
      System.out.println(StringUtils.REPEAT("-", 500));
      System.out.println("Title:" + metadata.get(TikaCoreProperties.TITLE));
      System.out.println("Content: " + contentHandler.toString().substring(0, 1000) + "...");

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
}
