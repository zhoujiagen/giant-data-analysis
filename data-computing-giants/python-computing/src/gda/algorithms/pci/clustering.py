# -*- coding: utf-8 -*-

"""
聚类(Clustering).
@author: zhoujiagen
Created on 2017-11-01 16:53:49
"""

import random

from gda.tools.similarity import pearson
from math import sqrt


class ClusterTreeNode(object):
    """聚类树节点"""

    def __init__(self, vector, left=None, right=None, distance=0.0, nid=None, data=None):
        """
        聚类树节点.
        :param vector: 单词在文档中词频向量.
        :param left: 左子节点.
        :param right: 右子节点.
        :param distance: 该节点汇总的距离值.
        :param nid: 节点ID.
        :param data: 节点数据, 这里是文档名称.
        """
        self.vector = vector
        self.left = left
        self.right = right
        self.distance = distance
        self.nid = nid
        self.data = data

    def __str__(self):
        return str(self.nid)


def hierarchical_clustering(matrix, labels, vector_distance_func=pearson):
    """
    层次聚类.
    :param matrix: 文档的词频向量矩阵.
    :param labels: 标签, 文档列表, 对应于matrix中行.
    :param vector_distance_func: 词频向量的相似度/距离函数.
    :return: 聚类树的根节点ClusterTreeNode.
    :raise None
    """
    # 最初的聚类是矩阵所有行向量
    nodes = [ClusterTreeNode(matrix[i], nid=i, data=labels[i]) for i in range(len(matrix))]
    vector_len = len(matrix[0])  # 词频向量的长度

    new_node_id = -1  # 新节点的ID
    distance_memo = {}  # 向量距离缓存: {(C1,C2): distance}
    while len(nodes) > 1:
        min_pair = (0, 1)
        min_distance = vector_distance_func(matrix[0], matrix[1])

        for i in range(len(nodes)):
            for j in range(i + 1, len(nodes)):
                distance = 0
                if (nodes[i].nid, nodes[j].nid) not in distance_memo:
                    distance = vector_distance_func(nodes[i].vector,
                                                    nodes[j].vector)
                    distance_memo[(nodes[i].nid, nodes[j].nid)] = distance
                else:
                    distance = distance_memo[(nodes[i].nid, nodes[j].nid)]

                if distance < min_distance:
                    min_distance = distance
                    min_pair = (i, j)
                    print('DEBUG>>> update pair: (%d, %d)' % (i, j))

        # 两个距离最小的节点的平均值
        merge_node = [(nodes[min_pair[0]].vector[i] +
                       nodes[min_pair[1]].vector[i]) / 2.0
                      for i in range(vector_len)]
        new_node = ClusterTreeNode(merge_node,
                                   left=nodes[min_pair[0]],
                                   right=nodes[min_pair[1]],
                                   distance=min_distance,
                                   nid=new_node_id,
                                   data='')
        new_node_id -= 1
        # 移除这两个节点, 加入新节点
        del nodes[min_pair[1]]
        del nodes[min_pair[0]]
        nodes.append(new_node)

    return nodes[0]


def k_means_clustering(matrix, vector_distance_func=pearson, k=4, iteration_count=100):
    """
    K均值聚类.
    :param matrix: 文档的词频向量矩阵.
    :param vector_distance_func: 词频向量的相似度/距离函数.
    :param k: 聚类数量.
    :param iteration_count: 迭代次数.
    :return: {集群标识: [单词词频向量行号]}.
    :raise None
    """
    result = {}  # {集群标识: [单词词频向量行号]}

    row_len = len(matrix)
    column_len = len(matrix[0])
    print("DEBUG>>> row_len=%d, column_len=%d" % (row_len, column_len))

    # 确定每个单词词频的最大值和最小值
    vector_ranges = [(min([row[i] for row in matrix]),
                      max([row[i] for row in matrix]))
                     for i in range(column_len)]

    # 随机生成K个中心点
    cluster_center = \
        [[random.random() * (vector_ranges[i][1] - vector_ranges[i][0]) + vector_ranges[i][0]
          for i in range(column_len)]
         for i in range(k)]

    last_result = None  # 上次迭代的结果
    for iteration in range(iteration_count):
        print('Iteration %d' % iteration)
        for i in range(k):
            result[i] = []  # 一定要清空, 重新计算

        # 寻找每行最匹配的聚类
        for row_index in range(row_len):
            row = matrix[row_index]
            best_cluster = 0  # 最佳匹配的聚类
            for i in range(k):
                distance = vector_distance_func(cluster_center[i], row)
                if distance < vector_distance_func(cluster_center[best_cluster], row):
                    best_cluster = i
            result[best_cluster].append(row_index)

        print("DEBUG>>> result= %s\ncenter=%s\n" % (str(result), str(cluster_center)))
        if last_result == result:  # 两次迭代结果相同, 退出
            break
        last_result = result

        # 更新中心点
        for i in range(k):
            center = [0.0] * column_len
            cluster_node_count = len(result[i])
            if cluster_node_count == 0:  # 聚类中没有元素
                continue

            for row_index in result[i]:
                for column_index in range(column_len):
                    center[column_index] += matrix[row_index][column_index]

            for j in range(column_len):
                center[j] /= cluster_node_count

            cluster_center[i] = center

    return result


def scale(data, distance=pearson, rate=0.01):
    """
    多维向量的二维表示.
    :param data: 多维向量的数组
    :param distance: 向量距离函数.
    :param rate: 调整速率.
    :return: 列分别为二维坐标的向量数组
    """
    n = len(data)
    # 实际距离
    real_dist = [[distance(data[i], data[j]) for j in range(n)] for i in range(0, n)]
    # 随机选择二维中位置
    loc_2d = [[random.random(), random.random()] for i in range(n)]
    # 二维距离计算
    dist_2d = [[0.0 for j in range(n)] for i in range(n)]
    lasterror = None
    for _ in range(0, 1000):  # 迭代次数
        for i in range(n):
            for j in range(n):
                dist_2d[i][j] = sqrt(sum([pow(loc_2d[i][x] - loc_2d[j][x], 2)
                                          for x in range(len(loc_2d[i]))]))

        # 计算梯度
        grad = [[0.0, 0.0] for i in range(n)]
        total_error = 0
        for k in range(n):
            for j in range(n):
                if j == k: continue
                error_term = (dist_2d[j][k] - real_dist[j][k]) / real_dist[j][k]
                grad[k][0] += ((loc_2d[k][0] - loc_2d[j][0]) / dist_2d[j][k]) * error_term
                grad[k][1] += ((loc_2d[k][1] - loc_2d[j][1]) / dist_2d[j][k]) * error_term
                total_error += abs(error_term)
        print(total_error)

        # 结束条件
        if lasterror and lasterror < total_error: break
        lasterror = total_error

        # 更新二维距离
        for k in range(n):
            loc_2d[k][0] -= rate * grad[k][0]
            loc_2d[k][1] -= rate * grad[k][1]

    return loc_2d
