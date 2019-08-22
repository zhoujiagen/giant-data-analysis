package com.spike.giantdataanalysis.model.logic.relational.model.core;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.REScope;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalDifferenceOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalIntersectionOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalProjectOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalSelectOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalUnionOperation;

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
      final Integer length, //
      final boolean nullable) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(dataType != null);

    return new RelationalAttribute(name, dataType, length, nullable);
  }

  // ---------------------------------------------------------------------------
  // 关系键
  // ---------------------------------------------------------------------------

  public static RelationalRelationKey makeKey(final RelationalRelation relation,
      final RelationalRelationKeyTypeEnum keyType, final String name,
      final List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name));
    Preconditions.checkArgument(attributes != null && !attributes.isEmpty());

    return new RelationalRelationKey(relation, keyType, name, attributes);
  }

  // ---------------------------------------------------------------------------
  // 关系
  // ---------------------------------------------------------------------------

  public static RelationalRelation makeRelation(final String name,
      List<RelationalAttribute> attributes) {
    return new RelationalRelation(name, attributes);
  }

  // ---------------------------------------------------------------------------
  // 操作
  // ---------------------------------------------------------------------------

  public static RelationalIntersectionOperation makeIntersection(RelationalRelation first,
      RelationalRelation second) {
    return new RelationalIntersectionOperation(first, second);
  }

  public static RelationalUnionOperation makeUnion(RelationalRelation first,
      RelationalRelation second) {
    return new RelationalUnionOperation(first, second);
  }

  public static RelationalUnionOperation makeUnion(RelationalRelation... relations) {
    return new RelationalUnionOperation(relations);
  }

  public static RelationalUnionOperation makeUnion(List<RelationalRelation> relationalRelations) {
    return new RelationalUnionOperation(relationalRelations);
  }

  public static RelationalDifferenceOperation makeDifference(RelationalRelation first,
      RelationalRelation second) {
    return new RelationalDifferenceOperation(first, second);
  }

  public static RelationalProjectOperation makeProject(RelationalRelation first,
      List<String> attributeNames) {
    return new RelationalProjectOperation(first, attributeNames);
  }

  public static RelationalSelectOperation makeSelect(RelationalRelation relation,
      Expression condition, REScope interpreteScope) {
    return new RelationalSelectOperation(relation, condition, interpreteScope);
  }
}
