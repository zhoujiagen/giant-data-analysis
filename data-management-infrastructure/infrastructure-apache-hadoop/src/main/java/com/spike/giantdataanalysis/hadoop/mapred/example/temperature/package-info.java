/**
 * <pre>
 * NCDC数据中年度最高温度示例
 * 
 * 应用类:
 * MaxTemperatureApplication        - 应用
 * MaxTemperatureJobDriver          - 使用Tool支持命令选项解析和配置
 * 
 * MaxTemperatureMapper             - Mapper
 * MaxTemperatureMapperUsingParser
 * LineRecordParser
 * 
 * MaxTemperatureReducer            - Reducer
 * 
 * 
 * 本地测试:
 * MaxTemperatureMapperTest               - Mapper测试
 * MaxTemperatureReducerTest              - Reducer测试
 * MaxTemperatureJobDriverTest            - JobDriver测试
 * MaxTemperatureJobDriverMiniClusterTest - TODO mini集群测试
 * </pre>
 * 
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.hadoop.mapred.example.temperature;