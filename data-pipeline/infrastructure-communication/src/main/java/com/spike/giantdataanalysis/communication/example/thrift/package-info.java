/**
 * Apache Thrift示例包.
 * 
 * <pre>
 * 1 概念
 * (1) Server: 单线程, 事件驱动等
 * 装配: 创建Transport => 创建Transport的输入输出Protocol => 基于Protocol创建Processor => 等待输入连接传入Processor.
 * (2) Processor: thrift编译器生成
 * 封装了读写输入输出流的功能;
 * (3) Protocol: JSON, 紧凑格式等
 * 定义了内存中数据与网络中线上数据之间的映射, 描述数据类型如何通过底层的Transport编解码;
 * (4) Transport: TCP, HTTP等
 * 通过网络读写的简单抽象.
 * </pre>
 */
package com.spike.giantdataanalysis.communication.example.thrift;