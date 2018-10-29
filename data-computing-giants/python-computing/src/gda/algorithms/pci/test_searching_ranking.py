# -*- coding: utf-8 -*-

import unittest

from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor
import logging

START_URL = 'http://localhost:9999/files/archive/spec/2.12/'
BASE_PATH = '/files/archive/spec/2.12/'
# NLTK's list of english stopwords: https://gist.github.com/sebleier/554280
STOP_WORDS = ['i', 'me', 'my', 'myself', 'we', 'our', 'ours', 'ourselves', 'you', "you're", "you've", "you'll", "you'd",
              'your', 'yours', 'yourself', 'yourselves', 'he', 'him', 'his', 'himself', 'she', "she's", 'her', 'hers',
              'herself', 'it', "it's", 'its', 'itself', 'they', 'them', 'their', 'theirs', 'themselves', 'what',
              'which', 'who', 'whom', 'this', 'that', "that'll", 'these', 'those', 'am', 'is', 'are', 'was', 'were',
              'be', 'been', 'being', 'have', 'has', 'had', 'having', 'do', 'does', 'did', 'doing', 'a', 'an', 'the',
              'and', 'but', 'if', 'or', 'because', 'as', 'until', 'while', 'of', 'at', 'by', 'for', 'with', 'about',
              'against', 'between', 'into', 'through', 'during', 'before', 'after', 'above', 'below', 'to', 'from',
              'up', 'down', 'in', 'out', 'on', 'off', 'over', 'under', 'again', 'further', 'then', 'once', 'here',
              'there', 'when', 'where', 'why', 'how', 'all', 'any', 'both', 'each', 'few', 'more', 'most', 'other',
              'some', 'such', 'no', 'nor', 'not', 'only', 'own', 'same', 'so', 'than', 'too', 'very', 's', 't', 'can',
              'will', 'just', 'don', "don't", 'should', "should've", 'now', 'd', 'll', 'm', 'o', 're', 've', 'y', 'ain',
              'aren', "aren't", 'couldn', "couldn't", 'didn', "didn't", 'doesn', "doesn't", 'hadn', "hadn't", 'hasn',
              "hasn't", 'haven', "haven't", 'isn', "isn't", 'ma', 'mightn', "mightn't", 'mustn', "mustn't", 'needn',
              "needn't", 'shan', "shan't", 'shouldn', "shouldn't", 'wasn', "wasn't", 'weren', "weren't", 'won', "won't",
              'wouldn', "wouldn't"]


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
        word_sep = r"""[,;.'"\W]"""
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
        import re
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
