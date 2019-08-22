package com.spike.giantdataanalysis.model.logic.relational.model.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAttributeTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalRelationKeyTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalOperationTree.TreeNode;

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
  public static TreeNode newTreeNode(RelationalRelation relation) {
    Preconditions.checkArgument(relation != null);

    TreeNode result = new TreeNode();
    result.relation = relation;
    return result;
  }

  public static TreeNode newTreeNode(RelationalAlgebraOperationEnum operation,
      List<TreeNode> children) {
    Preconditions.checkArgument(operation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(children));

    TreeNode result = new TreeNode();
    result.operation = operation;
    result.children = children;
    for (TreeNode treeNode : result.children) {
      treeNode.parent = result;
    }
    return result;
  }

  public static TreeNode newTreeNodeCondition(RelationalAlgebraOperationEnum operation,
      List<TreeNode> children, List<Expression> conditions) {
    Preconditions.checkArgument(operation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(children));

    TreeNode result = new TreeNode();
    result.operation = operation;

    result.children = children;
    result.conditions = conditions;
    for (TreeNode treeNode : result.children) {
      treeNode.parent = result;
    }
    return result;
  }

  public static TreeNode newTreeNodeAttribute(RelationalAlgebraOperationEnum operation,
      List<TreeNode> children, List<RelationalAttribute> attributes) {
    Preconditions.checkArgument(operation != null);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(children));

    TreeNode result = new TreeNode();
    result.operation = operation;

    result.children = children;
    result.attributes = attributes;
    for (TreeNode treeNode : result.children) {
      treeNode.parent = result;
    }
    return result;
  }

  // TODO(zhoujiagen) hacking RelationalOperationTree
  // public static RelationalIntersectionOperation makeIntersection(RelationalRelation first,
  // RelationalRelation second) {
  // return new RelationalIntersectionOperation(first, second);
  // }
  //
  // public static RelationalUnionOperation makeUnion(RelationalRelation first,
  // RelationalRelation second) {
  // return new RelationalUnionOperation(first, second);
  // }
  //
  // public static RelationalUnionOperation makeUnion(RelationalRelation... relations) {
  // return new RelationalUnionOperation(relations);
  // }
  //
  // public static RelationalUnionOperation makeUnion(List<RelationalRelation> relationalRelations)
  // {
  // return new RelationalUnionOperation(relationalRelations);
  // }
  //
  // public static RelationalDifferenceOperation makeDifference(RelationalRelation first,
  // RelationalRelation second) {
  // return new RelationalDifferenceOperation(first, second);
  // }
  //
  // public static RelationalProjectOperation makeProject(RelationalRelation first,
  // List<String> attributeNames) {
  // return new RelationalProjectOperation(first, attributeNames);
  // }
  //
  // public static RelationalSelectOperation makeSelect(RelationalRelation relation,
  // Expression condition, REScope interpreteScope) {
  // return new RelationalSelectOperation(relation, condition, interpreteScope);
  // }
}
