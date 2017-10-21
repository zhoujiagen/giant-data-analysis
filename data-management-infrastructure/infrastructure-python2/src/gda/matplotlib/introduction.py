# -*- coding: utf-8 -*-
#!/usr/bin/env python

'''
简单的使用
'''

import numpy as np
import matplotlib.pyplot as plt

# 标题
plt.title('introduction')
# 指定坐标轴取值范围
#plt.axis([0,6, 0,20]) # x: 0-6, y: 0-20

# 图1: y
# 只传入单个数组作为y, 自动生成x=[0,1,2,3]: x从0开始, 与y长度相同
plt.plot([1,2,3,4])
# 图2: x,y
plt.plot([1,2,3,4], [1,4,9,16], 'ro')
# 图3: x1,y1,ls1, x2,y2,l2, x3,y3,l3
t = np.arange(0., 5., 0.2)
plt.plot(t, t, 'r--', t, t**2, 'bs', t, t**3, 'g^')

# 指定坐标轴说明
plt.xlabel("x")
plt.ylabel("y")

# 展示图片
plt.show()
plt.close()
