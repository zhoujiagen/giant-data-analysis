# -*- coding: utf-8 -*-
#!/usr/bin/env python

'''
多图和坐标
'''

import numpy as np
import matplotlib.pyplot as plt


def f(t):
    return np.exp(-t) * np.cos(2*np.pi*t)
def g(t):
    return np.cos(2*np.pi*t)

t1 = np.arange(0.0, 5.0, 0.1)
t2 = np.arange(0.0, 5.0, 0.02)

plt.figure(1)
plt.subplot(211) # numrows, numcols, fignum
plt.plot(t1, f(t1), 'bo', t2, f(t2), 'k')
plt.title("figure 1, sub 211")

plt.figure(2) # 新的图2
plt.plot([4,5,6])
plt.title("figure 2, sub 111")

plt.figure(1) # 切换为图1
plt.subplot(212)
plt.plot(t2, g(t2), 'r--')
plt.title("figure 1, sub 212") # 设置子图212的标题

plt.show()
plt.close()
