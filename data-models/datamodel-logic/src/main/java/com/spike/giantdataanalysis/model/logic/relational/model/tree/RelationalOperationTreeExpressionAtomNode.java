package com.spike.giantdataanalysis.model.logic.relational.model.tree;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.Literal;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals;

public abstract class RelationalOperationTreeExpressionAtomNode implements Literal {
  public static class RelationalOperationTreeConstantExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public Literals.Constant constant;

    @Override
    public String literal() {
      return constant.literal;
    }
  }

  public static class RelationalOperationTreeFullColumnNameExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public DBObjects.FullColumnName fullColumnName;

    @Override
    public String literal() {
      return fullColumnName.literal();
    }
  }

  public static class RelationalOperationTreeFunctionCallExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public Functions.FunctionCall functionCall;

    @Override
    public String literal() {
      return functionCall.literal();
    }
  }

  public static class RelationalOperationTreeCollateExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode;
    public String collationName;

    @Override
    public String literal() {
      return expressionAtomNode.literal() + " COLLATE " + collationName;
    }
  }

  public static class RelationalOperationTreeMysqlVariableExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public DBObjects.MysqlVariable mysqlVariable;

    @Override
    public String literal() {
      return mysqlVariable.literal();
    }
  }

  public static class RelationalOperationTreeUnaryExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalUnaryOperatorEnum unaryOperator;
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode;

    @Override
    public String literal() {
      return unaryOperator.literal() + " " + expressionAtomNode.literal();
    }
  }

  public static class RelationalOperationTreeBinaryExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode;

    @Override
    public String literal() {
      return "BINARY " + expressionAtomNode.literal();
    }
  }

  public static class RelationalOperationTreeNestedExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public List<RelationalOperationTreeExpressionNode> expressionNodes = Lists.newArrayList();

    @Override
    public String literal() {
      List<String> expressions = Lists.newArrayList();
      for (RelationalOperationTreeExpressionNode expressionNode : expressionNodes) {
        expressions.add(expressionNode.literal());
      }
      return "(" + Joiner.on(", ").join(expressions) + ")";
    }
  }

  public static class RelationalOperationTreeNestedRowExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public List<RelationalOperationTreeExpressionNode> expressionNodes = Lists.newArrayList();

    @Override
    public String literal() {
      List<String> expressions = Lists.newArrayList();
      for (RelationalOperationTreeExpressionNode expressionNode : expressionNodes) {
        expressions.add(expressionNode.literal());
      }
      return "ROW (" + Joiner.on(", ").join(expressions) + ")";
    }
  }

  public static class RelationalOperationTreeExistsExpessionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeNode selectStatementTreeNode;

    @Override
    public String literal() {
      return "EXISTS " + selectStatementTreeNode.literal();
    }
  }

  public static class RelationalOperationTreeSubqueryExpessionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeNode selectStatementTreeNode;

    @Override
    public String literal() {
      return "(" + selectStatementTreeNode.literal() + ")";
    }
  }

  public static class RelationalOperationTreeIntervalExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeExpressionNode expressionNode;
    public DdlStatement.IntervalType intervalType;

    @Override
    public String literal() {
      return "INTERVAL " + expressionNode.literal() + " " + intervalType.literal();
    }
  }

  public static class RelationalOperationTreeBitExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode1;
    public RelationalBitOperatorEnum bitOperator;
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode2;

    @Override
    public String literal() {
      return bitOperator.literal() + "(" + expressionAtomNode1.literal() + ", "
          + expressionAtomNode2.literal() + ")";
    }
  }

  public static class RelationalOperationTreeMathExpressionAtomNode
      extends RelationalOperationTreeExpressionAtomNode {
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode1;
    public RelationalMathOperatorEnum mathOperator;
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode2;

    @Override
    public String literal() {
      return mathOperator.literal() + "(" + expressionAtomNode1.literal() + ", "
          + expressionAtomNode2.literal() + ")";
    }
  }
}