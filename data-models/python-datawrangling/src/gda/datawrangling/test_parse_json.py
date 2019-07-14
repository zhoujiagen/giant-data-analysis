# -*- coding: utf-8 -*-

"""
解析JSON文件.
"""

import json
import unittest


class TestParseJSON(unittest.TestCase):
    def test_read(self):
        with open("../../../data/data-text.json") as f:
            data = json.loads(f.read())
            for item in data:
                import pprint

                pprint.pprint(item)


if __name__ == '__main__':
    unittest.main()
