/**
 * <pre>
 * 1 抽象
 * 
 * Storm的数据模型抽象: Topology, Stream(unlimited sequence of tuple), Spout, Bolt.
 * Storm的支持性抽象: 并发机制(集群/Node/Worker/Executor/Task), 数据流分组, 可靠性机制. 
 * 
 * 2 运行案例
 * 
 * 分布式单词计数.
 * 
 * WordCountApplication       - 单词计数应用
 * SentenceStream             - 模拟语句流
 * WordCountTopology          - 单词计数拓扑结构定义
 * WordCountSentenceSpout     - 语句生成Spout
 * WordCountSplitSentenceBolt - 语句分割Bolt
 * WordCountCountWordsBolt    - 单词计数Bolt
 * WordCountReportBolt        - 上报Bolt
 * </pre>
 */

package com.spike.giantdataanalysis.storm.wordcount;