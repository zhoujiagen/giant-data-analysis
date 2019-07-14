# -*- coding: utf-8 -*-

"""
网页数据.

urllib
requests: http://docs.python-requests.org/en/latest/
BeautifulSoup: https://www.crummy.com/software/BeautifulSoup/
"""

import unittest


class TestDataWeb(unittest.TestCase):
    def test_urllib(self):
        import urllib.request
        import ssl  # https://stackoverflow.com/questions/27835619/urllib-and-ssl-certificate-verify-failed-error

        headers = {
            'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'}
        request = urllib.request.Request(url="https://www.google.com/search?q=ssl", headers=headers)
        context = ssl._create_unverified_context()
        data = urllib.request.urlopen(request, context=context).read()  # http://localhost:9999/files/archive/spec/2.12/
        print(data[:100])

    def test_requests(self):
        import requests
        data = requests.get("http://localhost:9999/files/archive/spec/2.12/")
        print(data.status_code)
        print(data.content)

    def test_beautifulsoup(self):
        from bs4 import BeautifulSoup
        import requests

        data = requests.get("http://localhost:9999/files/archive/spec/2.12/")
        bs = BeautifulSoup(data.content)
        print(bs)

    def test_lxml(self):
        from lxml import html
        page = html.parse("http://localhost:9999/files/archive/spec/2.12/")
        root = page.getroot()

        print(html.tostring(root))
        for link in root.iterlinks():
            print(link)


if __name__ == '__main__':
    unittest.main()
