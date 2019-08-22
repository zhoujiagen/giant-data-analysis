package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.DdlStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.BetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.BinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.ExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.InPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.IsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.LogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.NotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.PredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.SoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression.SubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.PasswordFunctionClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.ScalarFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.SpecificFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.UdfFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalOperation;

/**
 * Interpreter Base: Predicate => Model.
 * <p>
 * postfix: 区分同一作用域中两个或多个相同类型的子作用域.
 */
public abstract class REInterpreterBase {

  public abstract RelationalOperation interpreter(REInterpreterContext context,
      SelectStatement selectStatement, String postfix);

  public void interpreter(REInterpreterContext context, Expressions expressions, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(expressions != null);
    Preconditions.checkArgument(postfix != null);

    List<Expression> expressionList = expressions.expressions;
    for (Expression expression : expressionList) {
      this.interpreter(context, expression, postfix);
    }
  }

  public void interpreter(REInterpreterContext context, Expression expression, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(expression != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("Expression", postfix);
    try {
      if (expression instanceof NotExpression) {
        NotExpression notExpression = (NotExpression) expression;
        context.enterScope("NotExpression", postfix);
        try {
          this.interpreter(context, notExpression.expression, postfix);
        } finally {
          context.leaveScope();
        }
      }

      else if (expression instanceof LogicalExpression) {
        LogicalExpression logicalExpression = (LogicalExpression) expression;

        context.enterScope("LogicalExpression", postfix);
        try {
          this.interpreter(context, logicalExpression.first, postfix + "1");
          final RelationalLogicalOperatorEnum operator = logicalExpression.operator;
          context.addSymbol(operator.literal(), RESymbolTypeEnum.OPERATOR_NAME);
          this.interpreter(context, logicalExpression.second, postfix + "2");
        } finally {
          context.leaveScope();
        }
      }

      else if (expression instanceof IsExpression) {
        IsExpression isExpression = (IsExpression) expression;

        context.enterScope("IsExpression", postfix);
        try {
          this.interpreter(context, isExpression.predicate, postfix);
          Boolean not = isExpression.not;
          if (Boolean.TRUE.equals(not)) {
            context.addSymbol("NOT", RESymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          IsExpression.TestValue testValue = isExpression.testValue;
          context.addSymbol(testValue.literal(), RESymbolTypeEnum.CONSTANT);
        } finally {
          context.leaveScope();
        }
      }

      else if (expression instanceof PredicateExpression) {
        PredicateExpression predicateExpression = (PredicateExpression) expression;
        this.interpreter(context, predicateExpression, postfix);
      }

      else {
        throw REInterpreterError.make(expression);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, PredicateExpression predicateExpression,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(predicateExpression != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("PredicateExpression", postfix);
    try {
      if (predicateExpression instanceof InPredicate) {
        InPredicate inPredicate = (InPredicate) predicateExpression;

        context.enterScope("InPredicate", postfix);
        try {
          this.interpreter(context, inPredicate.predicate, postfix);
          Boolean not = inPredicate.not;
          if (Boolean.TRUE.equals(not)) {
            context.addSymbol("NOT", RESymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          SelectStatement selectStatement = inPredicate.selectStatement;
          Expressions expressions = inPredicate.expressions;
          if (selectStatement != null) {
            this.interpreter(context, selectStatement, postfix);
          } else {
            this.interpreter(context, expressions, postfix);
          }
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof IsNullPredicate) {
        IsNullPredicate isNullPredicate = (IsNullPredicate) predicateExpression;

        context.enterScope("IsNullPredicate", postfix);
        try {
          this.interpreter(context, isNullPredicate.predicate, postfix);
          NullNotnull nullNotnull = isNullPredicate.nullNotnull;
          context.addSymbol(nullNotnull.literal(), RESymbolTypeEnum.CONSTANT);
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof BinaryComparasionPredicate) {
        BinaryComparasionPredicate binaryComparasionPredicate =
            (BinaryComparasionPredicate) predicateExpression;

        context.enterScope("BinaryComparasionPredicate", postfix);
        try {
          this.interpreter(context, binaryComparasionPredicate.left, postfix + "1");
          final RelationalComparisonOperatorEnum comparisonOperator =
              binaryComparasionPredicate.comparisonOperator;
          context.addSymbol(comparisonOperator.literal(), RESymbolTypeEnum.OPERATOR_NAME);
          this.interpreter(context, binaryComparasionPredicate.right, postfix + "2");
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof SubqueryComparasionPredicate) {
        SubqueryComparasionPredicate subqueryComparasionPredicate =
            (SubqueryComparasionPredicate) predicateExpression;

        context.enterScope("SubqueryComparasionPredicate", postfix);
        try {
          this.interpreter(context, subqueryComparasionPredicate.predicate, postfix);
          final RelationalComparisonOperatorEnum comparisonOperator =
              subqueryComparasionPredicate.comparisonOperator;
          context.addSymbol(comparisonOperator.literal(), RESymbolTypeEnum.OPERATOR_NAME);
          final SubqueryComparasionPredicate.QuantifierEnum quantifier =
              subqueryComparasionPredicate.quantifier;
          context.addSymbol(quantifier.literal(), RESymbolTypeEnum.QUALIFIER_PREDICATE);
          this.interpreter(context, subqueryComparasionPredicate.selectStatement, postfix);
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof BetweenPredicate) {
        BetweenPredicate betweenPredicate = (BetweenPredicate) predicateExpression;

        context.enterScope("BetweenPredicate", postfix);
        try {
          this.interpreter(context, betweenPredicate.first, postfix + "1");
          final Boolean not = betweenPredicate.not;
          if (Boolean.TRUE.equals(not)) {
            context.addSymbol("NOT", RESymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          this.interpreter(context, betweenPredicate.second, postfix + "2");
          this.interpreter(context, betweenPredicate.third, postfix + "3");
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof SoundsLikePredicate) {
        SoundsLikePredicate soundsLikePredicate = (SoundsLikePredicate) predicateExpression;

        context.enterScope("SoundsLikePredicate", postfix);
        try {
          this.interpreter(context, soundsLikePredicate.first, postfix + "1");
          this.interpreter(context, soundsLikePredicate.second, postfix + "2");
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof LikePredicate) {
        LikePredicate likePredicate = (LikePredicate) predicateExpression;

        context.enterScope("LikePredicate", postfix);
        try {
          this.interpreter(context, likePredicate.first, postfix + "1");
          final Boolean not = likePredicate.not;
          if (Boolean.TRUE.equals(not)) {
            context.addSymbol("NOT", RESymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          this.interpreter(context, likePredicate.second, postfix + "2");
          String escapeString = likePredicate.stringLiteral;
          if (escapeString != null) {
            context.addSymbol(escapeString, RESymbolTypeEnum.CONSTANT);
          }
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof RegexpPredicate) {
        RegexpPredicate regexpPredicate = (RegexpPredicate) predicateExpression;

        context.enterScope("RegexpPredicate", postfix);
        try {
          this.interpreter(context, regexpPredicate.first, postfix + "1");
          final Boolean not = regexpPredicate.not;
          if (Boolean.TRUE.equals(not)) {
            context.addSymbol("NOT", RESymbolTypeEnum.SPEFICIER_TEST_VALUE);
          }
          final RegexpPredicate.RegexType regexType = regexpPredicate.regex;
          context.addSymbol(regexType.literal(), RESymbolTypeEnum.REGEX_TYPE);
          this.interpreter(context, regexpPredicate.second, postfix + "2");
        } finally {
          context.leaveScope();
        }
      }

      else if (predicateExpression instanceof ExpressionAtomPredicate) {
        ExpressionAtomPredicate expressionAtomPredicate =
            (ExpressionAtomPredicate) predicateExpression;

        context.enterScope("ExpressionAtomPredicate", postfix);
        try {
          String localId = expressionAtomPredicate.localId;
          if (StringUtils.isNotBlank(localId)) {
            context.addSymbol(localId, RESymbolTypeEnum.VARIABLE);
          }
          ExpressionAtom expressionAtom = expressionAtomPredicate.expressionAtom;
          this.interpreter(context, expressionAtom, postfix);
        } finally {
          context.leaveScope();
        }
      }

    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, ExpressionAtom expressionAtom,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(expressionAtom != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("ExpressionAtom", postfix);
    try {
      if (expressionAtom instanceof Constant) {
        Constant constant = (Constant) expressionAtom;

        this.interpreter(context, constant, postfix);
      }

      else if (expressionAtom instanceof FullColumnName) {
        FullColumnName fullColumnName = (FullColumnName) expressionAtom;
        this.interpreter(context, fullColumnName, postfix);
      }

      else if (expressionAtom instanceof FunctionCall) {
        FunctionCall functionCall = (FunctionCall) expressionAtom;
        this.interpreter(context, functionCall, postfix);
      }

      else if (expressionAtom instanceof Collate) {
        Collate collate = (Collate) expressionAtom;
        this.interpreter(context, collate, postfix);
      }

      else if (expressionAtom instanceof MysqlVariable) {
        MysqlVariable mysqlVariable = (MysqlVariable) expressionAtom;
        this.interpreter(context, mysqlVariable, postfix);
      }

      else if (expressionAtom instanceof UnaryExpressionAtom) {
        UnaryExpressionAtom unaryExpressionAtom = (UnaryExpressionAtom) expressionAtom;

        context.enterScope("UnaryExpressionAtom", postfix);
        try {
          RelationalUnaryOperatorEnum unaryOperator = unaryExpressionAtom.unaryOperator;
          context.addSymbol(unaryOperator.literal(), RESymbolTypeEnum.OPERATOR_NAME);
          this.interpreter(context, unaryExpressionAtom.expressionAtom, postfix);
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof BinaryExpressionAtom) {
        BinaryExpressionAtom binaryExpressionAtom = (BinaryExpressionAtom) expressionAtom;

        context.enterScope("BinaryExpressionAtom", postfix);
        try {
          this.interpreter(context, binaryExpressionAtom.expressionAtom, postfix);
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof NestedExpressionAtom) {
        NestedExpressionAtom nestedExpressionAtom = (NestedExpressionAtom) expressionAtom;

        context.enterScope("NestedExpressionAtom", postfix);
        try {
          List<Expression> expressions = nestedExpressionAtom.expressions;
          for (Expression expression : expressions) {
            this.interpreter(context, expression, postfix);
          }
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof NestedRowExpressionAtom) {
        NestedRowExpressionAtom nestedRowExpressionAtom = (NestedRowExpressionAtom) expressionAtom;

        context.enterScope("NestedRowExpressionAtom", postfix);
        try {
          List<Expression> expressions = nestedRowExpressionAtom.expressions;
          for (Expression expression : expressions) {
            this.interpreter(context, expression, postfix);
          }
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof ExistsExpessionAtom) {
        ExistsExpessionAtom existsExpessionAtom = (ExistsExpessionAtom) expressionAtom;

        context.enterScope("ExistsExpessionAtom", postfix);
        try {
          this.interpreter(context, existsExpessionAtom.selectStatement, postfix);
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof SubqueryExpessionAtom) {
        SubqueryExpessionAtom subqueryExpessionAtom = (SubqueryExpessionAtom) expressionAtom;

        context.enterScope("SubqueryExpessionAtom", postfix);
        try {
          this.interpreter(context, subqueryExpessionAtom.selectStatement, postfix);
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof IntervalExpressionAtom) {
        IntervalExpressionAtom intervalExpressionAtom = (IntervalExpressionAtom) expressionAtom;

        context.enterScope("IntervalExpressionAtom", postfix);
        try {
          this.interpreter(context, intervalExpressionAtom.expression, postfix);
          final DdlStatement.IntervalType intervalType = intervalExpressionAtom.intervalType;
          context.addSymbol(intervalType.literal(), RESymbolTypeEnum.INTERVAL_TYPE);
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof BitExpressionAtom) {
        BitExpressionAtom bitExpressionAtom = (BitExpressionAtom) expressionAtom;

        context.enterScope("BitExpressionAtom", postfix);
        try {
          this.interpreter(context, bitExpressionAtom.left, postfix + "1");
          RelationalBitOperatorEnum bitOperator = bitExpressionAtom.bitOperator;
          context.addSymbol(bitOperator.literal(), RESymbolTypeEnum.OPERATOR_NAME);
          this.interpreter(context, bitExpressionAtom.right, postfix + "2");
        } finally {
          context.leaveScope();
        }
      }

      else if (expressionAtom instanceof MathExpressionAtom) {
        MathExpressionAtom mathExpressionAtom = (MathExpressionAtom) expressionAtom;

        context.enterScope("MathExpressionAtom", postfix);
        try {
          this.interpreter(context, mathExpressionAtom.left, postfix + "1");
          RelationalMathOperatorEnum mathOperator = mathExpressionAtom.mathOperator;
          context.addSymbol(mathOperator.literal(), RESymbolTypeEnum.OPERATOR_NAME);
          this.interpreter(context, mathExpressionAtom.right, postfix + "2");
        } finally {
          context.leaveScope();
        }
      }

      else {
        throw REInterpreterError.make(expressionAtom);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, Collate collate, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(collate != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("Collate", postfix);
    try {
      this.interpreter(context, collate.expressionAtom, postfix);
      DBObjects.CollationName collateName = collate.collationName;
      context.addSymbol(REUtils.rawLiteral(collateName), RESymbolTypeEnum.COLLATION_NAME);
    } finally {
      context.leaveScope();
    }
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
  public void interpreter(REInterpreterContext context, Constant constant, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(constant != null);
    Preconditions.checkArgument(postfix != null);

    Constant.Type type = constant.type;
    String literal = constant.literal();

    if (!literal.startsWith(REUtils.NONE_STRING_LITERAL_PREFIX)) {
      context.enterScope("Constant", postfix);
      try {
        context.addSymbol(literal, RESymbolTypeEnum.CONSTANT);
        context.addLink(RESymbolLinkTypeEnum.TYPE, literal, type.literal());
      } finally {
        context.leaveScope();
      }
    } else {
      context.addSymbol(literal.replaceAll(REUtils.NONE_STRING_LITERAL_PREFIX, ""),
        RESymbolTypeEnum.ATTRIBUTE_NAME);
    }

  }

  public void interpreter(REInterpreterContext context, MysqlVariable mysqlVariable,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(mysqlVariable != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("MysqlVariable", postfix);
    try {
      context.addSymbol(mysqlVariable.literal(), RESymbolTypeEnum.MYSQL_VARIABLE);
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, FullColumnName fullColumnName,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(fullColumnName != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("FullColumnName", postfix);
    try {
      Triple<String, String, String> triple = REUtils.triple(fullColumnName);
      String database = triple.getLeft();
      String table = triple.getMiddle();
      String column = triple.getRight();
      context.addSymbol(column, RESymbolTypeEnum.ATTRIBUTE_NAME);
      if (database != null) {
        context.addSymbol(database, RESymbolTypeEnum.DATABASE_NAME);
      }
      if (table != null) {
        context.addSymbol(table, RESymbolTypeEnum.TABLE_NAME);
        context.addLink(RESymbolLinkTypeEnum.ATTRIBUTE_OF, column, table);
        if (database != null) {
          context.addLink(RESymbolLinkTypeEnum.TABLE_OF_DATABASE, table, database);
        }
      }
    } finally {
      context.leaveScope();
    }
  }

  // TODO(zhoujiagen) implement this!!!
  public void interpreter(REInterpreterContext context, FunctionCall functionCall, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(functionCall != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("FunctionCall", postfix);
    try {

      if (functionCall instanceof SpecificFunction) {
        SpecificFunction specificFunction = (SpecificFunction) functionCall;
        throw REInterpreterError.make(specificFunction);
      }

      else if (functionCall instanceof AggregateWindowedFunction) {
        AggregateWindowedFunction aggregateWindowedFunction =
            (AggregateWindowedFunction) functionCall;
        this.interpreter(context, aggregateWindowedFunction, postfix);
      }

      else if (functionCall instanceof ScalarFunctionCall) {
        ScalarFunctionCall scalarFunctionCall = (ScalarFunctionCall) functionCall;
        throw REInterpreterError.make(scalarFunctionCall);
      }

      else if (functionCall instanceof UdfFunctionCall) {
        UdfFunctionCall udfFunctionCall = (UdfFunctionCall) functionCall;
        throw REInterpreterError.make(udfFunctionCall);
      }

      else if (functionCall instanceof PasswordFunctionClause) {
        PasswordFunctionClause passwordFunctionClause = (PasswordFunctionClause) functionCall;
        throw REInterpreterError.make(passwordFunctionClause);
      }

      else {
        throw REInterpreterError.make(functionCall);
      }

    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context,
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
        context.addSymbol(type.literal(), RESymbolTypeEnum.FUNCTION_NAME);
        final AggregateWindowedFunction.AggregatorEnum aggregator =
            aggregateWindowedFunction.aggregator;
        if (aggregator != null) {
          context.addSymbol(aggregator.literal(), RESymbolTypeEnum.SPEFICIER_AGG);
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

  public void interpreter(REInterpreterContext context, FunctionArg functionArg, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(functionArg != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("FunctionArg", postfix);
    try {

      FunctionArg.Type type = functionArg.type;
      switch (type) {
      case CONSTANT:
        context.addSymbol(functionArg.literal(), RESymbolTypeEnum.CONSTANT);
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
        throw REInterpreterError.make(functionArg);
      }

    } finally {
      context.leaveScope();
    }
  }

}
