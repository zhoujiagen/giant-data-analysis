package com.spike.giantdataanalysis.model.algorithms.search;

/**
 * 符号表.
 * 
 * <pre>
 * 约束:
 * 
 * 不允许存在重复的键;
 * 待存入键与表中已有键冲突时, 新值会替代旧值;
 * 键不能为null;
 * 值不能为null;
 * </pre>
 * @author zhoujiagen
 */
public interface IST<Key, Value> {
  /** 将键值对存入表中. */
  void put(Key key, Value val);

  /** 获取键key对应的值, 不存在返回null. */
  Value get(Key key);

  /** 从表中删除键key和对应的值. */
  void delete(Key key);

  /** 键key在表中是否有对应的值. */
  boolean contains(Key key);

  /** 表是否为空. */
  boolean isEmpty();

  /** 表中键值对数量. */
  int size();

  /** 表中所有键的集合. */
  Iterable<Key> keys();
}
