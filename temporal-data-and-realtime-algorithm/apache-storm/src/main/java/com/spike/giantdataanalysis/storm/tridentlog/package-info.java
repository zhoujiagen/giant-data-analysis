/**
 * <pre>
 * Trident扩展, 应用场景: 日志中特定事件的移动平均值计算
 * 
 * LogAnalysisApplication                   - 应用入口
 * LogAnalysisTopology                      - 拓扑定义
 *  OpaqueTridentKafkaSpout                 - 内建的Kafka Spout[=>"str"]
 * 
 * 组件:
 * JsonProjectFunction                      - (BaseFunction)["str"=>"level","timestamp","message","logger"] 日志解析成JSON
 * MovingAverageFunction                    - (BaseFunction)["timestamp"=>"average"] 计算移动平均值
 * ThresholdFilterFunction                  - (BaseFunction)["average"=>"change","threshold"] 计算状态是否改变
 * BooleanFilter                            - (BaseFilter)  ["change"=>] 过滤
 * SlackBotFunction                         - (BaseFunction)[ALL=>] 发送Slack消息
 * </pre>
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.storm.tridentlog;