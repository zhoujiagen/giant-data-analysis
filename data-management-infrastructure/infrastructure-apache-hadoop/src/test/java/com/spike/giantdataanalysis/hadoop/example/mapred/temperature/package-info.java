/**
 * <pre>
 * NCDC数据中年度最高温度示例
 * 
 * 应用类:
 * ExampleMaxTemperatureApplication        - 应用
 * ExampleMaxTemperatureJobDriver          - 使用Tool支持命令选项解析和配置
 * 
 * ExampleMaxTemperatureMapper             - Mapper
 * ExampleMaxTemperatureMapperUsingParser
 * LineRecordParser
 * 
 * ExampleMaxTemperatureReducer            - Reducer
 * 
 * 
 * 本地测试:
 * TestMaxTemperatureMapper               - Mapper测试
 * TestMaxTemperatureReducer              - Reducer测试
 * TestMaxTemperatureJobDriver            - JobDriver测试
 * TestMaxTemperatureJobDriverMiniCluster - TODO mini集群测试
 * </pre>
 * 
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.hadoop.example.mapred.temperature;