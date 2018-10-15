# -*- coding: utf-8 -*-

"""
使用正则表达式处理数据.
"""
import unittest


class TestRegex(unittest.TestCase):
    def test_string(self):
        print("hello, {}".format("regex"))
        print("hello, %s" % "regex")
        value_dict = {"number": 1.234}
        print("hello, number: {number:.4f}".format(**value_dict))

    def test_fuzzy_string(self):
        # https://github.com/seatgeek/fuzzywuzzy
        from fuzzywuzzy import fuzz
        from fuzzywuzzy import process

        print(fuzz.ratio("this is a test", "this is a test!"))
        choices = ["Atlanta Falcons", "New York Jets", "New York Giants", "Dallas Cowboys"]
        print(process.extract("new york jets", choices, limit=2))
        print(process.extractOne("cowboys", choices))

    def test_regular_expression(self):
        import re

        names = "Barack Obama, Ronald Reagan, Nancy Drew"

        # first match
        name_re = "([A-Z]\w+) ([A-Z]\w+)"
        name_match = re.match(name_re, names)
        print(name_match.groups())
        name_match = re.search(name_re, names)
        print(name_match.groups())

        # all match
        name_matches = re.findall(name_re, names)
        print(name_matches)

        # with named group
        name_regex = '(?P<first_name>[A-Z]\w+) (?P<last_name>[A-Z]\w+)'
        for name in re.finditer(name_regex, names):
            print("Meet {}".format(name.groups("first_name")))


if __name__ == '__main__':
    unittest.main()
