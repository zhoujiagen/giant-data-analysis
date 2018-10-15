# Python性能分析工具: cProfile
import numpy as np
from numpy.linalg import eigvals

def run_experiment(niter=100):
  K = 100
  results = []
  for _ in range(niter):
    mat = np.random.randn(K, K) # K*K的矩阵
    max_eigenvalue = np.abs(eigvals(mat)).max() # 计算最大的特征值
    results.append(max_eigenvalue)
  return results

some_results = run_experiment()
print('Largest one we saw: %s' % np.max(some_results)) 