package com.spike.giantdataanalysis.model.logic.relational.model;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraOperationEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalAttribute;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * 关系操作树.
 */
public final class RelationalOperationTree {

  public static class TreeNode implements Literal {
    // tree structure
    public TreeNode parent;
    public List<TreeNode> children;

    // leaf node
    public RelationalRelation relation;

    // internal node
    public RelationalAlgebraOperationEnum operation;
    public List<Expression> conditions;
    public List<RelationalAttribute> attributes;

    public boolean isRelation() {
      return (relation != null);
    }

    @Override
    public String literal() {
      StringBuilder builder = new StringBuilder();
      this.literal(builder, this, 0);
      return builder.toString();
    }

    public void literal(StringBuilder builder, TreeNode treeNode, int level) {
      if (treeNode == null) {
        return;
      }

      String tabs = StringUtils.EMPTY;
      if (level > 0) {
        tabs = StringUtils.repeat("_|", level);
      }

      if (treeNode.isRelation()) {
        builder.append(tabs);
        builder.append(treeNode.relation.literal());
        builder.append(System.lineSeparator());
      }

      if (treeNode.operation != null) {
        builder.append(tabs);
        builder.append(treeNode.operation.literal());
        builder.append("(").append(treeNode.operation.name()).append(")");

        if (CollectionUtils.isNotEmpty(treeNode.conditions)) {
          List<String> conditionLiterals = Lists.newArrayList();
          for (Expression condition : treeNode.conditions) {
            conditionLiterals.add(condition.literal());
          }
          builder.append("[");
          builder.append(Joiner.on(", ").join(conditionLiterals));
          builder.append("]");
        }

        if (CollectionUtils.isNotEmpty(treeNode.attributes)) {
          List<String> attributeLiterals = Lists.newArrayList();
          for (RelationalAttribute attribute : treeNode.attributes) {
            attributeLiterals.add(attribute.literal());
          }
          builder.append("[");
          builder.append(Joiner.on(", ").join(attributeLiterals));
          builder.append("]");
        }

        builder.append(System.lineSeparator());
      }

      if (treeNode.children != null) {
        for (TreeNode child : treeNode.children) {
          this.literal(builder, child, level + 1);
        }
      }

    }

  }

}
