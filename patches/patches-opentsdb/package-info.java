/**
 * <pre>
 * 1 GangliaStatsCollector
 * TSDB统计指标收集器.
 * 
 * tsd.extension.ganglia.host
 * tsd.extension.ganglia.port
 * 
 * 
 * 2 KafkaRTPublisher
 * 使用Kafka的实时发布器.
 * 
 * tsd.extension.plugin.kakfa.file
 * tsd.extension.plugin.rt.kakfa.topic.annotation
 * 
 * 3 KafkaStorageExceptionHandler
 * 使用Kafka的存储异常处理器.
 * 
 * tsd.extension.plugin.kakfa.file
 * tsd.extension.plugin.storage_exception_handler.kakfa.topic.error
 * 
 * </pre>
 */
package com.spike.giantdataanalysis.opentsdb;