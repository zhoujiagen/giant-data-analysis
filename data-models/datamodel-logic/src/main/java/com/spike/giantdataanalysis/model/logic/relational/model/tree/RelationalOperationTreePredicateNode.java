package com.spike.giantdataanalysis.model.logic.relational.model.tree;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SubqueryComparasionPredicate;

public abstract class RelationalOperationTreePredicateNode
    extends RelationalOperationTreeExpressionNode {
  public static class RelationalOperationTreeInPredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode;
    public boolean not = false;
    public RelationalOperationTreeNode selectStmtTreeNode;
    public List<RelationalOperationTreeExpressionNode> expressionNodes;

    @Override
    public String literal() {
      String notString = "";
      if (not) {
        notString = " NOT ";
      }
      String inString = null;
      if (selectStmtTreeNode != null) {
        inString = selectStmtTreeNode.literal();
      } else {
        List<String> inStrings = Lists.newArrayList();
        for (RelationalOperationTreeExpressionNode expressionNode : expressionNodes) {
          inStrings.add(expressionNode.literal());
        }
        inString = Joiner.on(", ").join(inStrings);
      }
      return predicateNode.literal() + notString + " IN " + inString;
    }

  }

  public static class RelationalOperationTreeIsNullPredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode;
    public boolean isNull = false;

    @Override
    public String literal() {
      String isNullString = null;
      if (isNull) {
        isNullString = " IS NULL";
      } else {
        isNullString = " IS NOT NULL";
      }
      return predicateNode.literal() + isNullString;
    }
  }

  public static class RelationalOperationTreeBinaryComparasionPredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode1;
    public RelationalComparisonOperatorEnum comparisonOperator;
    public RelationalOperationTreePredicateNode predicateNode2;

    @Override
    public String literal() {
      return comparisonOperator.literal() + "(" + predicateNode1.literal() + ", "
          + predicateNode2.literal() + ")";
    }

  }

  public static class RelationalOperationTreeSubqueryComparasionPredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode1;
    public RelationalComparisonOperatorEnum comparisonOperator;
    public SubqueryComparasionPredicate.QuantifierEnum quantifier;
    public RelationalOperationTreeNode selectStatementTreeNode;

    @Override
    public String literal() {
      return quantifier.literal() + " " + comparisonOperator.literal() + "("
          + predicateNode1.literal() + ", " + selectStatementTreeNode.literal() + ")";
    }
  }

  public static class RelationalOperationTreeBetweenPredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode1;
    public boolean not = false;
    public RelationalOperationTreePredicateNode predicateNode2;
    public RelationalOperationTreePredicateNode predicateNode3;

    @Override
    public String literal() {
      String notString = "";
      if (not) {
        notString = " NOT ";
      }
      return predicateNode1.literal() + notString + "BETWEEN " + predicateNode2.literal() + ", "
          + predicateNode3.literal();
    }

  }

  public static class RelationalOperationTreeSoundsLikePredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode1;
    public RelationalOperationTreePredicateNode predicateNode2;

    @Override
    public String literal() {
      return predicateNode1.literal() + " SOUNDS LIKE " + predicateNode2.literal();
    }
  }

  public static class RelationalOperationTreeLikePredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode1;
    public boolean not = false;
    public RelationalOperationTreePredicateNode predicateNode2;
    public String escape;

    @Override
    public String literal() {
      String notString = "";
      if (not) {
        notString = " NOT ";
      }
      String escapeString = escape;
      if (escapeString == null) {
        escapeString = "";
      } else {
        escapeString = " " + escapeString;
      }

      return predicateNode1.literal() + notString + " LIKE" + predicateNode2.literal()
          + escapeString;
    }
  }

  public static class RelationalOperationTreeRegexpPredicateNode
      extends RelationalOperationTreePredicateNode {
    public RelationalOperationTreePredicateNode predicateNode1;
    public boolean not = false;
    public RegexpPredicate.RegexType regex;
    public RelationalOperationTreePredicateNode predicateNode2;

    @Override
    public String literal() {
      String notString = "";
      if (not) {
        notString = " NOT ";
      }
      return predicateNode1.literal() + notString + regex.literal() + " "
          + predicateNode2.literal();
    }

  }

  public static class RelationalOperationTreeExpressionAtomPredicateNode
      extends RelationalOperationTreePredicateNode {
    public String localId;
    public RelationalOperationTreeExpressionAtomNode expressionAtomNode;

    @Override
    public String literal() {
      if (localId != null) {
        return localId + ":= " + expressionAtomNode.literal();
      } else {
        return expressionAtomNode.literal();
      }
    }

  }
}