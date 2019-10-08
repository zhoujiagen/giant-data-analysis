package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalExpressionUtils;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.BetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.BinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.ExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.InPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.LikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.LogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.NotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.PredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.PasswordFunctionClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ScalarFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.SpecificFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.UdfFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterError;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterSymbolLinkTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.core.RelationalInterpreterSymbolTypeEnum;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeBinaryExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeBitExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeCollateExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeConstantExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeExistsExpessionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeFullColumnNameExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeFunctionCallExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeIntervalExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeMathExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeMysqlVariableExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeNestedExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeNestedRowExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeSubqueryExpessionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionAtomNode.RelationalOperationTreeUnaryExpressionAtomNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionNode.RelationalOperationTreeIsExpressionNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionNode.RelationalOperationTreeLogicalExpressionNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeExpressionNode.RelationalOperationTreeNotExpressionNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeBetweenPredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeBinaryComparasionPredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeExpressionAtomPredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeInPredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeIsNullPredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeLikePredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeRegexpPredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeSoundsLikePredicateNode;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreePredicateNode.RelationalOperationTreeSubqueryComparasionPredicateNode;

/**
 * Interpreter Base: Predicate => Model.
 * <p>
 * postfix: 区分同一作用域中两个或多个相同类型的子作用域.
 */
public abstract class RelationalInterpreterBase {

  public abstract RelationalOperationTreeNode interpreter(RelationalInterpreterContext context,
      SelectStatement selectStatement, String postfix);

  public List<RelationalOperationTreeExpressionNode>
      interpreter(RelationalInterpreterContext context, Expressions expressions, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(expressions != null);
    Preconditions.checkArgument(postfix != null);

    List<RelationalOperationTreeExpressionNode> result = Lists.newArrayList();
    final List<Expression> expressionList = expressions.expressions;
    for (Expression expression : expressionList) {
      result.add(this.interpreter(context, expression, postfix));
    }

    return result;
  }

  public RelationalOperationTreeExpressionNode interpreter(RelationalInterpreterContext context,
      Expression expression, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(expression != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("Expression", postfix);
    try {
      if (expression instanceof NotExpression) {
        final NotExpression notExpression = (NotExpression) expression;
        context.enterScope("NotExpression", postfix);
        try {
          RelationalOperationTreeNotExpressionNode result =
              new RelationalOperationTreeNotExpressionNode();
          result.expressionNode = this.interpreter(context, notExpression.expression, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expression instanceof LogicalExpression) {
        final LogicalExpression logicalExpression = (LogicalExpression) expression;

        context.enterScope("LogicalExpression", postfix);
        try {
          RelationalOperationTreeLogicalExpressionNode result =
              new RelationalOperationTreeLogicalExpressionNode();
          result.expressionNode1 =
              this.interpreter(context, logicalExpression.first, postfix + "1");
          final RelationalLogicalOperatorEnum operator = logicalExpression.operator;
          context.addSymbol(operator.literal(), RelationalInterpreterSymbolTypeEnum.OPERATOR_NAME);
          result.operator = operator;
          result.expressionNode2 =
              this.interpreter(context, logicalExpression.second, postfix + "2");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expression instanceof IsExpression) {
        final IsExpression isExpression = (IsExpression) expression;

        context.enterScope("IsExpression", postfix);
        try {
          RelationalOperationTreeIsExpressionNode result =
              new RelationalOperationTreeIsExpressionNode();
          result.predicateNode = this.interpreter(context, isExpression.predicate, postfix);
          Boolean not = isExpression.not;
          if (Boolean.TRUE.equals(not)) {
            result.not = false;
            context.addSymbol("NOT", RelationalInterpreterSymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          IsExpression.TestValue testValue = isExpression.testValue;
          result.testValue = testValue;
          context.addSymbol(testValue.literal(), RelationalInterpreterSymbolTypeEnum.CONSTANT);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expression instanceof PredicateExpression) {
        final PredicateExpression predicateExpression = (PredicateExpression) expression;
        return this.interpreter(context, predicateExpression, postfix);
      }

      else {
        throw RelationalInterpreterError.make(expression);
      }
    } finally {
      context.leaveScope();
    }
  }

  public RelationalOperationTreePredicateNode interpreter(RelationalInterpreterContext context,
      PredicateExpression predicateExpression, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(predicateExpression != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("PredicateExpression", postfix);
    try {
      if (predicateExpression instanceof InPredicate) {
        final InPredicate inPredicate = (InPredicate) predicateExpression;

        context.enterScope("InPredicate", postfix);
        try {
          RelationalOperationTreeInPredicateNode result =
              new RelationalOperationTreeInPredicateNode();
          result.predicateNode = this.interpreter(context, inPredicate.predicate, postfix);
          final Boolean not = inPredicate.not;
          if (Boolean.TRUE.equals(not)) {
            result.not = true;
            context.addSymbol("NOT", RelationalInterpreterSymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          final SelectStatement selectStatement = inPredicate.selectStatement;
          result.selectStmtTreeNode = this.interpreter(context, selectStatement, postfix);
          final Expressions expressions = inPredicate.expressions;
          if (selectStatement != null) {
            this.interpreter(context, selectStatement, postfix);
          } else {
            result.expressionNodes = this.interpreter(context, expressions, postfix);
          }
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof IsNullPredicate) {
        final IsNullPredicate isNullPredicate = (IsNullPredicate) predicateExpression;

        context.enterScope("IsNullPredicate", postfix);
        try {
          RelationalOperationTreeIsNullPredicateNode result =
              new RelationalOperationTreeIsNullPredicateNode();
          result.predicateNode = this.interpreter(context, isNullPredicate.predicate, postfix);
          final NullNotnull nullNotnull = isNullPredicate.nullNotnull;
          result.isNull = RelationalExpressionUtils.isNull(nullNotnull);
          context.addSymbol(nullNotnull.literal(), RelationalInterpreterSymbolTypeEnum.CONSTANT);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof BinaryComparasionPredicate) {
        final BinaryComparasionPredicate binaryComparasionPredicate =
            (BinaryComparasionPredicate) predicateExpression;

        context.enterScope("BinaryComparasionPredicate", postfix);
        try {
          RelationalOperationTreeBinaryComparasionPredicateNode result =
              new RelationalOperationTreeBinaryComparasionPredicateNode();
          result.predicateNode1 =
              this.interpreter(context, binaryComparasionPredicate.left, postfix + "1");
          final RelationalComparisonOperatorEnum comparisonOperator =
              binaryComparasionPredicate.comparisonOperator;
          result.comparisonOperator = comparisonOperator;
          context.addSymbol(comparisonOperator.literal(),
            RelationalInterpreterSymbolTypeEnum.OPERATOR_NAME);
          result.predicateNode2 =
              this.interpreter(context, binaryComparasionPredicate.right, postfix + "2");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof SubqueryComparasionPredicate) {
        final SubqueryComparasionPredicate subqueryComparasionPredicate =
            (SubqueryComparasionPredicate) predicateExpression;

        context.enterScope("SubqueryComparasionPredicate", postfix);
        try {
          RelationalOperationTreeSubqueryComparasionPredicateNode result =
              new RelationalOperationTreeSubqueryComparasionPredicateNode();
          result.predicateNode1 =
              this.interpreter(context, subqueryComparasionPredicate.predicate, postfix);
          final RelationalComparisonOperatorEnum comparisonOperator =
              subqueryComparasionPredicate.comparisonOperator;
          result.comparisonOperator = comparisonOperator;
          context.addSymbol(comparisonOperator.literal(),
            RelationalInterpreterSymbolTypeEnum.OPERATOR_NAME);
          final SubqueryComparasionPredicate.QuantifierEnum quantifier =
              subqueryComparasionPredicate.quantifier;
          result.quantifier = quantifier;
          context.addSymbol(quantifier.literal(),
            RelationalInterpreterSymbolTypeEnum.QUALIFIER_PREDICATE);
          result.selectStatementTreeNode =
              this.interpreter(context, subqueryComparasionPredicate.selectStatement, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof BetweenPredicate) {
        final BetweenPredicate betweenPredicate = (BetweenPredicate) predicateExpression;

        context.enterScope("BetweenPredicate", postfix);
        try {
          RelationalOperationTreeBetweenPredicateNode result =
              new RelationalOperationTreeBetweenPredicateNode();
          result.predicateNode1 = this.interpreter(context, betweenPredicate.first, postfix + "1");
          final Boolean not = betweenPredicate.not;
          if (Boolean.TRUE.equals(not)) {
            result.not = true;
            context.addSymbol("NOT", RelationalInterpreterSymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          result.predicateNode2 = this.interpreter(context, betweenPredicate.second, postfix + "2");
          result.predicateNode3 = this.interpreter(context, betweenPredicate.third, postfix + "3");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof SoundsLikePredicate) {
        final SoundsLikePredicate soundsLikePredicate = (SoundsLikePredicate) predicateExpression;

        context.enterScope("SoundsLikePredicate", postfix);
        try {
          RelationalOperationTreeSoundsLikePredicateNode result =
              new RelationalOperationTreeSoundsLikePredicateNode();
          result.predicateNode1 =
              this.interpreter(context, soundsLikePredicate.first, postfix + "1");
          result.predicateNode2 =
              this.interpreter(context, soundsLikePredicate.second, postfix + "2");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof LikePredicate) {
        final LikePredicate likePredicate = (LikePredicate) predicateExpression;

        context.enterScope("LikePredicate", postfix);
        try {
          RelationalOperationTreeLikePredicateNode result =
              new RelationalOperationTreeLikePredicateNode();
          result.predicateNode1 = this.interpreter(context, likePredicate.first, postfix + "1");
          final Boolean not = likePredicate.not;
          if (Boolean.TRUE.equals(not)) {
            result.not = true;
            context.addSymbol("NOT", RelationalInterpreterSymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          result.predicateNode2 = this.interpreter(context, likePredicate.second, postfix + "2");
          String escapeString = likePredicate.stringLiteral;
          if (escapeString != null) {
            result.escape = escapeString;
            context.addSymbol(escapeString, RelationalInterpreterSymbolTypeEnum.CONSTANT);
          }
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof RegexpPredicate) {
        final RegexpPredicate regexpPredicate = (RegexpPredicate) predicateExpression;

        context.enterScope("RegexpPredicate", postfix);
        try {
          RelationalOperationTreeRegexpPredicateNode result =
              new RelationalOperationTreeRegexpPredicateNode();
          result.predicateNode1 = this.interpreter(context, regexpPredicate.first, postfix + "1");
          final Boolean not = regexpPredicate.not;
          if (Boolean.TRUE.equals(not)) {
            result.not = true;
            context.addSymbol("NOT", RelationalInterpreterSymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          final RegexpPredicate.RegexType regexType = regexpPredicate.regex;
          result.regex = regexType;
          context.addSymbol(regexType.literal(), RelationalInterpreterSymbolTypeEnum.REGEX_TYPE);
          result.predicateNode2 = this.interpreter(context, regexpPredicate.second, postfix + "2");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof ExpressionAtomPredicate) {
        final ExpressionAtomPredicate expressionAtomPredicate =
            (ExpressionAtomPredicate) predicateExpression;

        context.enterScope("ExpressionAtomPredicate", postfix);
        try {
          RelationalOperationTreeExpressionAtomPredicateNode result =
              new RelationalOperationTreeExpressionAtomPredicateNode();
          final String localId = expressionAtomPredicate.localId;
          if (StringUtils.isNotBlank(localId)) {
            result.localId = localId;
            context.addSymbol(localId, RelationalInterpreterSymbolTypeEnum.LOCAL_ID);
          }
          final ExpressionAtom expressionAtom = expressionAtomPredicate.expressionAtom;
          result.expressionAtomNode = this.interpreter(context, expressionAtom, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else {
        throw RelationalInterpreterError.make(predicateExpression);
      }

    } finally {
      context.leaveScope();
    }
  }

  public RelationalOperationTreeExpressionAtomNode interpreter(RelationalInterpreterContext context,
      ExpressionAtom expressionAtom, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(expressionAtom != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("ExpressionAtom", postfix);
    try {
      if (expressionAtom instanceof Constant) {
        final Constant constant = (Constant) expressionAtom;
        return this.interpreter(context, constant, postfix);
      }

      else if (expressionAtom instanceof FullColumnName) {
        final FullColumnName fullColumnName = (FullColumnName) expressionAtom;
        return this.interpreter(context, fullColumnName, postfix);
      }

      else if (expressionAtom instanceof FunctionCall) {
        final FunctionCall functionCall = (FunctionCall) expressionAtom;
        return this.interpreter(context, functionCall, postfix);
      }

      else if (expressionAtom instanceof Collate) {
        final Collate collate = (Collate) expressionAtom;
        return this.interpreter(context, collate, postfix);
      }

      else if (expressionAtom instanceof MysqlVariable) {
        final MysqlVariable mysqlVariable = (MysqlVariable) expressionAtom;
        return this.interpreter(context, mysqlVariable, postfix);
      }

      else if (expressionAtom instanceof UnaryExpressionAtom) {
        final UnaryExpressionAtom unaryExpressionAtom = (UnaryExpressionAtom) expressionAtom;

        context.enterScope("UnaryExpressionAtom", postfix);
        try {
          RelationalOperationTreeUnaryExpressionAtomNode result =
              new RelationalOperationTreeUnaryExpressionAtomNode();
          final RelationalUnaryOperatorEnum unaryOperator = unaryExpressionAtom.unaryOperator;
          result.unaryOperator = unaryOperator;
          context.addSymbol(unaryOperator.literal(),
            RelationalInterpreterSymbolTypeEnum.OPERATOR_NAME);
          result.expressionAtomNode =
              this.interpreter(context, unaryExpressionAtom.expressionAtom, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof BinaryExpressionAtom) {
        final BinaryExpressionAtom binaryExpressionAtom = (BinaryExpressionAtom) expressionAtom;

        context.enterScope("BinaryExpressionAtom", postfix);
        try {
          RelationalOperationTreeBinaryExpressionAtomNode result =
              new RelationalOperationTreeBinaryExpressionAtomNode();
          result.expressionAtomNode =
              this.interpreter(context, binaryExpressionAtom.expressionAtom, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof NestedExpressionAtom) {
        final NestedExpressionAtom nestedExpressionAtom = (NestedExpressionAtom) expressionAtom;

        context.enterScope("NestedExpressionAtom", postfix);
        try {
          RelationalOperationTreeNestedExpressionAtomNode result =
              new RelationalOperationTreeNestedExpressionAtomNode();
          final List<Expression> expressions = nestedExpressionAtom.expressions;
          for (Expression expression : expressions) {
            result.expressionNodes.add(this.interpreter(context, expression, postfix));
          }
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof NestedRowExpressionAtom) {
        final NestedRowExpressionAtom nestedRowExpressionAtom =
            (NestedRowExpressionAtom) expressionAtom;

        context.enterScope("NestedRowExpressionAtom", postfix);
        try {
          RelationalOperationTreeNestedRowExpressionAtomNode result =
              new RelationalOperationTreeNestedRowExpressionAtomNode();
          final List<Expression> expressions = nestedRowExpressionAtom.expressions;
          for (Expression expression : expressions) {
            result.expressionNodes.add(this.interpreter(context, expression, postfix));
          }
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof ExistsExpessionAtom) {
        final ExistsExpessionAtom existsExpessionAtom = (ExistsExpessionAtom) expressionAtom;

        context.enterScope("ExistsExpessionAtom", postfix);
        try {
          RelationalOperationTreeExistsExpessionAtomNode result =
              new RelationalOperationTreeExistsExpessionAtomNode();
          result.selectStatementTreeNode =
              this.interpreter(context, existsExpessionAtom.selectStatement, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof SubqueryExpessionAtom) {
        final SubqueryExpessionAtom subqueryExpessionAtom = (SubqueryExpessionAtom) expressionAtom;

        context.enterScope("SubqueryExpessionAtom", postfix);
        try {
          RelationalOperationTreeSubqueryExpessionAtomNode result =
              new RelationalOperationTreeSubqueryExpessionAtomNode();
          result.selectStatementTreeNode =
              this.interpreter(context, subqueryExpessionAtom.selectStatement, postfix);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof IntervalExpressionAtom) {
        final IntervalExpressionAtom intervalExpressionAtom =
            (IntervalExpressionAtom) expressionAtom;

        context.enterScope("IntervalExpressionAtom", postfix);
        try {
          RelationalOperationTreeIntervalExpressionAtomNode result =
              new RelationalOperationTreeIntervalExpressionAtomNode();
          result.expressionNode =
              this.interpreter(context, intervalExpressionAtom.expression, postfix);
          final DdlStatement.IntervalType intervalType = intervalExpressionAtom.intervalType;
          result.intervalType = intervalType;
          context.addSymbol(intervalType.literal(),
            RelationalInterpreterSymbolTypeEnum.INTERVAL_TYPE);
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof BitExpressionAtom) {
        final BitExpressionAtom bitExpressionAtom = (BitExpressionAtom) expressionAtom;

        context.enterScope("BitExpressionAtom", postfix);
        try {
          RelationalOperationTreeBitExpressionAtomNode result =
              new RelationalOperationTreeBitExpressionAtomNode();
          result.expressionAtomNode1 =
              this.interpreter(context, bitExpressionAtom.left, postfix + "1");
          final RelationalBitOperatorEnum bitOperator = bitExpressionAtom.bitOperator;
          result.bitOperator = bitOperator;
          context.addSymbol(bitOperator.literal(),
            RelationalInterpreterSymbolTypeEnum.OPERATOR_NAME);
          result.expressionAtomNode2 =
              this.interpreter(context, bitExpressionAtom.right, postfix + "2");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof MathExpressionAtom) {
        final MathExpressionAtom mathExpressionAtom = (MathExpressionAtom) expressionAtom;

        context.enterScope("MathExpressionAtom", postfix);
        try {
          RelationalOperationTreeMathExpressionAtomNode result =
              new RelationalOperationTreeMathExpressionAtomNode();
          result.expressionAtomNode1 =
              this.interpreter(context, mathExpressionAtom.left, postfix + "1");
          final RelationalMathOperatorEnum mathOperator = mathExpressionAtom.mathOperator;
          result.mathOperator = mathOperator;
          context.addSymbol(mathOperator.literal(),
            RelationalInterpreterSymbolTypeEnum.OPERATOR_NAME);
          result.expressionAtomNode2 =
              this.interpreter(context, mathExpressionAtom.right, postfix + "2");
          return result;
        } finally {
          context.leaveScope();
        }
      }

      else {
        throw RelationalInterpreterError.make(expressionAtom);
      }
    } finally {
      context.leaveScope();
    }
  }

  public RelationalOperationTreeCollateExpressionAtomNode
      interpreter(RelationalInterpreterContext context, Collate collate, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(collate != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperationTreeCollateExpressionAtomNode result =
        new RelationalOperationTreeCollateExpressionAtomNode();

    context.enterScope("Collate", postfix);
    try {
      result.expressionAtomNode = this.interpreter(context, collate.expressionAtom, postfix);
      final DBObjects.CollationName collateName = collate.collationName;
      result.collationName = collateName.literal();
      context.addSymbol(RelationalExpressionUtils.rawLiteral(collateName),
        RelationalInterpreterSymbolTypeEnum.COLLATION_NAME);
    } finally {
      context.leaveScope();
    }

    return result;
  }

  /**
   * <pre>
   * 
   * 反例: `producerC#` = `cert#`
   * 
  SELECT name, SUM(length)
  FROM MovieExec, Movies
  WHERE `producerC#` = `cert#`
  GROUP BY name
  HAVING MIN(year) < 1930;
   * </pre>
   * 
   * @param context
   * @param constant
   * @param postfix
   */
  public RelationalOperationTreeConstantExpressionAtomNode
      interpreter(RelationalInterpreterContext context, Constant constant, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(constant != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperationTreeConstantExpressionAtomNode result =
        new RelationalOperationTreeConstantExpressionAtomNode();
    result.constant = constant;
    final Constant.Type type = constant.type;
    final String literal = constant.literal();

    if (!literal.startsWith(RelationalExpressionUtils.NONE_STRING_LITERAL_PREFIX)) {
      context.enterScope("Constant", postfix);
      try {
        context.addSymbol(literal, RelationalInterpreterSymbolTypeEnum.CONSTANT);
        context.addLink(RelationalInterpreterSymbolLinkTypeEnum.TYPE, literal, type.literal());
      } finally {
        context.leaveScope();
      }
    } else {
      context.addSymbol(
        literal.replaceAll(RelationalExpressionUtils.NONE_STRING_LITERAL_PREFIX, ""),
        RelationalInterpreterSymbolTypeEnum.ATTRIBUTE_NAME);
    }

    return result;
  }

  public RelationalOperationTreeMysqlVariableExpressionAtomNode interpreter(
      RelationalInterpreterContext context, MysqlVariable mysqlVariable, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(mysqlVariable != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperationTreeMysqlVariableExpressionAtomNode result =
        new RelationalOperationTreeMysqlVariableExpressionAtomNode();
    context.enterScope("MysqlVariable", postfix);
    try {
      result.mysqlVariable = mysqlVariable;
      context.addSymbol(mysqlVariable.literal(),
        RelationalInterpreterSymbolTypeEnum.MYSQL_VARIABLE);
    } finally {
      context.leaveScope();
    }
    return result;
  }

  public RelationalOperationTreeFullColumnNameExpressionAtomNode interpreter(
      RelationalInterpreterContext context, FullColumnName fullColumnName, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(fullColumnName != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperationTreeFullColumnNameExpressionAtomNode result =
        new RelationalOperationTreeFullColumnNameExpressionAtomNode();
    result.fullColumnName = fullColumnName;

    context.enterScope("FullColumnName", postfix);
    try {
      Triple<String, String, String> triple = RelationalExpressionUtils.triple(fullColumnName);
      String database = triple.getLeft();
      String table = triple.getMiddle();
      String column = triple.getRight();
      context.addSymbol(column, RelationalInterpreterSymbolTypeEnum.ATTRIBUTE_NAME);
      if (database != null) {
        context.addSymbol(database, RelationalInterpreterSymbolTypeEnum.DATABASE_NAME);
      }
      if (table != null) {
        context.addSymbol(table, RelationalInterpreterSymbolTypeEnum.TABLE_NAME);
        context.addLink(RelationalInterpreterSymbolLinkTypeEnum.ATTRIBUTE_OF, column, table);
        if (database != null) {
          context.addLink(RelationalInterpreterSymbolLinkTypeEnum.TABLE_OF_DATABASE, table,
            database);
        }
      }
    } finally {
      context.leaveScope();
    }

    return result;
  }

  // TODO(zhoujiagen) implement this!!!
  public RelationalOperationTreeFunctionCallExpressionAtomNode
      interpreter(RelationalInterpreterContext context, FunctionCall functionCall, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(functionCall != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperationTreeFunctionCallExpressionAtomNode result =
        new RelationalOperationTreeFunctionCallExpressionAtomNode();

    context.enterScope("FunctionCall", postfix);
    try {

      if (functionCall instanceof SpecificFunction) {
        SpecificFunction specificFunction = (SpecificFunction) functionCall;
        throw RelationalInterpreterError.make(specificFunction);
      }

      else if (functionCall instanceof AggregateWindowedFunction) {
        AggregateWindowedFunction aggregateWindowedFunction =
            (AggregateWindowedFunction) functionCall;
        this.interpreter(context, aggregateWindowedFunction, postfix);
      }

      else if (functionCall instanceof ScalarFunctionCall) {
        ScalarFunctionCall scalarFunctionCall = (ScalarFunctionCall) functionCall;
        throw RelationalInterpreterError.make(scalarFunctionCall);
      }

      else if (functionCall instanceof UdfFunctionCall) {
        UdfFunctionCall udfFunctionCall = (UdfFunctionCall) functionCall;
        throw RelationalInterpreterError.make(udfFunctionCall);
      }

      else if (functionCall instanceof PasswordFunctionClause) {
        PasswordFunctionClause passwordFunctionClause = (PasswordFunctionClause) functionCall;
        throw RelationalInterpreterError.make(passwordFunctionClause);
      }

      else {
        throw RelationalInterpreterError.make(functionCall);
      }

    } finally {
      context.leaveScope();
    }

    return result;
  }

  public void interpreter(RelationalInterpreterContext context,
      AggregateWindowedFunction aggregateWindowedFunction, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(aggregateWindowedFunction != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("AggregateWindowedFunction", postfix);
    try {
      AggregateWindowedFunction.Type type = aggregateWindowedFunction.type;
      switch (type) {
      case AVG:
      case MAX:
      case MIN:
      case SUM:
        context.addSymbol(type.literal(), RelationalInterpreterSymbolTypeEnum.FUNCTION_NAME);
        final AggregateWindowedFunction.AggregatorEnum aggregator =
            aggregateWindowedFunction.aggregator;
        if (aggregator != null) {
          context.addSymbol(aggregator.literal(),
            RelationalInterpreterSymbolTypeEnum.SPEFICIER_AGG);
        }
        final FunctionArg functionArg = aggregateWindowedFunction.functionArg;
        this.interpreter(context, functionArg, postfix);
        break;
      case COUNT:
        // Preconditions.checkArgument(functionArg != null);
        Preconditions.checkArgument(false);
        break;
      case COUNT_DISTINCT:
        // Preconditions.checkArgument(functionArgs != null);
        Preconditions.checkArgument(false);
        break;
      case BIT_AND:
      case BIT_OR:
      case BIT_XOR:
      case STD:
      case STDDEV:
      case STDDEV_POP:
      case STDDEV_SAMP:
      case VAR_POP:
        // Preconditions.checkArgument(functionArg != null);
        Preconditions.checkArgument(false);
        break;
      case GROUP_CONCAT:
        // Preconditions.checkArgument(functionArgs != null);
        Preconditions.checkArgument(false);
        break;
      default:
        Preconditions.checkArgument(false);
        break;
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(RelationalInterpreterContext context, FunctionArg functionArg,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(functionArg != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("FunctionArg", postfix);
    try {

      FunctionArg.Type type = functionArg.type;
      switch (type) {
      case CONSTANT:
        context.addSymbol(functionArg.literal(), RelationalInterpreterSymbolTypeEnum.CONSTANT);
        break;
      case FULL_COLUMN_NAME:
        this.interpreter(context, functionArg.fullColumnName, postfix);
        break;
      case FUNCTION_CALL:
        this.interpreter(context, functionArg.functionCall, postfix);
        break;
      case EXPRESSION:
        this.interpreter(context, functionArg.expression, postfix);
      default:
        throw RelationalInterpreterError.make(functionArg);
      }

    } finally {
      context.leaveScope();
    }
  }

}
