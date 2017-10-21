# -*- coding: utf-8 -*-

'''
Created on 2017-10-12 18:21:33

@author: zhoujiagen
'''

import matplotlib.pyplot as plt
import numpy as np
from numpy import pi

######################################################### 简单使用


######################################################### 函数的图形
def draw_sin():
    """sin(x)"""
    x = np.linspace(0, 2 * pi, 100)
    f = np.sin(x)
    plt.plot(x, f)
    plt.axis([0, 2 * pi, -1, 1])
    plt.show()



if __name__ == '__main__':
    import matplotlib
    print matplotlib.__version__  # 版本

    draw_sin()
