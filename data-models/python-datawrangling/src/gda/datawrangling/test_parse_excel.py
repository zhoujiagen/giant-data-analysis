# -*- coding: utf-8 -*-

"""
解析Excel文件.
"""
import unittest

import xlrd


class TestParseExcel(unittest.TestCase):
    def test_read(self):
        book = xlrd.open_workbook("../../../data/SOWC 2014 Stat Tables_Table 9.xlsx")

        for sheet in book.sheets():
            print(sheet.name)

        sheet = book.sheet_by_name("Table 9 ")
        print(sheet)

        for i in range(sheet.nrows):
            # 行
            row = sheet.row_values(i)
            # Cell
            for cell in row:
                print(cell)


if __name__ == '__main__':
    unittest.main()
