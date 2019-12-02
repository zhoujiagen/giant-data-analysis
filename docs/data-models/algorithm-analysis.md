# Algorithm Analysis

> An Introduction to the Analysis of Algorithms, Second Edition. by Rober Sedgewick, Philippe Flajolet.

## 记号

$\left \lfloor x \right \rfloor$: floor function. largest interger less than or equal to $x$

$\left \lceil x \right \rceil$: ceiling function. smallest integer greater than or equal to $x$

$\left \{ x \right \}$: fractional part. $x - \left \lfloor x \right \rfloor$

$\mathrm{lg}N$: binary logarithm. $\mathrm{log} _{2}N$

$\mathrm{ln}N$: natural logarithm. $\mathrm{log} _{e}N$

$\begin{pmatrix} n \\ k \end{pmatrix}$: binomial coefficient. number of ways to choose $k$ out of $n$ items

$\begin{bmatrix} n\\ k \end{bmatrix}$: Stirling number of the first kind. number of permutations of $n$ elements that have $k$ cycles

$\begin{Bmatrix} n \\ k \end{Bmatrix}$: Stirling number of the second kind. number of ways to partition $n$ elements into $k$ nonempty subsets

$\phi$: golden ratio. $(1 + \sqrt{5} ) / 2$

$\gamma$: Euler's contant. $.57721 \cdots$

$\sigma$: Stirling's constants. $\sqrt{2 \pi} = 2.50662 \cdots$


给定函数 $f(N)$:

- $O(f(N))$: 所有$g(N)$, 随$N \rightarrow \infty$, $|g(N)/f(N)|$ 有上界
- $\Omega(f(N))$: 所有$g(N)$, 随$N \rightarrow \infty$, $|g(N)/f(N)|$ 有下界
- $\Theta(f(N))$: 所有$g(N)$, 随$N \rightarrow \infty$, $|g(N)/f(N)|$ 有界


$H_{N}$: harmonic numbers. $H_{N} = \sum_{1 \le k \le N} 1/k$

## 1 Analysis of Algorithms

- $\Pi_{N}$: 大小为$N$的输入的数量
- $\Pi_{Nk}$: 大小为$N$且产生成本$k$的输入的数量
- $\Sigma_{N}$: 算法在所有大小为$N$的输入上的总成本

案例: 快排(quicksort)

- $A$: 划分截断的数量
- $B$: 交换的数量
- $C$: 比较的数量
- $C_{N}$: 用于排序$N$个元素的平均比较的数量



## 2 Recurrence Relations


## 3 Generating Functions


## 4 Asymptotic Approximations

## 5 Analytic Combinatorics

## 6 Trees

## 7 Permutations

## 8 Strings and Tries

## 9 Words and Mappings
