/**
 * <pre>
 * TODO 图的算法
 * 
 * 一些术语:
 * (1) 路径: 由边顺序联接的一系列顶点; 
 * 简单路径: 没有重复顶点的路径;
 * (2) 环: 至少含有一条边, 起点和终点相同的路径; 
 * 简单环: (除起点和终点必须相同外)不含有重复顶点和边的环;
 * (3) 路径或环的长度: 其中所包含的边数;
 * (4) 连通图: 途中任意一个顶点都存在一条路径到达另一个任意顶点;
 * (5) 树: 无环连通图
 * 连通图的生成树: 含有图中所有顶点且是一颗树的子图;
 * 图与树的关系:
 * 当且仅当含有V个顶点的图G满足下述条件之一时, 是一棵树:
 * (5.1) G有V-1条边, 不含有环;
 * (5.2) G有V-1条边, 连通的;
 * (5.3) G连通的, 但删除任意一条边后不再连通;
 * (5.4) G无环, 但添加任意一条边后有环;
 * (5.5) G中的任意一对顶点之间仅存在一条简单路径.
 * 
 * 0 图: IGraph
 * 
 * 1 无向图: Graph
 * 深度优先搜索(DFS): IGraphSearch, DepthFirstSearch
 * 寻找路径: IPaths, DepthFirstPaths
 * 广度优先搜索(BFS): BreadthFirstPaths
 * 连通分量: ConnectedComponents - 使用DFS
 * 特定实现: 符号图SymbolGraph
 * 
 * DFS的应用: 环检测CycleDetection, 双色问题TwoColorable
 * BFS的应用: 顶点间距离度量(最短路径, DegreeOfSeparation)
 * 
 * 2 有向图: DirectedGraph
 * 可达性: DirectedDFS - 使用DFS
 * 环, 有向无环图(DAG): 拓扑排序
 * 强连通性StrongConnectedComponents
 * 传递闭包TransitiveClosure
 * 
 * 3 最小生成树
 * 切分定理
 * Prim
 * Kruskal
 * 
 * 4 最短路径
 * 加权有向图
 * 非负权重: Dijkstra
 * 无环加权有向图中的最短路径
 * 一般加权有向图中的最短路径
 * 
 * </pre>
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.model.algorithms.graph;