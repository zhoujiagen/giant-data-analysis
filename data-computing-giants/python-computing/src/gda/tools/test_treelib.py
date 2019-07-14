# -*- coding: utf-8 -*-

"""
树工具类
@author: zhoujiagen
Created on 2017-11-01 23:05:33
"""

from treelib import Tree

if __name__ == '__main__':
    tree = Tree()
    tree.create_node("Harry", "harry")  # root node
    tree.create_node("Jane", "jane", parent = "harry")
    tree.create_node("Bill", "bill", parent = "harry")
    tree.create_node("Diane", "diane", parent = "jane")
    tree.create_node("Mary", "mary", parent = "diane")
    tree.create_node("Mark", "mark", parent = "jane")
    tree.show()
