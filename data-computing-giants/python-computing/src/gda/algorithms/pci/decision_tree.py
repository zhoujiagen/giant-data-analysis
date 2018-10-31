# -*- coding: utf-8 -*-

"""
决策树.
REF: https://en.wikipedia.org/wiki/Decision_tree_learning
@author: zhoujiagen
Created on 31/10/2018 11:39 AM
"""

from enum import Enum

# ---------------------------------------------------------------------------
# Data and Data Structure
# ---------------------------------------------------------------------------


DEBUG = True

# Referrer, Location, Read FAQ, Pages viewed, Service chosen
# target: Service chosen
my_data = [
    ['slashdot', 'USA', 'yes', 18, 'None'],
    ['google', 'France', 'yes', 23, 'Premium'],
    ['digg', 'USA', 'yes', 24, 'Basic'],
    ['kiwitobes', 'France', 'yes', 23, 'Basic'],
    ['google', 'UK', 'no', 21, 'Premium'],
    ['(direct)', 'New Zealand', 'no', 12, 'None'],
    ['(direct)', 'UK', 'no', 21, 'Basic'],
    ['google', 'USA', 'no', 24, 'Premium'],
    ['slashdot', 'France', 'yes', 19, 'None'],
    ['digg', 'USA', 'no', 18, 'None'],
    ['google', 'UK', 'no', 18, 'None'],
    ['kiwitobes', 'UK', 'no', 19, 'None'],
    ['digg', 'New Zealand', 'yes', 12, 'Basic'],
    ['google', 'UK', 'yes', 18, 'Basic'],
    ['kiwitobes', 'France', 'yes', 19, 'Basic']]


class ServiceChosen(Enum):
    NONE = 'None'
    BASIC = 'Basic'
    PREMIUM = 'Premium'


class CompareOperation(Enum):
    """比较操作"""
    EQ = 'EQ'
    LT = 'LT'
    LTE = 'LTE'
    GT = 'GT'
    GTE = 'GTE'


class UserBehaviour(object):
    def __init__(self, referrer, location, read_faq, pages_viewed, service_chosen):
        self.value_dict = dict()
        self.key_compare_op_dict = dict()
        self.value_dict['referrer'] = referrer
        self.key_compare_op_dict['referrer'] = CompareOperation.EQ
        self.value_dict['location'] = location
        self.key_compare_op_dict['location'] = CompareOperation.EQ
        if 'yes' == read_faq:
            self.value_dict['read_faq'] = True
        else:
            self.value_dict['read_faq'] = False
        self.key_compare_op_dict['read_faq'] = CompareOperation.EQ
        self.value_dict['pages_viewed'] = pages_viewed
        self.key_compare_op_dict['pages_viewed'] = CompareOperation.EQ  # 数值限制为相等比较
        if service_chosen == 'Basic':
            self.value_dict['service_chosen'] = ServiceChosen.BASIC
        elif service_chosen == 'Premium':
            self.value_dict['service_chosen'] = ServiceChosen.PREMIUM
        else:
            self.value_dict['service_chosen'] = ServiceChosen.NONE
        self.key_compare_op_dict['service_chosen'] = CompareOperation.EQ

    def get_length(self):
        return len(self.get_attr_keys())

    def get_attr_keys(self):
        result = set(self.value_dict.keys())
        result.discard(self.get_target_key())
        return result

    def get_target_key(self):
        return 'service_chosen'

    def get_value(self, key):
        return self.value_dict[key]

    def get_key_compare_op(self, key):
        return self.key_compare_op_dict[key]

    def referrer(self):
        return self.value_dict['referrer']

    def location(self):
        return self.value_dict['location']

    def read_faq(self):
        return self.value_dict['read_faq']

    def pages_viewed(self):
        return self.value_dict['pages_viewed']

    def service_chosen(self):
        return self.value_dict['service_chosen'].value

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        return "{} {} {} {}: {}".format(
            self.referrer(), self.location(), self.read_faq(), self.pages_viewed(), self.service_chosen())


class BinaryDecisionTreeNode(object):
    """二叉决策树."""

    def __init__(self,
                 compare_key=None,
                 compare_op=None,
                 compare_value=None,
                 results=None,
                 true_branch=None,
                 false_branch=None):
        """
        :param compare_key: 比较的键名称
        :type compare_key: str
        :param compare_op: 比较操作
        :type compare_op: CompareOperation
        :param compare_value: 比较的键的值
        :type compare_value: int|float|Enum
        :param results: 叶子节点中决策结果
        :type results: dict
        :param true_branch: 比较后为真的分支节点
        :type true_branch: BinaryDecisionTreeNode
        :param false_branch: 比较后为假的分支节点
        :type false_branch: BinaryDecisionTreeNode
        """
        self.compare_key = compare_key
        self.compare_op = compare_op
        self.compare_value = compare_value
        self.results = results
        self.true_branch = true_branch
        self.false_branch = false_branch

    def __str__(self):
        if self.results is not None:
            return "{}".format(self.results)
        else:
            return "{} {} {}".format(self.compare_key, self.compare_op.value, self.compare_value)


# ---------------------------------------------------------------------------
# Algorithm methods
# ---------------------------------------------------------------------------


def get_data():
    """
    获取数据.
    :return: 用户行为列表
    :rtype list[UserBehaviour]
    """
    return list(map(lambda l: UserBehaviour(l[0], l[1], l[2], l[3], l[4]), my_data))


def show_data(datas):
    from prettytable import PrettyTable
    table = PrettyTable(['Referrer', 'Location', 'Read FAQ', 'Pages viewed', 'Service chosen'])
    table.align = "l"

    for data in datas:
        table.add_row([data.referrer(), data.location(), data.read_faq(), data.pages_viewed(), data.service_chosen()])
    print(table)


def divide_set(datas, attr_key, compare_value):
    """
    拆分集合.
    :param datas:
    :type datas: list[UserBehaviour]
    :param attr_key:
    :type attr_key: str
    :param compare_value:
    :type compare_value: int|float|Enum
    :return:
    """

    def filter_function(user_behaviour, compare_op):
        """
        过滤函数
        :param user_behaviour:
        :type user_behaviour UserBehaviour
        :param compare_op:
        :type compare_op: CompareOperation
        :return:
        """
        to_compare_value = user_behaviour.get_value(attr_key)
        if compare_op is CompareOperation.EQ:
            return to_compare_value == compare_value
        if compare_op is CompareOperation.LT:
            return to_compare_value < compare_value
        if compare_op is CompareOperation.LTE:
            return to_compare_value <= compare_value
        if compare_op is CompareOperation.GT:
            return to_compare_value > compare_value
        if compare_op is CompareOperation.GTE:
            return to_compare_value >= compare_value

    if len(datas) == 0 or attr_key not in datas[0].get_attr_keys():
        return None

    compare_op = datas[0].get_key_compare_op(attr_key)
    return ([ub for ub in datas if filter_function(ub, compare_op)],
            [ub for ub in datas if not filter_function(ub, compare_op)], compare_op,)


def get_target_counts(datas):
    """不同目标值的数量"""
    results = {}

    for data in datas:
        results.setdefault(data.service_chosen(), 0)
        results[data.service_chosen()] += 1

    return results


def gini_impurity(datas):
    """Gini impurity"""
    counts = get_target_counts(datas)
    sum_of_square_pi = 0.0
    data_size = len(datas)

    for target in counts.keys():
        sum_of_square_pi += (counts[target] / data_size) ** 2

    return 1 - sum_of_square_pi


def entropy(datas):
    """Entropy"""
    from math import log2
    counts = get_target_counts(datas)
    data_size = len(datas)
    result = 0.0
    for target in counts.keys():
        p = counts[target] / data_size
        result = result - p * log2(p)
    return result


def build_tree(datas, score_function=entropy):
    """
    构建二叉决策树.
    :param datas:
    :type datas: list[UserBehaviour]
    :param score_function:
    :type score_function: function
    :return:
    """
    if len(datas) == 0:
        return BinaryDecisionTreeNode()

    data_size = len(datas)
    current_score = score_function(datas)

    best_gain = 0.0
    best_attr = None
    best_attr_compare_op = None
    best_value = None
    best_sets = None

    for attr in datas[0].get_attr_keys():
        column_values = set()
        for data in datas:
            column_values.add(data.get_value(attr))
        for value in column_values:
            # 按每个出现的不同值拆分数据集
            (set1, set2, compare_op) = divide_set(datas, attr, value)
            # 计算信息增益
            p = len(set1) / data_size
            gain = current_score - p * score_function(set1) - (1 - p) * score_function(set2)
            if DEBUG:
                print("p: {}, gain: {}, attr: {}, value: {}".format(p, gain, attr, value))
            if gain > best_gain and len(set1) > 0 and len(set2) > 0:
                best_gain = gain
                (best_attr, best_attr_compare_op) = (attr, compare_op)
                best_value = value
                best_sets = (set1, set2)

                if DEBUG:
                    print(">>> better gain: column_values={}, value={}, \n\t{} {}".format(
                        column_values, value, set1, set2))

    # 创建子分支
    if best_gain > 0:
        true_branch = build_tree(best_sets[0])
        false_branch = build_tree(best_sets[1])
        return BinaryDecisionTreeNode(
            compare_key=best_attr,
            compare_op=best_attr_compare_op,
            compare_value=best_value,
            results=None,
            true_branch=true_branch,
            false_branch=false_branch)
    else:
        return BinaryDecisionTreeNode(results=get_target_counts(datas))


def display_tree(tree):
    """
    打印二叉决策树.
    :param tree:
    :type tree: BinaryDecisionTreeNode
    :return:
    """
    from treelib import Tree

    def build_api_tree(tree, api_tree, parent=None, is_left=False):
        tree_str = str(tree)
        if parent is None:
            parent = api_tree.create_node(tree_str, "0").identifier
        else:
            if is_left:
                parent = api_tree.create_node(tree_str, "L" + parent, parent=parent).identifier
            else:
                parent = api_tree.create_node(tree_str, "R" + parent, parent=parent).identifier
        true_branch = tree.true_branch
        false_branch = tree.false_branch
        if true_branch is not None:
            build_api_tree(true_branch, api_tree, parent=parent, is_left=True)
        if false_branch is not None:
            build_api_tree(false_branch, api_tree, parent=parent, is_left=False)

    api_tree = Tree()
    build_api_tree(tree, api_tree)
    print(api_tree)
