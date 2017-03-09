/**
 * <pre>
 * Trident扩展, 应用场景: 传感器数据中特定事件数量计数
 * 
 * OutbreakDetectionApplication     - 应用入口
 * 
 * OutbreakDetectionTopology        - 应用的拓扑定义
 * 
 * 
 * 组件:
 * 
 * DiagnosisEventSpout              - (ITridentSpout) 诊断事件Spout[=>"event"]
 *  DefaultCoordinator                - (BatchCoordinator) 批次协同逻辑
 *  DiagnosisEventEmitter             - (Emitter) 事件发出逻辑
 *    DiagnosisEvent                    - 诊断事件抽象
 * 
 * DiseaseFilter                    - (BaseFilter) 筛选感兴趣的疾病["event"=>"event"]
 * 
 * CityAssignment                   - (BaseFunction) 添加city字段, 值为tuple中经纬度最近的城市名称["event"=>"event","city"]
 * HourAssignment                   - (BaseFunction) 添加hour和key字段, key的格式为city:diagnosisCode:hour["event","city"=>"event","city","hour","cityDiseaseHour"]
 * 
 * 使用了默认的聚合器Count             - ["cityDiseaseHour"...=>"cityDiseaseHour","count"]
 * 
 * 持久化
 * OutbreakTrendFactory             - 简单的StateFactory的实现
 *  OutbreakTrendState              - (NonTransactionalMap) 状态实现
 *    OutbreakTrendBackingMap       - (IBackingMap) 内存中存储实现
 *  
 * OutbreakDetector                 - (BaseFunction) 超出阈值时发布事件["cityDiseaseHour","count"=>"alert"]
 * DispatchAlert                    - (BaseFunction) 接收到事件时退出应用["alert"=>""]
 * </pre>
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.storm.tridentsensor;

