# -*- coding: utf-8 -*-

"""
测试聚类.
@author: zhoujiagen
Created on 2017-11-01 16:55:02
"""

import unittest

from treelib import Tree
from prettytable import PrettyTable
from gda.algorithms.pci import WORKING_DIR
from gda.algorithms.pci.clustering import hierarchical_clustering, \
    k_means_clustering, scale
from gda.tools.feeds import load_feeds_from_file
from gda.tools.matrix import rotate


def show_cluster_tree(root):
    """
    展示聚类树.
    :param root: 根节点ClusterTreeNode.
    :return: None
    :raise None
    """
    tree = Tree()
    root_api_node = tree.create_node('[' + str(root.nid) + ']')

    def create_node(node, api_parent=None):
        """内部辅助创建API节点函数."""
        tag = '[' + str(node.nid) + ']'
        if node.data != '':
            tag += ': ' + node.data
        tag += '(' + str(node.distance) + ')'
        api_node = tree.create_node(tag, parent=api_parent)

        _left = node.left
        _right = node.right
        if _left is None and _right is None:
            return
        if _left:
            create_node(_left, api_node)
        if _right:
            create_node(_right, api_node)

    left = root.left
    right = root.right
    if left:
        create_node(left, root_api_node)
    if right:
        create_node(right, root_api_node)

    tree.show()
    tree.save2file(WORKING_DIR + 'cluster-tree.txt')


class TestHierarchicalClustering(unittest.TestCase):
    """层次聚类"""

    def test_doc(self):
        """文档的聚类"""
        docs, _, matrix = load_feeds_from_file(WORKING_DIR + 'feeddata.txt')
        root = hierarchical_clustering(matrix, docs)
        show_cluster_tree(root)
        self.assertTrue(1 == 1)

    def test_word(self):
        """单词的聚类"""
        # CAUTION: too slow
        _, words, matrix = load_feeds_from_file(WORKING_DIR + 'feeddata.txt')
        rotated_matrix = rotate(matrix)
        word_root = hierarchical_clustering(rotated_matrix, words)
        show_cluster_tree(word_root)
        self.assertTrue(1 == 1)


class TestKMeansClustering(unittest.TestCase):
    """K均值聚类"""

    def test_clustering(self):
        """测试K均值聚类"""
        docs, _, matrix = load_feeds_from_file(WORKING_DIR + 'feeddata.txt')
        k_cluster = k_means_clustering(matrix, k=4)
        for cluster_index, doc_list in k_cluster.items():
            print('Cluster %d' % cluster_index)
            print([docs[i] for i in doc_list if len(doc_list) > 0])
            print()
        self.assertTrue(1 == 1)


class TestScale(unittest.TestCase):

    def test_scale_simple(self):
        data = [[0.4, 0.0, 0.0, 0.0],
                [0.3, 0.1, 0.0, 0.0],
                [0.2, 0.0, 0.1, 0.0],
                [0.1, 0.0, 0.0, 0.1], ]
        coordinate_2d = scale(data)
        import matplotlib.pyplot as plt
        c = ord('A')
        for i in range(len(data)):
            plt.text(coordinate_2d[i][0], coordinate_2d[i][1], chr(c + i))
        plt.show()
        plt.close()

    def test_scale(self):
        docs, _, matrix = load_feeds_from_file(WORKING_DIR + 'feeddata.txt')
        coordinate_2d = scale(data=matrix)

        table = PrettyTable(['doc', 'X', 'Y'])
        n = len(docs)
        for i in range(n):
            table.add_row([docs[i], coordinate_2d[i][0], coordinate_2d[i][1]])
        print(table)
