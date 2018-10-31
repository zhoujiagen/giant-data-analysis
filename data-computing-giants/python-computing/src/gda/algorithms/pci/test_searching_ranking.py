# -*- coding: utf-8 -*-

import unittest

from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor
import logging

from gda.algorithms.pci import STOP_WORDS

START_URL = 'http://localhost:9999/files/archive/spec/2.12/'
BASE_PATH = '/files/archive/spec/2.12/'


# run with
# $ bin/scrapy runspider ../src/gda/algorithms/pci/test_searching_ranking.py -o ../workdir/pci/scala-spec.json
class ScalaSpecCrawler(CrawlSpider):
    name = "scala-spec-crawler"
    allowed_domains = ['localhost']
    start_urls = [START_URL, ]
    rules = (Rule(LinkExtractor(
        allow=[START_URL + '.+', ],
        restrict_xpaths=['//*[@id="chapters"]', ], ),
        follow=True,
        callback='parse'),)

    data = {}

    def parse(self, response):
        import re
        word_sep = r"""[,;.'"\W]"""  # r'\W+'
        out_links = []
        for a_in_home in response.css("body main ol li a"):
            logging.info("Found links in home: {}".format(a_in_home))
            content = [word.lower() for word in
                       re.split(word_sep, a_in_home.css("::text").extract()[0].strip())
                       if word.lower() not in STOP_WORDS and word != '']
            url = a_in_home.attrib['href'].replace(START_URL, "").replace(BASE_PATH, "")
            out_links.append({"content": content, "url": url})
            yield response.follow(a_in_home, callback=self.parse)
        for a_in_content in response.css("body main p a"):
            logging.info("Found links in content: {}".format(a_in_content))
            content = [word.lower() for word in
                       re.split(word_sep, a_in_content.css("::text").extract()[0].strip())
                       if word.lower() not in STOP_WORDS and word != '']
            url = a_in_content.attrib['href'].replace(START_URL, "").replace(BASE_PATH, "")
            out_links.append({"content": content, "url": url})
            yield response.follow(a_in_content, callback=self.parse)

        texts = response.css('body main p::text').extract()
        words = []
        for text in texts:
            words.extend([word.lower() for word in re.split(word_sep, text)
                          if word.lower() not in STOP_WORDS and word != ''])
        texts = words

        yield {"url": response.url.replace(START_URL, ""), "texts": texts, "out_links": out_links}


class TestURLParse(unittest.TestCase):
    def test_extract(self):
        from urllib import parse
        url = "http://localhost:9999/files/archive/spec/2.12/04-basic-declarations-and-definitions.html"
        r = parse.urlparse(url)
        print(r)
        print(r.path)
        print(r.path.replace('/', '_'))


if __name__ == '__main__':
    unittest.main()
