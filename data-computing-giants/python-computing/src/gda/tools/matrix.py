# -*- coding: utf-8 -*-

'''
Created on 2017-11-01 23:44:35
矩阵工具模块.
@author: zhoujiagen
'''


def rotate(matrix):
    """转置矩阵.

    Args:
        matrix: 矩阵.

    Returns:
        matrix的转置.

    Raises:
        None
    """
    new_matrix = []

    row_len = len(matrix)
    column_len = len(matrix[0])

    for i in range(column_len):
        new_row = [matrix[j][i] for j in range(row_len)]
        new_matrix.append(new_row)

    return new_matrix

if __name__ == '__main__':
    MATRIX = [[1, 2], [3, 4]]
    print(rotate(MATRIX))
