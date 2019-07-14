# -*- coding: utf-8 -*-

"""
文档分类.
@author: zhoujiagen
Created on 30/10/2018 10:08 AM
"""

import re

from gda.algorithms.pci import STOP_WORDS

DEBUG = True


def get_words(document):
    """
    获取文档中单词
    :param document: 文档.
    :type document: str
    :return: 文档中独立单词集合.
    :rtype list(tuple(str, int))
    """
    # 限制: 非停用词, 恰当的长度
    words = [s.lower() for s in re.split(r'\W+', document)
             if s.lower() not in STOP_WORDS and 2 < len(s) < 20]
    return dict([(word, 1) for word in words])


class Classifier(object):
    def __init__(self, get_features, file_name=None):
        """
        分类器.
        :param get_features: 获取特征的函数
        :type get_features: function
        :param file_name:
        :type file_name: str
        """
        self.fc = {}  # {feature: {category: feature-count}}
        self.cc = {}  # {category: document-count}
        self.get_features = get_features
        self.thresholds = {}  # the best prob cat should greater than threshold * next best prob

    def get_threshold(self, cat):
        if cat not in self.thresholds.keys():
            return 1.0
        else:
            return self.thresholds[cat]

    def set_threshold(self, cat, threshold):
        self.thresholds[cat] = threshold

    def inc_f(self, f, cat):
        """增加特征和类别对的计数."""
        self.fc.setdefault(f, {})
        self.fc[f].setdefault(cat, 0)
        self.fc[f][cat] += 1

    def inc_c(self, cat):
        """增加类别中文档计数."""
        self.cc.setdefault(cat, 0)
        self.cc[cat] += 1

    def f_count(self, f, cat):
        """特征在类别中出现的次数."""
        if f in self.fc and cat in self.fc[f]:
            return float(self.fc[f][cat])
        return 0.0

    def cat_count(self, cat):
        """类别中文档的数量."""
        if cat in self.cc:
            return float(self.cc[cat])
        return 0

    def total_count(self):
        """文档总数."""
        return sum(self.cc.values())

    def categories(self):
        """所有类别."""
        return self.cc.keys()

    def train(self, document, cat):
        """训练."""
        features = self.get_features(document)
        for f in features:
            self.inc_f(f, cat)

        self.inc_c(cat)

    def f_prob(self, f, cat):
        """概率: 特征. 单词在类别的文档中出现次数 / 类别中文档数量."""
        if self.cat_count(cat) == 0.0:
            return 0.0
        else:
            return self.f_count(f, cat) / self.cat_count(cat)

    def weighted_prob(self, f, cat, prf, weight=1.0, ap=0.5):
        """带权重的概率."""
        basic_prob = prf(f, cat)  # 基础概率
        totals = sum([self.f_count(f, c) for c in self.categories()])
        bp = ((weight * ap) + (totals * basic_prob)) / (weight + totals)
        return bp

    def classify(self, document, default=None):
        """分类."""
        probs = {}

        max_prob = 0.0
        best_category = None
        for cat in self.categories():
            probs[cat] = self.prob(document, cat)
            if probs[cat] > max_prob:
                max_prob = probs[cat]
                best_category = cat

        if DEBUG:
            import pprint
            pprint.pprint(probs)
            pprint.pprint(best_category)

        for cat in probs.keys():
            if cat == best_category:
                continue
            if probs[cat] * self.get_threshold(cat) > probs[best_category]: return default

        return best_category, probs[best_category]


class NavieBayesianClassifier(Classifier):
    """朴素贝叶斯分类器.
    Pr(Category|Document) = Pr(Document|Category) * Pr(Category) / Pr(Document)
    """

    def doc_prob(self, document, cat):
        """概率: 文档的概率. Pr(Document|Category) = product of Pr(Word|Category)"""
        features = self.get_features(document)
        p = 1
        for f in features:
            p *= self.weighted_prob(f, cat, self.f_prob)
        return p

    def prob(self, item, cat):
        """Pr(Document|Category) * Pr(Category)"""
        cat_prob = self.cat_count(cat) / self.total_count()  # Pr(Category)
        doc_prob = self.doc_prob(item, cat)
        return doc_prob * cat_prob
