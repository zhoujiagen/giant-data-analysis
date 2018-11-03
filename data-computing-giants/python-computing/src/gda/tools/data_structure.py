# -*- coding: utf-8 -*-

"""
样本数据结构.
@author: zhoujiagen
Created on 02/11/2018 6:14 PM
"""
import random
from enum import Enum
import numbers
import math


# ---------------------------------------------------------------------------
# Data Structure
#
# Although list is simple for machine, self descriptive is critical for human.
# ---------------------------------------------------------------------------
class CompareOperation(Enum):
    """比较操作"""
    EQ = 'EQ'
    LT = 'LT'
    LTE = 'LTE'
    GT = 'GT'
    GTE = 'GTE'


def get_attr_counts(datas, attr):
    """
    不同属性值的数量.
    :param datas:
    :type datas: list[BaseDataSample]
    :param attr:
    :type attr: str
    :return:
    """
    results = {}

    for data in datas:
        value = data.get_value(attr)
        if isinstance(value, list):
            for v in value:
                results.setdefault(attr + "-" + v, 0)
                results[attr + "-" + v] += 1
        else:
            results.setdefault(value, 0)
            results[value] += 1

    return results


class BaseDataSample(object):
    """
    :type value_dict: dict[str, object]
    :type key_compare_op_dict: dict[str, CompareOperation]
    :type target_attr: str
    """

    def __init__(self, target_attr='target'):
        """
        数据样本基类.
        :param target_attr: 目标属性
        """
        self.value_dict = dict()  # 属性键值字典
        self.key_compare_op_dict = dict()  # 属性键值比较操作符字典
        self.target_attr = target_attr

    def get_target_key(self):
        return self.target_attr

    def get_length(self):
        return len(self.get_attr_keys())

    def get_attr_keys(self):
        result = set(self.value_dict.keys())
        result.discard(self.get_target_key())
        return result

    def get_all_attr_keys(self):
        return self.value_dict.keys()

    def get_target_value(self):
        return self.get_value(self.get_target_key())

    def get_value(self, key):
        return self.value_dict[key]

    def get_key_compare_op(self, key):
        return self.key_compare_op_dict[key]

    def set_value(self, attr, value, compare_op=CompareOperation.EQ):
        self.value_dict[attr] = value
        self.key_compare_op_dict[attr] = compare_op


# ---------------------------------------------------------------------------
# Operation on Data Structure
# ---------------------------------------------------------------------------

# ----------------------------------------
#  Operation on samples
# ----------------------------------------

def scale_samples(samples, min_value=999999, max_value=-999999):
    """
    伸缩数据集.
    :param samples:
    :type samples: list[BaseDataSample]
    :param min_value
    :param max_value
    :return:
    :rtype tuple[list[BaseDataSample], function]
    """

    if len(samples) == 0:
        return

    high = dict()
    low = dict()

    first_sample = samples[0]
    attr_keys = first_sample.get_attr_keys()
    target_attr = first_sample.get_target_key()

    for attr in attr_keys:
        high[attr] = min_value
        low[attr] = max_value

    for sample in samples:
        for attr in attr_keys:
            value = sample.get_value(attr)
            if value is None or not isinstance(value, numbers.Real):  # 不处理非数值属性
                continue
            if value < low[attr]:
                low[attr] = value
            if value > high[attr]:
                high[attr] = value

    def scale(scale_attr, scale_value):
        """
        伸缩函数.
        :param scale_attr:
        :type scale_attr: str
        :param scale_value:
        :type scale_value: numbers.Real
        :return:
        :rtype float
        """
        if not isinstance(first_sample.get_value(attr), numbers.Real):
            return scale_value

        if low[scale_attr] == high[scale_attr]:
            return scale_value
        return (scale_value - low[scale_attr]) / (high[scale_attr] - low[scale_attr])

    result = []
    for sample in samples:
        new_sample = BaseDataSample(target_attr=target_attr)
        new_sample.set_value(target_attr, sample.get_target_value())
        for attr in attr_keys:
            value = sample.get_value(attr)
            if value is None or not isinstance(value, numbers.Real):
                continue
            new_sample.set_value(attr, scale(attr, value))
        result.append(new_sample)

    return result, scale


def divide_samples(data, test=0.05):
    """
    拆分数据集: 训练集, 测试集.
    :param data: 数据集
    :type data: list[BaseDataSample]
    :param test: 数据项成为测试集的概率
    :return:
    """
    train_set = []
    test_set = []
    for item in data:
        if random.random() < test:
            test_set.append(item)
        else:
            train_set.append(item)

    return train_set, test_set


# ----------------------------------------
# Operation on vectors
# ----------------------------------------

def vec_dot_product(vec1, vec2):
    """
    向量点积.
    :param vec1:
    :type vec1: list[numbers.Real]
    :param vec2:
    :type vec2: list[numbers.Real]
    :return:
    """
    return sum([vec1[i] * vec2[i] for i in range(len(vec1))])


def vec_length(vec):
    """
    向量长度.
    :param vec:
    :type vec: list[numbers.Real]
    :return:
    """
    return sum([vec[i] for i in range(len(vec))]) ** 0.5


def vec_angle(vec1, vec2):
    """
    向量之间的角度.
    :param vec1:
    :type vec1: list[numbers.Real]
    :param vec2:
    :type vec2:list[numbers.Real]
    :return:
    """
    dp = vec_dot_product(vec1, vec2)
    l1 = vec_length(vec1)
    l2 = vec_length(vec2)
    return math.acos(dp / (l1 * l2))


def matrix_rotate(matrix):
    """
    转置矩阵.
    :param matrix: 矩阵.
    :return:
    """
    new_matrix = []

    row_len = len(matrix)
    column_len = len(matrix[0])

    for i in range(column_len):
        new_row = [matrix[j][i] for j in range(row_len)]
        new_matrix.append(new_row)

    return new_matrix
