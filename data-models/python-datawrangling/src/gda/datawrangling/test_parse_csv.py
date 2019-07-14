# -*- coding: utf-8 -*-

"""
解析CSV文件.
"""

import csv
import pprint
import unittest
from csv import DictReader
from csv import writer


class TestParseCSV(unittest.TestCase):
    def test_load(self):
        # 加载CSV文件
        with open("../../../data/data-text.csv", "rt") as f:
            reader = csv.reader(f)
            for row in reader:
                print(row)

    def test_DictReader(self):
        # 带键的字典
        header_rdr = DictReader(open("../../../data/data-text.csv", 'rt'))
        header_rows = [h for h in header_rdr]
        pprint.pprint(header_rows)

    def test_write(self):
        with open("../../../data/data-text-output.csv", "wt") as f:
            csv_writer = writer(f)
            title = ["C1", "C2", "C3"]
            csv_writer.writerow(title)
            data = [("11","12","13"),("21","22","12"),]
            csv_writer.writerows(data)


if __name__ == '__main__':
    unittest.main()
