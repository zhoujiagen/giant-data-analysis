# -*- coding: utf-8 -*-
#!/usr/bin/env python

'''
坐标: linear, logarithmic and logit scales.
WARN: 使用Python3执行
'''

import numpy as np
import matplotlib.pyplot as plt

# 用于logit范围
from matplotlib.ticker import NullFormatter

np.random.seed(19680801)
y = np.random.normal(loc=0.5, scale=0.4, size=1000)
y = y[(y > 0) & (y < 1)]
y.sort()
x = np.arange(len(y))

plt.figure(1)

# 1 linear
plt.subplot(221)
plt.plot(x,y)
plt.yscale('linear')
plt.title('linear')
plt.grid(True)

# 2 log
plt.subplot(222)
plt.plot(x,y)
plt.yscale('log')
plt.title('log')
plt.grid(True)

# 3 symmetric log
plt.subplot(223)
plt.plot(x, y-y.mean())
plt.yscale('symlog', linthreashy=0.01)
plt.title('symlog')
plt.grid(True)

# 4 logit
# WARN: 使用Python3执行
plt.subplot(224)
plt.plot(x, y)
plt.yscale('logit')
plt.title('logit')
plt.grid(True)
plt.gca().yaxis.set_minor_formatter(NullFormatter())

# 调整subplt布局
plt.subplots_adjust(top=0.92, bottom=0.08, left=0.10, right=0.95,
    hspace=0.25, wspace=0.35)

plt.show()
plt.close()
