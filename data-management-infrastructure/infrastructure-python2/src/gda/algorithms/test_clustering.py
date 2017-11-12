# -*- coding: utf-8 -*-

'''
Created on 2017-11-01 16:55:02
测试聚类.
@author: zhoujiagen
'''

import unittest

from treelib import Tree

from gda.algorithms.clustering import hierarchical_clustering, \
    k_means_clustering
from gda.tools.feeds import load_feeds_from_file
from gda.tools.matrix import rotate


def show_cluster_tree(root):
    """展示聚类树.

    Args:
        root: 根节点ClusterTreeNode.

    Returns:
        None

    Raises:
        None
    """
    tree = Tree()
    root_apinode = tree.create_node('[' + str(root.nid) + ']')

    def create_node(node, apiparent=None):
        """内部辅助创建API节点函数."""
        tag = '[' + str(node.nid) + ']'
        if node.data != '':
            tag += ': ' + node.data
        tag += '(' + str(node.distance) + ')'
        apinode = tree.create_node(tag, parent=apiparent)

        left = node.left
        right = node.right
        if left is None and right is None:
            return
        if left:
            create_node(left, apinode)
        if right:
            create_node(right, apinode)

    left = root.left
    right = root.right
    if left:
        create_node(left, root_apinode)
    if right:
        create_node(right, root_apinode)

    tree.show()
    with open('tree.txt', 'w') as out:
        out.write(tree.reader.encode('utf-8'))


class TestHierarchicalClustering(unittest.TestCase):
    """层次聚类"""
    def test_doc(self):
        """文档的聚类"""
        docs, _, matrix = load_feeds_from_file('../tools/feeddata.txt')
        root = hierarchical_clustering(matrix, docs)
        show_cluster_tree(root)
        self.assertTrue(1 == 1)

    def test_word(self):
        """单词的聚类"""
        # CAUTION: too slow
        _, words, matrix = load_feeds_from_file('../tools/feeddata.txt')
        rotated_matrix = rotate(matrix)
        word_root = hierarchical_clustering(rotated_matrix, words)
        show_cluster_tree(word_root)
        self.assertTrue(1 == 1)


class TestKMeansClustering(unittest.TestCase):
    """K均值聚类"""
    def test_clustering(self):
        """测试K均值聚类"""
        docs, _, matrix = load_feeds_from_file('../tools/feeddata.txt')
        k_cluster = k_means_clustering(matrix, k=4)
        for cluster_index, doc_list in k_cluster.items():
            print 'Cluster %d' % cluster_index
            print [docs[i] for i in doc_list if len(doc_list) > 0]
            print
        self.assertTrue(1 == 1)
