# -*- coding: utf-8 -*-

import unittest

from gda.algorithms.pci.document_classification import get_words, Classifier, NavieBayesianClassifier

import re


def sample_train(classifier):
    classifier.train('Nobody owns the water.', 'good')
    classifier.train('the quick rabbit jumps fences', 'good')
    classifier.train('buy pharmaceuticals now', 'bad')
    classifier.train('make quick money at the online casino', 'bad')
    classifier.train('the quick brown fox jumps', 'good')


class TestUtilities(unittest.TestCase):

    def test_get_word(self):
        document = "what is real is rational, and what is rational is real."
        print(re.split(r'\W+', document))
        words = get_words(document)
        print(words)

    def test_train(self):
        classifier = Classifier(get_words)
        classifier.train("the quick brown fox jumps over the lazy dog", "good")
        classifier.train("make quick money in the online casino", "bad")
        self.assertEqual(1, classifier.f_count("quick", "good"))
        self.assertEqual(1, classifier.f_count("quick", "bad"))

    def test_f_prob(self):
        classifier = Classifier(get_words)
        sample_train(classifier)
        print(classifier.f_prob("quick", "good"))
        print(classifier.f_prob("quick", "bad"))


class TestNavieBayesianClassifier(unittest.TestCase):

    def test_classifiy(self):
        classifier = NavieBayesianClassifier(get_words)
        sample_train(classifier)

        document = "what is real is rational, and what is rational is real."
        result = classifier.classify(document, default="unknown")
        print(result)


if __name__ == '__main__':
    unittest.main()
