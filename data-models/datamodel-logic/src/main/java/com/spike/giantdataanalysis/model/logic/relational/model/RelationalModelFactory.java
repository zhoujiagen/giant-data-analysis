package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;

/**
 * 关系模型抽象的工厂.
 */
public abstract class RelationalModelFactory {

  // ---------------------------------------------------------------------------
  // 属性
  // ---------------------------------------------------------------------------

  public static RelationalAttribute makeAttribute(//
      final String name, //
      final RelationalAttributeTypeEnum dataType, //
      final boolean nullable) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(dataType != null);

    return new RelationalAttribute(name, dataType, nullable);
  }

  // ---------------------------------------------------------------------------
  // 关系键
  // ---------------------------------------------------------------------------

  public static RelationalRelationKey makeKey(//
      final RelationalRelationKeyTypeEnum keyType, //
      final String name, //
      final List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());

    return new RelationalRelationKey() {
      @Override
      public RelationalRelationKeyTypeEnum keyType() {
        return keyType;
      }

      @Override
      public String name() {
        return name;
      }

      @Override
      public List<RelationalAttribute> attributes() {
        return attributes;
      }

    };
  }

  // ---------------------------------------------------------------------------
  // 关系
  // ---------------------------------------------------------------------------

  public static RelationalRelation makeRelation(final String name) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));

    return new RelationalRelation(name);
  }

  public static RelationalRelation makeRelation(//
      final String name, //
      final List<RelationalAttribute> attributes, //
      final List<RelationalRelationKey> keys) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());

    RelationalRelation result = new RelationalRelation(name);
    for (RelationalAttribute attribute : attributes) {
      result.addAttribute(attribute);
    }
    for (RelationalRelationKey key : keys) {
      result.addKey(key);
    }
    return result;
  }

  // ---------------------------------------------------------------------------
  // 元组
  // ---------------------------------------------------------------------------

  public static RelationalTuple makeTuple(//
      final List<RelationalAttribute> attributes, //
      final List<Object> values) {
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());
    Preconditions.checkArgument(values != null && !values.isEmpty());
    Preconditions.checkArgument(attributes.size() == values.size());
    int size = attributes.size();

    for (int i = 0; i < size; i++) {
      Class<?> reprClass = attributes.get(i).dataType().reprClass;
      Preconditions.checkArgument(reprClass.isInstance(values.get(i)));
    }

    return new RelationalTuple() {

      @Override
      public List<Object> values() {
        return values;
      }

      @Override
      public List<RelationalAttribute> attributes() {
        return attributes;
      }
    };
  }

  // ---------------------------------------------------------------------------
  // 元组列表
  // ---------------------------------------------------------------------------

  public static RelationalTuples makeTuples(//
      final List<RelationalAttribute> attributes, //
      final List<List<Object>> values) {
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());
    Preconditions.checkArgument(values != null && values.size() > 0);
    int size = attributes.size();
    for (List<Object> value : values) {
      Preconditions.checkArgument(size == value.size());
    }
    for (List<Object> value : values) {
      for (int i = 0; i < size; i++) {
        Class<?> reprClass = attributes.get(i).dataType().reprClass;
        Preconditions.checkArgument(reprClass.isInstance(value.get(i)));
      }
    }

    return new RelationalTuples() {

      @Override
      public List<List<Object>> values() {
        return values;
      }

      @Override
      public List<RelationalAttribute> attributes() {
        return attributes;
      }
    };
  }
}
