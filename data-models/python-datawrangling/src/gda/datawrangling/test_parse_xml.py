# -*- coding: utf-8 -*-

"""
解析XML文件.
"""
import unittest
from xml.etree import ElementTree as ET
import pprint


class TestParseXML(unittest.TestCase):
    def test_read(self):
        tree = ET.parse("../../../data/data-text.xml")
        root = tree.getroot()

        all_data = []

        data = root.find("Data")  # 查找子节点
        for observation in data:
            record = {}
            for item in observation:
                lookup_key = list(item.attrib.keys())[0]  # 获取节点的属性

                if lookup_key == "Numeric":
                    rec_key = "NUMERIC"
                    rec_value = item.attrib['Numeric']
                else:
                    rec_key = item.attrib[lookup_key]
                    rec_value = item.attrib['Code']

                record[rec_key] = rec_value
            all_data.append(record)

        pprint.pprint(all_data)


if __name__ == '__main__':
    unittest.main()
