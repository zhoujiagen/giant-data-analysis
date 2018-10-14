# -*- coding: utf-8 -*-
#!/usr/bin/env python

'''
线型设置
'''

import numpy as np
import matplotlib.pyplot as plt

# 标题
plt.title('line styles')
# 指定坐标轴取值范围
plt.axis([0,10, 0,10]) # x: 0-10, y: 0-10

'''
设置方法:

(1) 使用关键字
plt.plot(x, y, linewidth=2.0) # 线宽度

(2) 使用set方法
line, = plt.plot(x, y, '-')
line.set_antialiased(False) # 取消平滑

(3) 使用plt.setp()命令
lines = plt.plot(x1, y1, x2, y2)
# use keyword args - 关键字参数
plt.setp(lines, color='r', linewidth=2.0)	# 线颜色和宽度
# or MATLAB style string value pairs - MATLAB风格
plt.setp(lines, 'color', 'r', 'linewidth', 2.0)
'''
# 返回matplotlib.lines.Line2D对象(http://matplotlib.org/api/lines_api.html#matplotlib.lines.Line2D)
line, = plt.plot([1,2,4,3])
print line
# 查看可设置属性
print plt.setp(line)

line.set_linestyle('--')
line.set_linewidth(2.)

# 指定坐标轴说明
plt.xlabel("x")
plt.ylabel("y")
# 展示图片
plt.show()
plt.close()
