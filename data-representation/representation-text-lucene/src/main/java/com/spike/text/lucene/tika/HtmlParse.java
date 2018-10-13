package com.spike.text.lucene.tika;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;


import com.spike.text.lucene.util.LuceneAppConstants;
import com.spike.giantdataanalysis.commons.lang.StringUtils;

/**
 * Tika解析Html示例
 * @author zhoujiagen
 */
public class HtmlParse {
  static final String HTML_FILE_PATH = //
      LuceneAppConstants.ROOT_DATA_DIR + "/html/tika-sample.html";

  public static void main(String[] args) {
    try (InputStream input = new FileInputStream(HTML_FILE_PATH);) {
      BodyContentHandler bch = new BodyContentHandler();
      LinkContentHandler lch = new LinkContentHandler();
      TeeContentHandler handler = new TeeContentHandler(bch, lch);

      Metadata metadata = new Metadata();
      Parser parser = new HtmlParser();
      ParseContext context = new ParseContext();

      parser.parse(input, handler, metadata, context);

      System.out.println("Title: " + metadata.get(TikaCoreProperties.TITLE));
      System.out.println("Body: " + bch.toString());
      System.out.println("Links: " + lch.getLinks());

      System.out.println(StringUtils.REPEAT("-", 500));
      String[] metadataNames = metadata.names();
      for (String mdn : metadataNames) {
        System.out.println(mdn + "=" + metadata.get(mdn));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
