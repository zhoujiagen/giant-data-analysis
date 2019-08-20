package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.LockClauseEnum;
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
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.QuerySpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectElements;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectExpressionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectFunctionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectIntoExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectSpecEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SelectStarElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.SimpleSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.UnionSelect;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RelationalCataloger.DummyRelationalCataloger;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * Interpreter: Expression => Model.
 * <p>
 * FIXME(zhoujiagen) 在上层作用域中校正下层作用域中解析出的符号和链接.
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
    RelationalOperation result = null;

    if (selectStatement instanceof SimpleSelect) {
      SimpleSelect simpleSelect = (SimpleSelect) selectStatement;
      result = this.interpreter(context, simpleSelect, postfix);
    } else if (selectStatement instanceof ParenthesisSelect) {
      ParenthesisSelect parenthesisSelect = (ParenthesisSelect) selectStatement;
      throw REInterpreterError.make(parenthesisSelect);
    } else if (selectStatement instanceof UnionSelect) {
      UnionSelect unionSelect = (UnionSelect) selectStatement;
      throw REInterpreterError.make(unionSelect);
    } else if (selectStatement instanceof UnionParenthesisSelect) {
      UnionParenthesisSelect unionParenthesisSelect = (UnionParenthesisSelect) selectStatement;
      throw REInterpreterError.make(unionParenthesisSelect);
    } else {
      throw REInterpreterError.make(selectStatement);
    }

    context.leaveScope();
    return result;
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
      final LockClauseEnum lockClause = simpleSelect.lockClause;
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

      final SelectElements selectElements = querySpecification.selectElements;
      this.interpreter(context, selectElements, postfix);

      context.enterScope("SelectIntoExpression", postfix);
      final SelectIntoExpression selectIntoExpression = querySpecification.selectIntoExpression;
      if (selectIntoExpression != null) {
        throw REInterpreterError.make(selectIntoExpression);
      }
      context.leaveScope(); // SelectIntoExpression

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
        throw REInterpreterError.make(joinPart);
      } else if (joinPart instanceof StraightJoin) {
        StraightJoin straightJoin = (StraightJoin) joinPart;
        throw REInterpreterError.make(joinPart);
      } else if (joinPart instanceof OuterJoin) {
        OuterJoin outerJoin = (OuterJoin) joinPart;
        throw REInterpreterError.make(joinPart);
      } else if (joinPart instanceof NaturalJoin) {
        NaturalJoin naturalJoin = (NaturalJoin) joinPart;
        throw REInterpreterError.make(joinPart);
      } else {
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
