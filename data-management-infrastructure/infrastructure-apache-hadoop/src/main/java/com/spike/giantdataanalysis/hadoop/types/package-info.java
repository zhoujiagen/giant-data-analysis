/**
 * <pre>
 * 数据结构类型
 * 
 * 1 Hadoop I/O: 数据完整性, 压缩, 序列化, 文件
 * 
 * 2 MapReduce类型
 * map: (K1, V1) => list(K2, V2)
 * reduce: (K2, list(V2)) => list(K3, V3)
 * 
 * combiner: (K2, list(V2)) => list(K2, V2)
 * 
 * partition: (K2, V2) => integer
 * 
 * 3 输入输出格式
 * SequenceFileExample      - 序列化文件的示例
 * WholeFileInputForamt     - 输入格式示例: 将整个文件作为一条记录
 * MultiOutpusExample       - Reducer多输出的示例: 将NCDC数据集按气象站拆分
 * </pre>
 * 
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.hadoop.types;