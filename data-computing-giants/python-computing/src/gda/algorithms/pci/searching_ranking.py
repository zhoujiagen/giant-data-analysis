# -*- coding: utf-8 -*-

"""

@author: zhoujiagen
Created on 24/10/2018 5:16 PM
"""

from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor


# use Chrome Dev Tools to extract element's XPath
# run with
# $ bin/scrapy runspider ../src/gda/algorithms/pci/searching_ranking.py
class ScalaSpecCrawler(CrawlSpider):
    name = "scala-spec-crawler"
    allowed_domains = ['localhost']
    start_urls = ['http://localhost:9999/files/archive/spec/2.12/', ]
    rules = (Rule(LinkExtractor(
        allow=['http://localhost:9999/files/archive/spec/2.12/.+', ],
        restrict_xpaths=['//*[@id="chapters"]', ], ),
        follow=True,
        callback='parse'),)

    data = {}

    def parse(self, response):
        for a in response.css("body main ol li a"):
            # print("DEBUG>>> ", a)
            yield response.follow(a, callback=self.parse)

        texts = response.css('body main p::text').extract()
        print("DEBUG>>> ", response.url, texts[:10])
        return response.url, texts
