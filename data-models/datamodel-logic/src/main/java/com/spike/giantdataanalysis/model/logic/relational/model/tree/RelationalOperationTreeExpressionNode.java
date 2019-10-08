package com.spike.giantdataanalysis.model.logic.relational.model.tree;

import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsExpression;

/**
 * 关系操作树的表达式节点.
 */
public abstract class RelationalOperationTreeExpressionNode implements Literal {
  public static class RelationalOperationTreeNotExpressionNode
      extends RelationalOperationTreeExpressionNode {
    public RelationalOperationTreeExpressionNode expressionNode;

    @Override
    public String literal() {
      return "NOT " + expressionNode.literal();
    }
  }

  public static class RelationalOperationTreeLogicalExpressionNode
      extends RelationalOperationTreeExpressionNode {
    public RelationalOperationTreeExpressionNode expressionNode1;
    public RelationalLogicalOperatorEnum operator;
    public RelationalOperationTreeExpressionNode expressionNode2;

    @Override
    public String literal() {
      return operator.literal() + "(" + expressionNode1.literal() + ", " + expressionNode2.literal()
          + ")";
    }
  }

  public static class RelationalOperationTreeIsExpressionNode
      extends RelationalOperationTreeExpressionNode {
    public RelationalOperationTreePredicateNode predicateNode;
    public boolean not = false;
    public IsExpression.TestValue testValue;

    @Override
    public String literal() {
      String notString = "";
      if (not) {
        notString = " NOT ";
      }
      return predicateNode.literal() + notString + testValue.literal();
    }
  }

  public static class RelationalOperationTreePredicateExpressionNode
      extends RelationalOperationTreeExpressionNode {
    public RelationalOperationTreePredicateNode predicateNode;

    @Override
    public String literal() {
      return predicateNode.literal();
    }
  }
}
