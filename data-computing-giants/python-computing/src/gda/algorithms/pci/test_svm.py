# -*- coding: utf-8 -*-

import unittest

from gda.algorithms.pci.svm import *
from gda.algorithms.pci.decision_tree import build_tree, display_tree
from gda.tools.data_structure import get_attr_counts, scale_samples


class TestSVM(unittest.TestCase):
    def test_show_data(self):
        samples = load_data(data_type=DataType.AgeOnly)
        show_data(samples)
        samples = load_data(data_type=DataType.Full)
        show_data(samples)

    def test_plot_data(self):
        samples = load_data(data_type=DataType.AgeOnly)
        plot_data(samples)
        samples = load_data(data_type=DataType.Full)
        plot_data(samples)

    def test_decision_tree(self):
        samples = load_data(data_type=DataType.AgeOnly)
        tree = build_tree(samples)
        display_tree(tree)

    def test_get_attr_counts(self):
        samples = load_data(data_type=DataType.Full)
        attrs = samples[0].get_attr_keys()
        print(attrs)
        for attr in attrs:
            print(attr, get_attr_counts(samples, attr))

    def test_scale_sample(self):
        samples = load_data(data_type=DataType.AgeOnly)
        show_data(samples)
        new_samples = scale_samples(samples)
        show_data(new_samples)


if __name__ == '__main__':
    unittest.main()
