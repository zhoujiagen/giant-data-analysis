/**
 * HBase客户端API示例 - 协处理器 TODO(zhoujiagen) 添加协处理器示例.
 * 
 * <pre>
 * 主要思想: 将代码移动到数据的存放端.
 * 基本接口: Coprocessor.
 * 
 * 两种插件: observer, endpoint.
 * (1) observer
 * 类似于触发器, 回调函数/钩子函数/hook在特定事件发生时被执行.
 * RegionObserver, MasterObserver, WALObserver.
 * (2) endpoint
 * 类似于存储过程, 动态扩展RPC协议.
 * 
 * 加载方式: 配置文件, 表描述符.
 * </pre>
 */
package com.spike.giantdataanalysis.hbase.example.client.coprocessor;