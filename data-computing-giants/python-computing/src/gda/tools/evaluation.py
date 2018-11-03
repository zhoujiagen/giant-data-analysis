# -*- coding: utf-8 -*-

"""
算法评估.
@author: zhoujiagen
Created on 03/11/2018 9:57 AM
"""
from gda.tools.data_structure import BaseDataSample, divide_samples


class Algorithm(object):
    def __init__(self, train_set):
        """
        算法抽象.
        :param train_set:
        :type train_set: list[BaseDataSample]
        """
        self.train_set = train_set

    def guess(self, item):
        """
        预测.
        :param item:
        :type BaseDataSample
        :return:
        """
        pass


class CrossValidation(object):
    def __init__(self, algorithm, data_set):
        """
        交叉验证
        :param algorithm:
        :type algorithm: Algorithm
        :param data_set:
        :type data_set: list[BaseDataSample]
        """
        self.algorithm = algorithm
        self.data_set = data_set

    def cross_validate(self, trials=100, test=0.05):
        """
        交叉验证: 计算算法错误率.
        :param trials: 验证次数
        :param test: 数据项成为测试集的概率
        :return:
        """

        def error(test_set, target_attr='result'):
            """
            测试算法: 计算算法错误率.
            :param test_set: 测试集
            :type test_set: list[BaseDataSample]
            :param target_attr: 数据项的目标属性
            :type target_attr: str
            :return:
            """
            error = 0.0
            for item in test_set:
                guess = self.algorithm.guess(item)
                error += (item.get_target_value() - guess) ** 2

            return error / len(test_set)

        error = 0.0
        for i in range(trials):
            train_set, test_set = divide_samples(self.data_set, test)
            error += self.error(test_set)

        return error / trials
