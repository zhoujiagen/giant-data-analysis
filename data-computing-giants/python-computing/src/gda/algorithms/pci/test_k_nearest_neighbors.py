# -*- coding: utf-8 -*-

import unittest

import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

import random

from gda.algorithms.pci.k_nearest_neighbors import wine_price \
    , wine_set1, wine_set3, knn_estimate, knn_weighted_estimate \
    , cumulative_graph, smooth_prob_graph

# fix the data for ease of debug
random.seed(1)


class TestKNN(unittest.TestCase):
    def test_wine_price(self):
        ratings = np.arange(0., 100., 1)
        ages = np.arange(0., 100., 1)
        size = len(ratings)
        wine_prices = np.array([wine_price(rating, age) for rating in ratings for age in ages]).reshape(size, size)
        ratings, ages = np.meshgrid(ratings, ages)
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.plot_surface(ratings, ages, wine_prices)

        plt.show()
        plt.close()

    def test_wine_price2(self):
        ratings = np.arange(0., 100., 1)
        ages = np.arange(0., 100., 1)
        # 将标量函数向量化
        v_wine_price = np.vectorize(wine_price)
        ratings, ages = np.meshgrid(ratings, ages)
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.plot_surface(ratings, ages, v_wine_price(ratings, ages))

        plt.show()
        plt.close()

    def test_knn_estimate(self):
        data = wine_set1()
        vec1 = (45.0, 12.0)
        print(wine_price(*vec1), knn_estimate(data, vec1, k=10))
        vec2 = (70.0, 12.0)
        print(wine_price(*vec2), knn_estimate(data, vec2, k=10))

    def test_knn_weighted_estimate(self):
        data = wine_set1()
        vec1 = (45.0, 12.0)
        print(wine_price(*vec1), knn_estimate(data, vec1, k=10), knn_weighted_estimate(data, vec1, k=10))
        vec2 = (70.0, 12.0)
        print(wine_price(*vec2), knn_estimate(data, vec2, k=10), knn_weighted_estimate(data, vec2, k=10))

    def test_cumulative_graph(self):
        data = wine_set3()
        vec1 = (5.0, 5.0)
        cumulative_graph(data, vec1, 100)

    def test_smooth_prob_graph(self):
        data = wine_set3()
        vec1 = (5.0, 5.0)
        smooth_prob_graph(data, vec1, 100)


if __name__ == '__main__':
    unittest.main()
