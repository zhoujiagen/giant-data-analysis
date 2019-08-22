package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.NaturalJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OuterJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.StraightJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.SubqueryTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSource;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourceBase;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourceItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourceNested;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSources;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.TableSourcesItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.Expression;
import com.spike.giantdataanalysis.model.logic.relational.expression.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.FromClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.GroupByItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClauseAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.ParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QueryExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QueryExpressionNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QuerySpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QuerySpecificationNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElements;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectExpressionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectFunctionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectSpecEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectStarElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SimpleSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionParenthesis;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionStatement;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RelationalCataloger.DummyRelationalCataloger;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * Interpreter: Expression => Model.
 * 
 * <pre>
 *  TODO(zhoujiagen) hack these:
 * 1 在上层作用域中校正下层作用域中解析出的符号和链接
 * 2 将AST转换为代数表达式
 * </pre>
 */
public final class REInterpreter extends REInterpreterBase {

  final DummyRelationalCataloger cataloger = new DummyRelationalCataloger();

  @Override
  public RelationalOperation interpreter(REInterpreterContext context,
      SelectStatement selectStatement, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(selectStatement != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("SelectStatement", postfix);
    try {
      if (selectStatement instanceof SimpleSelect) {
        SimpleSelect simpleSelect = (SimpleSelect) selectStatement;
        return this.interpreter(context, simpleSelect, postfix);
      } else if (selectStatement instanceof ParenthesisSelect) {
        ParenthesisSelect parenthesisSelect = (ParenthesisSelect) selectStatement;
        return this.interpreter(context, parenthesisSelect, postfix);
      } else if (selectStatement instanceof UnionSelect) {
        UnionSelect unionSelect = (UnionSelect) selectStatement;
        return this.interpreter(context, unionSelect, postfix);
      } else if (selectStatement instanceof UnionParenthesisSelect) {
        UnionParenthesisSelect unionParenthesisSelect = (UnionParenthesisSelect) selectStatement;
        return this.interpreter(context, unionParenthesisSelect, postfix);
      } else {
        throw REInterpreterError.make(selectStatement);
      }
    } finally {
      context.leaveScope();
    }
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      ParenthesisSelect parenthesisSelect, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(parenthesisSelect != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperation result = null;
    context.enterScope("ParenthesisSelect", postfix);
    try {
      final QueryExpression queryExpression = parenthesisSelect.queryExpression;
      result = this.interpreter(context, queryExpression, postfix);
      final DmlStatement.LockClauseEnum lockClause = parenthesisSelect.lockClause;
      if (lockClause != null) {
        context.addSymbol(lockClause.literal(), RESymbolTypeEnum.OPERATOR_NAME);
      }
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      QueryExpression queryExpression, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(queryExpression != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperation result = null;
    context.enterScope("QueryExpression", postfix);
    try {
      final QuerySpecification querySpecification = queryExpression.querySpecification;
      result = this.interpreter(context, querySpecification, postfix);
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      QueryExpressionNointo queryExpressionNointo, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(queryExpressionNointo != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("QueryExpressionNointo", postfix);
    try {
      final QuerySpecificationNointo querySpecificationNointo =
          queryExpressionNointo.querySpecificationNointo;
      return this.interpreter(context, querySpecificationNointo, postfix);
    } finally {
      context.leaveScope();
    }
  }

  // TODO(zhoujiagen) hack this: 多个并操作符
  public RelationalOperation interpreter(REInterpreterContext context, UnionSelect unionSelect,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(unionSelect != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperation result = null;
    context.enterScope("UnionSelect", postfix);
    try {
      final QuerySpecificationNointo querySpecificationNointo =
          unionSelect.querySpecificationNointo;
      this.interpreter(context, querySpecificationNointo, postfix);
      final List<UnionStatement> unionStatements = unionSelect.unionStatements;
      int unionStatementIndex = 1;
      for (UnionStatement unionStatement : unionStatements) {
        this.interpreter(context, unionStatement, postfix + (unionStatementIndex++));
      }

      final SelectStatement.UnionTypeEnum unionType = unionSelect.unionType;
      final QuerySpecification querySpecification = unionSelect.querySpecification;
      final QueryExpression queryExpression = unionSelect.queryExpression;
      if (querySpecification != null || queryExpression != null) {
        context.addSymbol("UNION", RESymbolTypeEnum.OPERATOR_NAME);
        if (unionType != null) {
          context.addLink(RESymbolLinkTypeEnum.SPECIFIER, "UNION", unionType.literal());
        }
      }

      final OrderByClause orderByClause = unionSelect.orderByClause;
      if (orderByClause != null) {
        this.interpreter(context, orderByClause, postfix);
      }
      final LimitClause limitClause = unionSelect.limitClause;
      if (limitClause != null) {
        this.interpreter(context, limitClause, postfix);
      }
      final DmlStatement.LockClauseEnum lockClause = unionSelect.lockClause;
      if (lockClause != null) {
        context.addSymbol(lockClause.literal(), RESymbolTypeEnum.OPERATOR_NAME);
      }
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      UnionParenthesis unionParenthesis, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(unionParenthesis != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("UnionParenthesis", postfix);
    try {
      context.addSymbol("UNION", RESymbolTypeEnum.OPERATOR_NAME);
      final SelectStatement.UnionTypeEnum unionType = unionParenthesis.unionType;
      context.addLink(RESymbolLinkTypeEnum.SPECIFIER, "UNION", unionType.literal());

      final QueryExpressionNointo queryExpressionNointo = unionParenthesis.queryExpressionNointo;
      return this.interpreter(context, queryExpressionNointo, postfix);
    } finally {
      context.leaveScope();
    }
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      UnionStatement unionStatement, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(unionStatement != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("UnionStatement", postfix);
    try {
      context.addSymbol("UNION", RESymbolTypeEnum.OPERATOR_NAME);
      final SelectStatement.UnionTypeEnum unionType = unionStatement.unionType;
      context.addLink(RESymbolLinkTypeEnum.SPECIFIER, "UNION", unionType.literal());
      final QuerySpecificationNointo querySpecificationNointo =
          unionStatement.querySpecificationNointo;
      final QueryExpressionNointo queryExpressionNointo = unionStatement.queryExpressionNointo;
      if (querySpecificationNointo != null) {
        return this.interpreter(context, querySpecificationNointo, postfix);
      } else {
        return this.interpreter(context, queryExpressionNointo, postfix);
      }
    } finally {
      context.leaveScope();
    }
  }

  /**
   * @param context
   * @param querySpecificationNointo
   * @param postfix
   * @return
   * @see #interpreter(REInterpreterContext, QuerySpecification, String)
   */
  public RelationalOperation interpreter(REInterpreterContext context,
      QuerySpecificationNointo querySpecificationNointo, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(querySpecificationNointo != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperation result = null;
    context.enterScope("QuerySpecification", postfix);

    try {
      final List<SelectSpecEnum> selectSpecs = querySpecificationNointo.selectSpecs;
      if (CollectionUtils.isNotEmpty(selectSpecs)) {
        for (SelectSpecEnum selectSpec : selectSpecs) {
          context.addSymbol(selectSpec.literal(), RESymbolTypeEnum.SPECIFIER_SELECT);
        }
      }

      final SelectElements selectElements = querySpecificationNointo.selectElements;
      this.interpreter(context, selectElements, postfix);

      final FromClause fromClause = querySpecificationNointo.fromClause;
      if (fromClause != null) {
        this.interpreter(context, fromClause, postfix);
      }

      final OrderByClause orderByClause = querySpecificationNointo.orderByClause;
      if (orderByClause != null) {
        this.interpreter(context, orderByClause, postfix);
      }

      final LimitClause limitClause = querySpecificationNointo.limitClause;
      if (limitClause != null) {
        this.interpreter(context, limitClause, postfix);
      }
    } finally {
      context.leaveScope();
    }

    return result;
  }

  // TODO(zhoujiagen) see UnionSelect
  public RelationalOperation interpreter(REInterpreterContext context,
      UnionParenthesisSelect unionParenthesisSelect, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(unionParenthesisSelect != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("UnionParenthesisSelect", postfix);
    try {
      final QueryExpressionNointo queryExpressionNointo =
          unionParenthesisSelect.queryExpressionNointo;
      this.interpreter(context, queryExpressionNointo, postfix);
      final List<UnionParenthesis> unionParenthesisList =
          unionParenthesisSelect.unionParenthesisList;
      for (UnionParenthesis unionParenthesis : unionParenthesisList) {
        this.interpreter(context, unionParenthesis, postfix);
      }
      final SelectStatement.UnionTypeEnum unionType = unionParenthesisSelect.unionType;
      final QueryExpression queryExpression = unionParenthesisSelect.queryExpression;
      if (queryExpression != null) {
        context.addSymbol("UNION", RESymbolTypeEnum.OPERATOR_NAME);
        if (unionType != null) {
          context.addLink(RESymbolLinkTypeEnum.SPECIFIER, "UNION", unionType.literal());
        }
      }
      final OrderByClause orderByClause = unionParenthesisSelect.orderByClause;
      if (orderByClause != null) {
        this.interpreter(context, orderByClause, postfix);
      }
      final LimitClause limitClause = unionParenthesisSelect.limitClause;
      if (limitClause != null) {
        this.interpreter(context, limitClause, postfix);
      }
      final DmlStatement.LockClauseEnum lockClause = unionParenthesisSelect.lockClause;
      if (lockClause != null) {
        context.addSymbol(lockClause.literal(), RESymbolTypeEnum.OPERATOR_NAME);
      }
    } finally {
      context.leaveScope();
    }

    throw REInterpreterError.make(unionParenthesisSelect);
  }

  public RelationalOperation interpreter(REInterpreterContext context, SimpleSelect simpleSelect,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(simpleSelect != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperation result = null;
    context.enterScope("SimpleSelect", postfix);

    try {
      result = this.interpreter(context, simpleSelect.querySpecification, postfix);
      final DmlStatement.LockClauseEnum lockClause = simpleSelect.lockClause;
      if (lockClause != null) {
        context.addSymbol(lockClause.literal(), RESymbolTypeEnum.OPERATOR_NAME);
      }
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      QuerySpecification querySpecification, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(querySpecification != null);
    Preconditions.checkArgument(postfix != null);

    RelationalOperation result = null;
    context.enterScope("QuerySpecification", postfix);

    try {
      final List<SelectSpecEnum> selectSpecs = querySpecification.selectSpecs;
      if (CollectionUtils.isNotEmpty(selectSpecs)) {
        for (SelectSpecEnum selectSpec : selectSpecs) {
          context.addSymbol(selectSpec.literal(), RESymbolTypeEnum.SPECIFIER_SELECT);
        }
      }
      final SelectElements selectElements = querySpecification.selectElements;
      this.interpreter(context, selectElements, postfix);

      context.enterScope("SelectIntoExpression", postfix);
      final SelectIntoExpression selectIntoExpression = querySpecification.selectIntoExpression;
      if (selectIntoExpression != null) {
        throw REInterpreterError.make(selectIntoExpression);
      }
      context.leaveScope(); // SelectIntoExpression

      result = RelationalModelFactory.makeSelect(RelationalRelation.DUAL, null, context.currentScope);
      
      final FromClause fromClause = querySpecification.fromClause;
      if (fromClause != null) {
        this.interpreter(context, fromClause, postfix);
      }

      final OrderByClause orderByClause = querySpecification.orderByClause;
      if (orderByClause != null) {
        this.interpreter(context, orderByClause, postfix);
      }

      final LimitClause limitClause = querySpecification.limitClause;
      if (limitClause != null) {
        this.interpreter(context, limitClause, postfix);
      }
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public void interpreter(REInterpreterContext context, LimitClause limitClause, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(limitClause != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("LimitClause", postfix);
    try {
      final LimitClauseAtom limit = limitClause.limit;
      final LimitClauseAtom offset = limitClause.offset;
      if (offset != null) {
        this.interpreter(context, offset, postfix + "1");
        this.interpreter(context, limit, postfix + "2");
      } else {
        this.interpreter(context, limit, postfix);
      }
    } finally {
      context.leaveScope();
    }

  }

  // no scope change
  public void interpreter(REInterpreterContext context, LimitClauseAtom limitClauseAtom,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(limitClauseAtom != null);
    Preconditions.checkArgument(postfix != null);

    final DecimalLiteral decimalLiteral = limitClauseAtom.decimalLiteral;
    final MysqlVariable mysqlVariable = limitClauseAtom.mysqlVariable;
    if (decimalLiteral != null) {
      this.interpreter(context, decimalLiteral, postfix);
    } else {
      this.interpreter(context, mysqlVariable, postfix);
    }
  }

  // no scope change
  public void interpreter(REInterpreterContext context, DecimalLiteral decimalLiteral,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(decimalLiteral != null);
    Preconditions.checkArgument(postfix != null);

    context.addSymbol(decimalLiteral.literal(), RESymbolTypeEnum.CONSTANT);
    context.addLink(RESymbolLinkTypeEnum.TYPE, decimalLiteral.literal(),
      Constant.Type.DECIMAL_LITERAL.literal());
  }

  public void interpreter(REInterpreterContext context, OrderByClause orderByClause,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(orderByClause != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("OrderByClause", postfix);
    try {
      List<OrderByExpression> orderByExpressions = orderByClause.orderByExpressions;
      for (OrderByExpression orderByExpression : orderByExpressions) {
        this.interpreter(context, orderByExpression, postfix);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, OrderByExpression orderByExpression,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(orderByExpression != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("OrderByExpression", postfix);
    try {
      Expression expression = orderByExpression.expression;
      this.interpreter(context, expression, postfix);
      OrderByExpression.OrderType order = orderByExpression.order;
      if (order != null) {
        context.addSymbol(order.literal(), RESymbolTypeEnum.SPECIFIER_ORDER);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, FromClause fromClause, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(fromClause != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("FromClause", postfix);
    try {
      final TableSources tableSources = fromClause.tableSources;
      this.interpreter(context, tableSources, postfix);
      final Expression whereExpr = fromClause.whereExpr;
      if (whereExpr != null) {
        this.interpreter(context, whereExpr, postfix + "1");
      }

      final List<GroupByItem> groupByItems = fromClause.groupByItems;
      if (CollectionUtils.isNotEmpty(groupByItems)) {
        for (GroupByItem groupByItem : groupByItems) {
          this.interpreter(context, groupByItem, postfix);
        }
      }

      final Boolean withRollup = fromClause.withRollup;
      if (Boolean.TRUE.equals(withRollup)) {
        context.addSymbol("WITH ROLLUP", RESymbolTypeEnum.SPEFICIER_GROUP_BY);
      }
      final Expression havingExpr = fromClause.havingExpr;
      if (havingExpr != null) {
        this.interpreter(context, havingExpr, postfix + "2");
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, GroupByItem groupByItem, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(groupByItem != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("GroupByItem", postfix);
    try {
      Expression expression = groupByItem.expression;
      this.interpreter(context, expression, postfix);
      GroupByItem.OrderType orderType = groupByItem.order;
      if (orderType != null) {
        context.addSymbol(orderType.literal(), RESymbolTypeEnum.SPECIFIER_ORDER);
      }
    } finally {
      context.leaveScope();
    }

  }

  public void interpreter(REInterpreterContext context, SelectElements selectElements,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(selectElements != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("SelectElements", postfix);
    try {
      if (Boolean.TRUE.equals(selectElements.star)) {
        context.addSymbol(REUtils.star(selectElements), RESymbolTypeEnum.ALL_ATTRIBUTE_NAME);
      }

      final List<SelectElement> selectElementList = selectElements.selectElements;
      for (SelectElement selectElement : selectElementList) {
        this.interpreter(context, selectElement, postfix);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, SelectElement selectElement,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(selectElement != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("SelectElement", postfix);
    try {
      if (selectElement instanceof SelectStarElement) {
        SelectStarElement selectStarElement = (SelectStarElement) selectElement;
        final FullId fullId = selectStarElement.fullId;
        String firstRawLiteral = REUtils.firstRawLiteral(fullId);
        String secondRawLiteral = REUtils.secondRawLiteral(fullId);
        if (secondRawLiteral == null) {
          context.addSymbol(firstRawLiteral, RESymbolTypeEnum.DATABASE_NAME);
          context.addSymbol(secondRawLiteral, RESymbolTypeEnum.TABLE_NAME);
        } else {
          context.addSymbol(firstRawLiteral, RESymbolTypeEnum.TABLE_NAME);
        }
      }

      else if (selectElement instanceof SelectColumnElement) {
        SelectColumnElement selectColumnElement = (SelectColumnElement) selectElement;
        final FullColumnName fullColumnName = selectColumnElement.fullColumnName;
        this.interpreter(context, fullColumnName, postfix);
      }

      else if (selectElement instanceof SelectFunctionElement) {
        SelectFunctionElement selectFunctionElement = (SelectFunctionElement) selectElement;
        this.interpreter(context, selectFunctionElement, postfix);
      }

      else if (selectElement instanceof SelectExpressionElement) {
        SelectExpressionElement selectExpressionElement = (SelectExpressionElement) selectElement;
        throw REInterpreterError.make(selectExpressionElement);
      } else {
        throw REInterpreterError.make(selectElement);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, SelectFunctionElement selectFunctionElement,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(selectFunctionElement != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("SelectFunctionElement", postfix);
    try {
      FunctionCall functionCall = selectFunctionElement.functionCall;
      String functionCallLiteral = functionCall.literal();
      context.addSymbol(functionCallLiteral, RESymbolTypeEnum.ATTRIBUTE_NAME);
      this.interpreter(context, functionCall, postfix);

      Uid uid = selectFunctionElement.uid;
      if (uid != null) {
        String uidRawLiteral = REUtils.rawLiteral(uid);
        context.addSymbol(uidRawLiteral, RESymbolTypeEnum.ATTRIBUTE_NAME);
        context.addLink(RESymbolLinkTypeEnum.ALIAS_OF, uidRawLiteral, functionCallLiteral);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, TableSources tableSources, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(tableSources != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("TableSources", postfix);
    try {
      List<TableSource> tableSourceList = tableSources.tableSources;
      for (TableSource tableSource : tableSourceList) {
        this.interpreter(context, tableSource, postfix);
      }
    } finally {
      context.leaveScope();
    }

  }

  public void interpreter(REInterpreterContext context, TableSource tableSource, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(tableSource != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("TableSource", postfix);

    try {
      final TableSourceItem tableSourceItem;
      final List<JoinPart> joinParts;
      if (tableSource instanceof TableSourceBase) {
        TableSourceBase tableSourceBase = (TableSourceBase) tableSource;
        tableSourceItem = tableSourceBase.tableSourceItem;
        joinParts = tableSourceBase.joinParts;
      } else if (tableSource instanceof TableSourceNested) {
        TableSourceNested tableSourceNested = (TableSourceNested) tableSource;
        tableSourceItem = tableSourceNested.tableSourceItem;
        joinParts = tableSourceNested.joinParts;
      } else {
        throw REInterpreterError.make(tableSource);
      }

      this.interpreter(context, tableSourceItem, postfix);

      if (CollectionUtils.isNotEmpty(joinParts)) {
        for (JoinPart joinPart : joinParts) {
          this.interpreter(context, joinPart, postfix);
        }
      }
    } finally {
      context.leaveScope();
    }

  }

  public void interpreter(REInterpreterContext context, JoinPart joinPart, String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(joinPart != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("JoinPart", postfix);
    try {
      if (joinPart instanceof InnerJoin) {
        InnerJoin innerJoin = (InnerJoin) joinPart;
        final TableSourceItem tableSourceItem = innerJoin.tableSourceItem;
        this.interpreter(context, tableSourceItem, postfix);
        final Expression expression = innerJoin.expression;
        final UidList uidList = innerJoin.uidList;
        if (expression != null) {
          this.interpreter(context, expression, postfix);
        }
        if (uidList != null) {
          List<String> uids = REUtils.rawLiteral(uidList);
          for (String uid : uids) {
            context.addSymbol(uid, RESymbolTypeEnum.ATTRIBUTE_NAME);
          }
        }
      }

      else if (joinPart instanceof StraightJoin) {
        StraightJoin straightJoin = (StraightJoin) joinPart;
        final TableSourceItem tableSourceItem = straightJoin.tableSourceItem;
        this.interpreter(context, tableSourceItem, postfix);
        final Expression expression = straightJoin.expression;
        if (expression != null) {
          this.interpreter(context, expression, postfix);
        }
      }

      else if (joinPart instanceof OuterJoin) {
        OuterJoin outerJoin = (OuterJoin) joinPart;
        final DmlStatement.OuterJoinType type = outerJoin.type;
        context.addSymbol(type.literal(), RESymbolTypeEnum.SPEFICIER_JOIN);
        final TableSourceItem tableSourceItem = outerJoin.tableSourceItem;
        this.interpreter(context, tableSourceItem, postfix);
        final Expression expression = outerJoin.expression;
        if (expression != null) {
          this.interpreter(context, expression, postfix);
        }
        final UidList uidList = outerJoin.uidList;
        if (uidList != null) {
          List<String> uids = REUtils.rawLiteral(uidList);
          for (String uid : uids) {
            context.addSymbol(uid, RESymbolTypeEnum.ATTRIBUTE_NAME);
          }
        }
      }

      else if (joinPart instanceof NaturalJoin) {
        NaturalJoin naturalJoin = (NaturalJoin) joinPart;
        final DmlStatement.OuterJoinType outerJoinType = naturalJoin.outerJoinType;
        if (outerJoinType != null) {
          context.addSymbol(outerJoinType.literal(), RESymbolTypeEnum.SPEFICIER_JOIN);
        }
        final TableSourceItem tableSourceItem = naturalJoin.tableSourceItem;
        this.interpreter(context, tableSourceItem, postfix);
      }

      else {
        throw REInterpreterError.make(joinPart);
      }
    } finally {
      context.leaveScope();
    }

  }

  public void interpreter(REInterpreterContext context, TableSourceItem tableSourceItem,
      String postfix) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(tableSourceItem != null);
    Preconditions.checkArgument(postfix != null);

    context.enterScope("TableSourceItem", postfix);

    try {
      if (tableSourceItem instanceof AtomTableItem) {
        AtomTableItem atomTableItem = (AtomTableItem) tableSourceItem;
        final TableName tableName = atomTableItem.tableName;
        final FullId fullId = tableName.fullId;
        String firstRawLiteral = REUtils.firstRawLiteral(fullId);
        String secondRawLiteral = REUtils.secondRawLiteral(fullId);
        String tableNameText = null;
        if (secondRawLiteral != null) {
          context.addSymbol(firstRawLiteral, RESymbolTypeEnum.DATABASE_NAME);
          tableNameText = secondRawLiteral;
          context.addSymbol(secondRawLiteral, RESymbolTypeEnum.TABLE_NAME);
        } else {
          tableNameText = firstRawLiteral;
          context.addSymbol(firstRawLiteral, RESymbolTypeEnum.TABLE_NAME);
        }

        final Uid alias = atomTableItem.alias;
        if (alias != null) {
          context.addLink(RESymbolLinkTypeEnum.ALIAS_OF, REUtils.rawLiteral(alias), tableNameText);
        }
      }

      else if (tableSourceItem instanceof SubqueryTableItem) {
        SubqueryTableItem subqueryTableItem = (SubqueryTableItem) tableSourceItem;
        final SelectStatement selectStatement = subqueryTableItem.selectStatement;
        final Uid alias = subqueryTableItem.alias;
        RelationalOperation relationalOperation =
            this.interpreter(context, selectStatement, postfix);
        // 临时关系
        RelationalRelation relation = relationalOperation.result(REUtils.rawLiteral(alias));
        context.addSymbol(REUtils.rawLiteral(alias), RESymbolTypeEnum.TABLE_NAME);
        context.addTemporaryRelation(REUtils.rawLiteral(alias), relation);
      }

      else if (tableSourceItem instanceof TableSourcesItem) {
        TableSourcesItem tableSourcesItem = (TableSourcesItem) tableSourceItem;
        final TableSources tableSources = tableSourcesItem.tableSources;
        this.interpreter(context, tableSources, postfix);
      }

      else {
        throw REInterpreterError.make(tableSourceItem);
      }
    } finally {
      context.leaveScope();
    }
  }
}
