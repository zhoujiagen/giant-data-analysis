# -*- coding: utf-8 -*-

"""
解析PDF文件.

slate: online parse
pdfminer: tool convert pdf to text
pdftables
"""

import slate3k

import unittest


class TestParsePDF(unittest.TestCase):
    def test_read(self):
        with open("../../../data/EN-FINAL Table 9.pdf", 'rb') as f:
            doc = slate3k.PDF(f)

        for page in doc:
            print(page)


if __name__ == '__main__':
    unittest.main()
