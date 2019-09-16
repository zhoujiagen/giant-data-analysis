package com.spike.giantdataanalysis.model.logic.relational.model.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeNode;

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

  /**
   * 构造操作数树节点: 关系.
   * @param relation
   * @return
   */
  public static RelationalOperationTreeNode newTreeNode(RelationalRelation relation) {
    Preconditions.checkArgument(relation != null);

    RelationalOperationTreeNode result = new RelationalOperationTreeNode();
    result.relation = relation;
    return result;
  }

  /**
   * 构造操作数树节点: 子节点.
   * @param operation
   * @param children
   * @return
   */
  public static RelationalOperationTreeNode newTreeNode(RelationalAlgebraOperationEnum operation,
      List<RelationalOperationTreeNode> children) {
    Preconditions.checkArgument(operation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(children));

    RelationalOperationTreeNode result = new RelationalOperationTreeNode();
    result.operation = operation;
    result.children = children;
    for (RelationalOperationTreeNode treeNode : result.children) {
      treeNode.parent = result;
    }
    return result;
  }

  /**
   * 构造操作数树节点: 子节点和条件.
   * @param operation
   * @param children
   * @param conditions
   * @return
   */
  public static RelationalOperationTreeNode newTreeNodeCondition(
      RelationalAlgebraOperationEnum operation, List<RelationalOperationTreeNode> children,
      List<RelationalOperationTreeExpressionNode> conditions) {
    Preconditions.checkArgument(operation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(children));

    RelationalOperationTreeNode result = new RelationalOperationTreeNode();
    result.operation = operation;

    result.children = children;
    result.conditions = conditions;
    for (RelationalOperationTreeNode treeNode : result.children) {
      treeNode.parent = result;
    }
    return result;
  }

  /**
   * 构造操作数树节点: 子节点和属性.
   * @param operation
   * @param children
   * @param attributes
   * @return
   */
  public static RelationalOperationTreeNode newTreeNodeAttribute(
      RelationalAlgebraOperationEnum operation, List<RelationalOperationTreeNode> children,
      List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(operation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(children));

    RelationalOperationTreeNode result = new RelationalOperationTreeNode();
    result.operation = operation;

    result.children = children;
    result.attributes = attributes;
    for (RelationalOperationTreeNode treeNode : result.children) {
      treeNode.parent = result;
    }
    return result;
  }
}
