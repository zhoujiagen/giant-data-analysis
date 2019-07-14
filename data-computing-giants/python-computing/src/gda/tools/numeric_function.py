# -*- coding: utf-8 -*-

"""
数值函数.
@author: zhoujiagen
Created on 02/11/2018 7:12 PM
"""

import math


# ---------------------------------------------------------------------------
# 距离(Distance)函数
# ---------------------------------------------------------------------------

def euclidean(vec1, vec2):
    """
    距离函数: 欧几里得距离.
    :param vec1:
    :type vec1: list[numbers.Real]
    :param vec2:
    :type vec2: list[numbers.Real]
    :return:
    """
    d = 0.0
    for i in range(len(vec1)):
        d += (vec1[i] - vec2[i]) ** 2
    return math.sqrt(d)


# ---------------------------------------------------------------------------
# 权重(Weight)函数
# ---------------------------------------------------------------------------

def inverse_weight(distance, num=1.0, const=0.1):
    """
    权重函数: 距离的逆.
    :param distance: 距离
    :type distance: float
    :param num: 分子
    :type num: float
    :param const: 常量
    :type const: float
    :return: 权重值
    """
    return num / (distance + const)


def subtract_weight(distance, const=1.0):
    """
    权重函数: 距离的截断.
    :param distance: 距离
    :type distance: float
    :param const: 常量
    :type const: float
    :return: 权重值
    """
    if distance > const:
        return 0
    else:
        return const - distance


def gaussian(distance, sigma=10.0):
    """
    权重函数: 高斯(期望值为0).
    :param distance: 距离
    :type distance: float
    :param sigma: 高斯正太分布标准差
    :type sigma
    :return:
    """
    return math.e ** (-distance ** 2 / (2 * sigma ** 2))


# ---------------------------------------------------------------------------
# 相似度函数
# ---------------------------------------------------------------------------

def pearson(x, y):
    """
    计算两向量的皮尔逊相关系数.

                        Sum(XY) - (Sum(X) * Sum(Y))/N
    --------------------------------------------------------------------
    sqrt( (Sum(X^2) - (Sum(X))^2/N) * (Sum(Y^2) - (Sum(Y))^2/N) )
    :param x: 向量.
    :param y: 向量.
    :return: 向量的皮尔逊相关系数
    :raise None
    """
    if len(x) != len(y):
        return 0

    n = len(x)
    # 和
    sum_x = sum([float(xe) for xe in x])
    sum_y = sum([float(ye) for ye in y])
    # 平方和
    sum_square_x = sum([xe ** 2.0 for xe in x])
    sum_square_y = sum([ye ** 2.0 for ye in y])
    # 乘积和
    sum_product = sum(x[i] * y[i] for i in range(n))

    formula_numerator = sum_product - (sum_x * sum_y / n)
    formula_denominator = ((sum_square_x - pow(sum_x, 2) / n) *
                           (sum_square_y - pow(sum_y, 2) / n)) ** 0.5

    if formula_denominator == 0:
        return 1
    return formula_numerator / formula_denominator
