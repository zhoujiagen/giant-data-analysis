package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.LockClauseEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.NaturalJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.DmlStatement.OrderByClause;
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
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.FromClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.GroupByItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClause;
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
 */
public final class REInterpreter {

  // TODO(zhoujiagen) 一些考虑
  // (1) catalog provider
  // (2) IR真心的不好搞啊!!!

  final DummyRelationalCataloger cataloger = new DummyRelationalCataloger();

  public RelationalOperation interpreter(REInterpreterContext context,
      SelectStatement selectStatement) {
    Preconditions.checkArgument(selectStatement != null);

    context.enterScope("SelectStatement");
    RelationalOperation result = null;

    if (selectStatement instanceof SimpleSelect) {
      SimpleSelect simpleSelect = (SimpleSelect) selectStatement;
      result = this.interpreter(context, simpleSelect);
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

  public RelationalOperation interpreter(REInterpreterContext context, SimpleSelect simpleSelect) {
    Preconditions.checkArgument(simpleSelect != null);

    RelationalOperation result = null;
    context.enterScope("SimpleSelect");

    try {
      result = this.interpreter(context, simpleSelect.querySpecification);
      final LockClauseEnum lockClause = simpleSelect.lockClause;
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public RelationalOperation interpreter(REInterpreterContext context,
      QuerySpecification querySpecification) {
    Preconditions.checkArgument(querySpecification != null);

    RelationalOperation result = null;
    context.enterScope("QuerySpecification");

    try {
      context.enterScope("SelectSpec");
      final List<SelectSpecEnum> selectSpecs = querySpecification.selectSpecs;
      context.leaveScope();

      this.interpreter(context, querySpecification.selectElements);

      context.enterScope("SelectIntoExpression");
      final SelectIntoExpression selectIntoExpression = querySpecification.selectIntoExpression;
      if (selectIntoExpression != null) {
        throw REInterpreterError.make(selectIntoExpression);
      }
      context.leaveScope(); // SelectIntoExpression

      this.interpreter(context, querySpecification.fromClause);

      context.enterScope("OrderByClause");
      final OrderByClause orderByClause = querySpecification.orderByClause;
      context.leaveScope(); // OrderByClause

      context.enterScope("LimitClause");
      final LimitClause limitClause = querySpecification.limitClause;
      context.leaveScope(); // LimitClause
    } finally {
      context.leaveScope();
    }

    return result;
  }

  public void interpreter(REInterpreterContext context, FromClause fromClause) {
    Preconditions.checkArgument(fromClause != null);

    context.enterScope("FromClause");
    try {
      final TableSources tableSources = fromClause.tableSources;
      this.interpreter(context, tableSources);
      final Expression whereExpr = fromClause.whereExpr;

      final List<GroupByItem> groupByItems = fromClause.groupByItems;
      final Boolean withRollup = fromClause.withRollup;
      final Expression havingExpr = fromClause.havingExpr;
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, SelectElements selectElements) {
    Preconditions.checkArgument(selectElements != null);

    context.enterScope("SelectElements");
    try {
      if (Boolean.TRUE.equals(selectElements.star)) {
        context.addSymbol(REUtils.star(selectElements));
      }

      final List<SelectElement> selectElementList = selectElements.selectElements;
      for (SelectElement selectElement : selectElementList) {
        this.interpreter(context, selectElement);
      }
    } finally {
      context.leaveScope();
    }
  }

  public void interpreter(REInterpreterContext context, SelectElement selectElement) {
    Preconditions.checkArgument(selectElement != null);

    context.enterScope("SelectElement");

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
        String column = REUtils.triple3(fullColumnName);
        String database = REUtils.triple1(fullColumnName);
        String table = REUtils.triple2(fullColumnName);
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
        final Uid uid = selectColumnElement.uid;
        if (uid != null) {
          context.addSymbol(REUtils.rawLiteral(uid), RESymbolTypeEnum.ATTRIBUTE_NAME);
          context.addLink(RESymbolLinkTypeEnum.ALIAS_OF, REUtils.rawLiteral(uid), column);
        } else {
        }
      }

      else if (selectElement instanceof SelectFunctionElement) {
        SelectFunctionElement selectFunctionElement = (SelectFunctionElement) selectElement;
        throw REInterpreterError.make(selectFunctionElement);
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

  public void interpreter(REInterpreterContext context, TableSources tableSources) {
    Preconditions.checkArgument(tableSources != null);

    context.enterScope("TableSources");
    try {
      List<TableSource> tableSourceList = tableSources.tableSources;
      for (TableSource tableSource : tableSourceList) {
        this.interpreter(context, tableSource);
      }
    } finally {
      context.leaveScope();
    }

  }

  public void interpreter(REInterpreterContext context, TableSource tableSource) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(tableSource != null);

    context.enterScope("TableSource");

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

      this.interpreter(context, tableSourceItem);

      if (CollectionUtils.isNotEmpty(joinParts)) {
        for (JoinPart joinPart : joinParts) {
          this.interpreter(context, joinPart);
        }
      }
    } finally {
      context.leaveScope();
    }

  }

  public void interpreter(REInterpreterContext context, JoinPart joinPart) {
    Preconditions.checkArgument(joinPart != null);

    context.enterScope("JoinPart");

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

  public void interpreter(REInterpreterContext context, TableSourceItem tableSourceItem) {
    Preconditions.checkArgument(tableSourceItem != null);

    context.enterScope("TableSourceItem");

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
        RelationalOperation relationalOperation = this.interpreter(context, selectStatement);
        // 临时关系
        RelationalRelation relation = relationalOperation.result(REUtils.rawLiteral(alias));
        context.addSymbol(REUtils.rawLiteral(alias), RESymbolTypeEnum.TABLE_NAME);
        context.addTemporaryRelation(REUtils.rawLiteral(alias), relation);
      }

      else if (tableSourceItem instanceof TableSourcesItem) {
        TableSourcesItem tableSourcesItem = (TableSourcesItem) tableSourceItem;
        final TableSources tableSources = tableSourcesItem.tableSources;
        this.interpreter(context, tableSources);
      }

      else {
        throw REInterpreterError.make(tableSourceItem);
      }
    } finally {
      context.leaveScope();
    }
  }

}
