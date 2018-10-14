# -*- coding: utf-8 -*-
#!/usr/bin/env python

'''
使用文本
'''

import numpy as np
import matplotlib.pyplot as plt


# 随机数种子
np.random.seed(19680801)

mu, sigma = 100, 15
x  = mu + sigma * np.random.randn(10000)


plt.figure(1)

plt.subplot(211)
n, bins, patches = plt.hist(x, 50, normed=1, facecolor='g', alpha=0.75)
plt.xlabel('x')
plt.ylabel('Probs')
plt.title('Histogram')
# 文本
plt.text(60, 0.025, r'$\mu=100, \ \sigma=15$')
plt.axis([40, 160, 0, 0.03])
plt.grid(True) # 显示网格

plt.subplot(212)
t = np.arange(0.0, 5.0, 0.01)
s = np.cos(2*np.pi*t)
line, = plt.plot(t,s, lw=2)
# 注释文本
plt.annotate('local max', xy=(2,1), xytext=(3,1.5),
    arrowprops=dict(facecolor='red', shrink=0.05))
plt.ylim(-2,2) # y坐标取值范围

plt.show()
plt.close()
