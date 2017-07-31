/**
 * HBase客户端API示例 - 过滤器
 * 
 * <pre>
 * 所有的过滤器在服务端生效, 即谓词下推(predicate push down).
 * 
 * 0 接口和基类
 * Filter, FilterBase
 * 应用在Get和Scan中.
 * 
 * 1 比较过滤器: 返回匹配的值.
 * CompareFilter: 比较运算符见org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
 * 比较器均继承自ByteArrayComparable.
 * 
 * RowFilter: 按行键值过滤.
 * FamilyFilter: 按列族名过滤.
 * QualifierFilter: 按列名过滤.
 * ValueFilter: 按单元格的值过滤.
 * DependentColumnFilter: 使用参考列控制其他列的过滤, 结果中包含的列与参考列的时间戳相同.
 * 
 * 2 专用过滤器
 * 直接继承自FilterBase.
 * 
 * SingleColumnValueFilter: 用一列的值确定是否一行数据被过滤.
 * SingleColumnValueExcludeFilter: 参考列不被包含到结果中.
 * PrefixFilter: 匹配行键值的前缀.
 * PageFilter: 按行分页.
 * KeyOnlyFilter: 只返回行键值.
 * FirstKeyOnlyFilter: 只访问行中的第一列.
 * InclusiveStopFilter: 包含终止行, 与一般情况下不包含终止行不一样.
 * TimestampsFilter: 按时间戳精确匹配版本.
 * ColumnCountGetFilter: 限制每行最多取回多少列.
 * ColumnPaginationFilter: 对一行的所有列进行分页.
 * ColumnPrefixFilter: 列名称前缀匹配过滤.
 * RandomRowFilter: 包含随机行.
 * 
 * 3 附加/装饰过滤器(decorating filter)
 * SkipFilter: 行中任意Cell值均满足内部过滤器的条件(A wrapper filter that filters an entire row if any of the Cell checks do not pass.).
 *  内部过滤器必须实现filterKeyValue()方法, 例如ValueFilter.
 * WhileMatchFilter: 与SkipFilter类似, 当一条数据(行键或列)被过滤掉时直接放弃这次扫描操作.
 * 
 * 4 FilterList
 * 可按顺序组合单一功能的过滤器, 可组成多级的过滤器.
 * 
 * 
 * </pre>
 */
package com.spike.giantdataanalysis.hbase.example.client.filter;

