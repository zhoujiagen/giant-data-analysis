/**
 * 散列表.
 * 
 * <pre>
 * 使用散列的查找算法:
 * (1) 使用算术操作(散列函数)将键转化为数组的索引;
 * (2) 处理碰撞冲突(多个键被散列到相同的索引值).
 * 
 * 两种解决碰撞的方法:
 * (1) 拉链法 SeparateChainingHashST
 * (2) 线性探测法 LinearProbingHashST
 * </pre>
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.model.algorithms.search.hash;