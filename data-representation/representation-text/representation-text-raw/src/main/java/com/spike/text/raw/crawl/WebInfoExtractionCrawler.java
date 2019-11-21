package com.spike.text.raw.crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class WebInfoExtractionCrawler extends WebCrawler {

  private final static Pattern FILTERS = //
      Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    return !FILTERS.matcher(href).matches();
  }

  @Override
  public void visit(Page page) {
    String url = page.getWebURL().getURL();
    System.out.println("URL: " + url);

    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      String html = htmlParseData.getHtml();
      Set<WebURL> links = htmlParseData.getOutgoingUrls();

      System.out.println("Text length: " + text.length());
      // String abbrevText = text.substring(0, text.length() < 100 ? text.length() : 100);
      // System.out.println("Text: " + text + "...");
      System.out.println("Html length: " + html.length());
      // System.out.println("Html: " + html);
      System.out.println("Number of outgoing links: " + links.size());
      System.out.println();

      // write to file
      String fileName = url.replaceAll("/", "_SLASH_").replaceAll(":", "_SEMICOLON_");
      System.out.println("Writing to: " + fileName + "...");
      try (BufferedWriter writer = new BufferedWriter(//
          new FileWriter(new File(WebInfoExtraction.crawlStorageFolder, fileName)));) {
        text = text.replaceAll("(\\s*\\n)+", "\n");// remove blank lines
        writer.write(text);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}