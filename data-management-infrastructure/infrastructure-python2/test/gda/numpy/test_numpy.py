# -*- coding: utf-8 -*-

'''
Created on 2017-10-11 19:21:36
NumPy的使用
@author: zhoujiagen
'''

import numpy as np

######################################################### 创建
def create_from_seq():
    """从常规的Python元组和列表中创建"""
    a = np.array((1, 2, 3))
    b = np.array([1, 2, 3])
    print a
    print b

def create_from_seq_of_seq():
    """使用序列的序列"""
    c = np.array([(1, 2, 3), (4, 5, 6)])
    d = np.array([[1, 2, 3], [4, 5, 6]])
    e = np.array([([1, 2, 3], [4, 5, 6])])
    print c
    print d
    print e

def create_with_type():
    """创建时指定元素类型"""
    f = np.array([[1, 2], [3, 4]], dtype = complex)
    print f

def create_use_routine():
    """使用routine"""
    g = np.zeros((2, 3))  # 零矩阵
    h = np.ones((2, 3, 4), dtype = np.int16)  # 单位矩阵
    i = np.empty((2, 3))  # 未初始化的矩阵
    print g
    print h
    print i

    # 数值序列
    print np.arange(10, 30, 5)
    print np.arange(0, 2, 0.3)

######################################################### 数据结构属性

def attribute_of_ndarray():
    """ndarray的属性"""
    a = np.arange(15).reshape(3, 5)
    print "a=", a
    print "a.shape=", a.shape  # 形状/维度元组: (3,5)
    print "a.ndim=", a.ndim  # 维度数量: 2
    print "a.dtype.name=", a.dtype.name  # 元素数据类型的名称: int64
    print "a.itemsize=", a.itemsize  # 元素所占字节数: 8
    print "a.size=", a.size  # 元素总数: 15
    print "type(a)=", type(a)  # 数组的类型: numpy.ndarray


######################################################### Benchmark

def sum_python(n):
    """返回[...,i^2+i^3,...], 使用Python Builtin"""
    a = range(n)
    b = range(n)
    c = []

    for i in range(len(a)):
        a[i] = i ** 2  # 平方
        b[i] = i ** 3  # 立方
        c.append(a[i] + b[i])

    return c

def sum_numpy(n):
    """返回[...,i^2+i^3,...], 使用NumPy"""
    a = np.arange(n) ** 2
    b = np.arange(n) ** 3
    c = a + b

    return c

def benchmark_compare_python_numpy():
    """比较Python Builtin和NumPy"""
    from datetime import datetime

    size = 10000  # 数据量

    start = datetime.now()  # 计时
    c = sum_python(size)
    end = datetime.now()
    print "Last 2 elements of sum", c[-2:], ", Python spend ", (end - start).microseconds, " ms"

    start = datetime.now()
    c = sum_numpy(size)
    end = datetime.now()
    print "Last 2 elements of sum", c[-2:], ", NumPy spend ", (end - start).microseconds, " ms"


######################################################### TODO: (zhoujiagen) NumPy遍历元素

######################################################### 便利方法

def random_routine():
    """随机工具"""
    mu, sigma = 2, 0.5
    v = np.random.normal([mu, sigma, 10000])  # 生成10000个正态分布点
    print v[:10]

    # 绘图
    import matplotlib.pyplot as plt
    plt.hist(v, bins = 100, normed = 1)
    plt.show()

def histogram_routine():
    """直方图"""
    mu, sigma = 2, 0.5
    v = np.random.normal([mu, sigma, 10000])  # 生成10000个正态分布点
    # |bin1-n1|bin2-n2|bin3-n3|
    (n, bins) = np.histogram(v, bins = 100, density = True)

    # 绘图
    import matplotlib.pyplot as plt
    plt.plot(0.5 * (bins[1:] + bins[:-1]), n)
    plt.show()

if __name__ == '__main__':
    print np.__version__  # 版本

    histogram_routine()

