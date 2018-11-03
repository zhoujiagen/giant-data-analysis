# -*- coding: utf-8 -*-

"""
数值预测: KNN.
@author: zhoujiagen
Created on 01/11/2018 12:58 PM
"""

import random
import matplotlib.pyplot as plt

# ---------------------------------------------------------------------------
# Data and Data Structure
#
# limitations: no data scale (and related optimize tuning) consideration.
# ---------------------------------------------------------------------------
from gda.tools.numeric_function import euclidean, gaussian

DEBUG = True


def wine_price(rating, age):
    """
    酒价格.
    :param rating: 品级
    :type rating: float
    :param age: 酒龄
    :type age: float
    :return:
    """
    peak_age = rating - 50
    price = rating / 2
    if age > peak_age:
        price = price * (5 - (age - peak_age))
    else:
        if peak_age == 0:
            peak_age = 0.1
        price = price * (5 * ((age + 1) / peak_age))
    if price < 0: price = 0
    return price


def wine_set1():
    """

    :return: [{input: [], result: float}]
    """
    rows = []
    for i in range(300):
        rating = random.random() * 50 + 50
        age = random.random() * 50
        price = wine_price(rating, age)
        price *= (random.random() * 0.4 + 0.8)  # 噪音
        rows.append({'input': [rating, age],
                     'result': price})
    return rows


def wine_set3():
    rows = wine_set1()
    for row in rows:
        if random.random() < 0.5:
            row['result'] *= 0.6
    return rows


# ---------------------------------------------------------------------------
# Algorithm methods
# ---------------------------------------------------------------------------


def get_distances(data, vec, distance_function=euclidean):
    """计算所有距离."""
    result = []
    for i in range(len(data)):
        vec2 = data[i]['input']
        result.append((distance_function(vec, vec2), i))  ## distance, index
    result.sort()
    return result


def knn_estimate(data, vec, k=3):
    """算法: KNN估计."""
    distances = get_distances(data, vec)

    avg = 0.0
    for i in range(k):
        index = distances[i][1]
        avg += data[index]['result']

    return avg / k


def knn_weighted_estimate(data, vec, weight_function=gaussian, k=3):
    """
    算法: 带权重的KNN估计.
    :param data: 数据集
    :param vec: 数据项
    :param weight_function: 权重函数
    :param k: 近邻数量
    :return:
    """
    distances = get_distances(data, vec)

    avg = 0.0
    total_weight = 0.0

    for i in range(k):
        index = distances[i][1]
        distance = distances[i][0]
        weight = weight_function(distance)
        avg += weight * data[index]['result']
        total_weight += weight

    return avg / total_weight


# ----------------------------------------
# 概率的图形展示
# ----------------------------------------

def prob_guess(data, vec, low, high, k=5, weight_function=gaussian):
    """
    概率估计.
    :param data: 数据集
    :param vec: 数据项
    :param low: 下界
    :param high: 上界
    :param k: 近邻数
    :param weight_function: 权重函数
    :return:
    """
    distance_list = get_distances(data, vec)
    in_weight = 0.0
    total_weight = 0.0
    for i in range(k):
        dist = distance_list[i][0]
        idx = distance_list[i][1]
        weight = weight_function(dist)
        v = data[idx]['result']
        if low <= v <= high:
            in_weight += weight
        total_weight += weight
    if total_weight == 0:
        return 0
    return in_weight / total_weight


def cumulative_graph(data, vec, high, k=5, weight_function=gaussian):
    """
    概率累计分布图.
    :param data: 数据集
    :param vec: 数据项
    :param high: 最大值
    :param k: 近邻数
    :param weight_function: 权重函数
    :return:
    """
    import numpy as np
    points = np.arange(0.0, high, 0.1)
    probs = [prob_guess(data, vec, 0, p, k, weight_function) for p in points]
    if DEBUG:
        print(points)
        print(probs)

    plt.plot(points, probs)
    plt.show()


def smooth_prob_graph(data, vec, high, k=5, weight_function=gaussian, sigma=5.0):
    """平滑概率图."""
    import numpy as np
    points = np.arange(0.0, high, 0.1)
    probs = [prob_guess(data, vec, 0, p + 0.1, k, weight_function) for p in points]

    smoothed = []
    for i in range(len(probs)):
        smooth_value = 0.0
        for j in range(len(probs)):
            distance = abs(i - j) * 0.1
            weight = weight_function(distance, sigma)  # hard coded here
            smooth_value += weight * probs[j]
        smoothed.append(smooth_value)

    plt.plot(points, smoothed)
    plt.show()
