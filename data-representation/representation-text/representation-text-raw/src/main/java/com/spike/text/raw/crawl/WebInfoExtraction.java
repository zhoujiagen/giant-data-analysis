package com.spike.text.raw.crawl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.spike.giantdataanalysis.commons.lang.StringUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * <pre>
 * 提取https://www.amazon.cn/中书籍相关信息
 * 
 * https://www.amazon.cn/robots.txt
 * </pre>
 * 
 * @author zhoujiagen
 * @see com.spike.text.raw.Controller
 * @see com.spike.text.raw.MyCrawler
 */
public final class WebInfoExtraction {

  static final String crawlStorageFolder = "./data/crawl/root";
  static final int numberOfCrawlers = 1;
  static final List<String> SEED_URLS = new ArrayList<String>();

  public static void main(String[] args) throws Exception {

    try (BufferedReader reader = new BufferedReader(new FileReader("data/to_crawl_urls.txt"));) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (StringUtils.isNotBlank(line)) {
          SEED_URLS.add(line);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    CrawlConfig config = new CrawlConfig();
    config.setMaxDepthOfCrawling(0); // only current page
    config.setCrawlStorageFolder(crawlStorageFolder);

    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    for (String seedUrl : SEED_URLS) {
      controller.addSeed(seedUrl);
    }

    controller.start(WebInfoExtractionCrawler.class, numberOfCrawlers);
  }
}
