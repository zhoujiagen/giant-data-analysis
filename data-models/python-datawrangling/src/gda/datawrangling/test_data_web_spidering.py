# -*- coding: utf-8 -*-

"""
网络爬虫.
"""
import unittest

import scrapy
from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor


# use Chrome Dev Tools to extract element's XPath
# run with
# $ bin/scrapy runspider ../src/gda/datawrangling/test_data_web_spidering.py
class PythonPackageItem(scrapy.Item):
    package_name = scrapy.Field()
    version_number = scrapy.Field()
    package_downloads = scrapy.Field()
    package_page = scrapy.Field()
    package_short_description = scrapy.Field()
    home_page = scrapy.Field()
    python_versions = scrapy.Field()
    last_month_downloads = scrapy.Field()


class PythonPackageSpider(CrawlSpider):
    name = "python-package"
    allowed_domains = ['pypi.python.org']
    start_urls = [
        'https://pypi.org/project/scrapely/', ]
    rules = (
        Rule(LinkExtractor(
            allow=['/pypi/[\w-]+/[\d\.]+', ],
            restrict_xpaths=['//table/tr/td', ], ),
            follow=True,
            callback='parse_package', ),)

    def grab_data(self, response, xpath_sel):
        data = response.xpath(xpath_sel).extract()

        if len(data) > 1:
            return data
        elif len(data) == 1:
            if data[0].isdigit():
                return int(data[0])
        return data[0]

    def parse(self, response):
        item = PythonPackageItem()
        item['package_page'] = response.url
        # response.xpath('//div[@class="section"]/h1/text()').extract()
        item['package_name'] = list(
            map(lambda x: x.strip(), response.xpath('//*[@id="content"]/section[1]/div/div[1]/h1/text()').extract()))
        item['package_short_description'] = response.xpath('//meta[@name="description"]/@content').extract()
        item['home_page'] = response.xpath('//*[@id="content"]/section[3]/div/div/div[1]/div[2]/a/@href').extract()
        item['python_versions'] = []
        versions = response.xpath('//li/a[contains(text(), ":: Python ::")]/text()').extract()
        for v in versions:
            version_number = v.split("::")[-1]
            item['python_versions'].append(version_number.strip())
        item['last_month_downloads'] = response.xpath('//li/text()[contains(., "month")]/../span/text()').extract()
        item['package_downloads'] = response.xpath(
            '//table/tr/td/span/a[contains(@href,"pypi.python.org")]/@href').extract()
        return item


class TestDataWebSpidering(unittest.TestCase):
    def test_python_package(self):
        spider = PythonPackageSpider()
        spider.start_requests()


if __name__ == '__main__':
    unittest.main()
