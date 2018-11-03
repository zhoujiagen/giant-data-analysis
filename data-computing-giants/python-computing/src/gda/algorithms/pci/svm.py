# -*- coding: utf-8 -*-

"""
SVM: support vector machine.
@author: zhoujiagen
Created on 02/11/2018 1:23 PM
"""

from enum import Enum
from gda.algorithms.pci import WORKING_DIR
from prettytable import PrettyTable
import matplotlib.pyplot as plt

from gda.tools.data_structure import BaseDataSample, CompareOperation

# ---------------------------------------------------------------------------
# Data and Data Structure
# ---------------------------------------------------------------------------

DEBUG = True


class DataType(Enum):
    AgeOnly = WORKING_DIR + 'svm_agesonly.csv'
    Full = WORKING_DIR + 'svm_matchmaker.csv'


class Gender(Enum):
    Male = 'M'
    Female = 'F'


def row_to_sample(data_type, row):
    if data_type == DataType.AgeOnly:
        return Sample(data_type=data_type,
                      first=Data(age=int(row[0])),
                      second=Data(age=int(row[1])),
                      target=int(row[2]))
    else:
        first_age = int(row[0])
        if row[1] == 'yes':
            first_is_smoke = True
        else:
            first_is_smoke = False
        if row[2] == 'yes':
            first_want_children = True
        else:
            first_want_children = False
        first_interest = row[3].split(":")
        first_location = row[4]

        second_age = int(row[5])
        if row[6] == 'yes':
            second_is_smoke = True
        else:
            second_is_smoke = False
        if row[7] == 'yes':
            second_want_children = True
        else:
            second_want_children = False
        second_interest = row[8].split(":")
        second_location = row[9]

        return Sample(data_type=data_type,
                      first=Data(age=first_age, is_smoke=first_is_smoke, want_children=first_want_children,
                                 interest=first_interest, location=first_location),
                      second=Data(age=second_age, is_smoke=second_is_smoke, want_children=second_want_children,
                                  interest=second_interest, location=second_location),
                      target=int(row[10]))


class Sample(BaseDataSample):
    def __init__(self, data_type=None, first=None, second=None, target=None):
        """

        :param data_type:
        :type data_type: DataType
        :param first:
        :type first: Data
        :param second:
        :type second: Data
        :param target:
        :type target: int
        """
        BaseDataSample.__init__(self)
        self.data_type = data_type
        self.first = first
        first_value_dict = first.value_dict
        for attr in first_value_dict.keys():
            self.value_dict["1-" + attr] = first_value_dict[attr]
        self.second = second
        second_value_dict = second.value_dict
        for attr in second_value_dict.keys():
            self.value_dict["2-" + attr] = second_value_dict[attr]
        self.target = target
        self.value_dict['target'] = target

        for attr in self.value_dict.keys():
            self.key_compare_op_dict[attr] = CompareOperation.EQ

    def get_target_key(self):
        return 'target'


class Data(BaseDataSample):
    def __init__(self, age=None, is_smoke=None, want_children=None, interest=None,
                 location=None):
        """
        完整数据.
        :param age: 年龄
        :type age: int
        :param is_smoke: 是否抽烟
        :type is_smoke: bool
        :param want_children: 是否要小孩
        :type want_children: bool
        :param interest: 爱好
        :type interest: list[str]
        :param location: 地址
        :type location: str
        """
        BaseDataSample.__init__(self)
        self.age = age
        self.value_dict['age'] = age
        self.is_smoke = is_smoke
        self.value_dict['is_smoke'] = is_smoke
        self.want_children = want_children
        self.value_dict['want_children'] = want_children
        self.interest = interest
        self.value_dict['interest'] = interest
        self.location = location
        self.value_dict['location'] = location

        def get_target_key(self):
            return 'none'


def load_data(data_type=DataType.AgeOnly):
    result = []

    with open(data_type.value) as f:
        for line in f:
            line = line.strip()
            if len(line) == 0:
                continue
            result.append(row_to_sample(data_type, line.split(",")))

    return result


def show_data(samples):
    """
    展示数据.
    :param samples:
    :type samples: list[Sample]
    :return:
    """
    if len(samples) == 0:
        return

    all_attrs = sorted(list(samples[0].get_all_attr_keys()))

    if hasattr(samples[0], 'data_type'):
        data_type = samples[0].data_type
    else:
        data_type = DataType.AgeOnly

    if data_type == DataType.AgeOnly:
        table = PrettyTable(all_attrs)
        for sample in samples:
            values = []
            for attr in all_attrs:
                values.append(sample.get_value(attr))
            table.add_row(values)
    else:
        table = PrettyTable(all_attrs)
        for sample in samples:
            values = []
            for attr in all_attrs:
                values.append(sample.get_value(attr))
            table.add_row(values)

    table.align = "l"
    print(table)


def plot_data(samples):
    """
    展示数据.
    :param samples:
    :type samples: list[Sample]
    :return:
    """
    matched_first = [sample.first.age for sample in samples if sample.target == 1]
    matched_second = [sample.second.age for sample in samples if sample.target == 1]
    unmatched_first = [sample.first.age for sample in samples if sample.target == 0]
    unmatched_second = [sample.second.age for sample in samples if sample.target == 0]
    plt.plot(matched_first, matched_second, 'o')
    plt.plot(unmatched_first, unmatched_second, 'o')
    plt.show()

# ---------------------------------------------------------------------------
# Algorithm methods
# ---------------------------------------------------------------------------
