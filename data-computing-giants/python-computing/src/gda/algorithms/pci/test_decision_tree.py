# -*- coding: utf-8 -*-

import unittest

from gda.algorithms.pci.decision_tree import get_data, show_data, divide_set \
    , get_target_counts, gini_impurity, entropy, build_tree, display_tree
import pprint


class TestDecisionTree(unittest.TestCase):
    def test_get_data(self):
        pprint.pprint(get_data())

    def test_show_data(self):
        datas = get_data()
        show_data(datas)

    def test_isinstance(self):
        self.assertTrue(isinstance(True, int))
        self.assertTrue(isinstance(False, int))

    def test_divide_set(self):
        datas = get_data()
        print(datas)
        (set1, set2, compare_op) = divide_set(datas, 'read_faq', False)
        print(set1)
        print(set2)
        print(compare_op)

    def test_get_keys(self):
        keys = set(['location', 'pages_viewed', 'referrer', 'service_chosen', 'read_faq'])
        print(keys)
        keys.discard('service_chosen')
        print(keys)
        datas = get_data()
        print(datas[0].get_attr_keys())

    def test_target_counts(self):
        datas = get_data()
        print(get_target_counts(datas))

    def test_gini_impurity(self):
        datas = get_data()
        print(gini_impurity(datas))
        (set1, set2, compare_op) = divide_set(datas, 'read_faq', False)
        print(gini_impurity(set1))
        print(gini_impurity(set2))
        print(compare_op)

    def test_entropy(self):
        datas = get_data()
        print(entropy(datas))
        (set1, set2, compare_value) = divide_set(datas, 'read_faq', False)
        print(entropy(set1))
        print(entropy(set2))

    def test_display_tree(self):
        datas = get_data()
        tree = build_tree(datas)
        display_tree(tree)


if __name__ == '__main__':
    unittest.main()
