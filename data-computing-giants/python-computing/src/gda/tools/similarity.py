# -*- coding: utf-8 -*-  

"""
相似度计算.
@author: zhoujiagen
Created on 2017-11-01 18:54:05
"""


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
