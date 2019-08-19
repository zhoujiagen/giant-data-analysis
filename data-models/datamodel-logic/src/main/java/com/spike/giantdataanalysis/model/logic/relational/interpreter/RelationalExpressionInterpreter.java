package com.spike.giantdataanalysis.model.logic.relational.interpreter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RelationalSymbolTable.Scope;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalOperation;
import com.spike.giantdataanalysis.model.logic.relational.model.RelationalUtils;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalModelFactory;
import com.spike.giantdataanalysis.model.logic.relational.model.core.RelationalRelation;

/**
 * Interpreter: Expression => Model.
 */
public final class RelationalExpressionInterpreter {

  // TODO(zhoujiagen) 一些考虑
  // (1) catalog provider
  // (2) IR真心的不好搞啊!!!

  final DummyRelationalCataloger cataloger = new DummyRelationalCataloger();

  public static class InterpreterContext {

    /** 符号表. */
    public final RelationalSymbolTable symbolTable = new RelationalSymbolTable();

    /** 当前所属作用域. */
    public RelationalSymbolTable.Scope currentScope =
        new RelationalSymbolTable.Scope(RelationalSymbolTable.Scope.ROOT);

    /** 临时关系字典. */
    public final Map<String, RelationalRelation> temporaryRelationMap = Maps.newHashMap();

    public void addTemporaryRelation(String name, RelationalRelation relation) {
      temporaryRelationMap.put(name, relation);
    }

  }

  public RelationalOperation interpreter(InterpreterContext context,
      SelectStatement selectStatement) {
    Preconditions.checkArgument(selectStatement != null);

    if (selectStatement instanceof SimpleSelect) {
      SimpleSelect simpleSelect = (SimpleSelect) selectStatement;
      return this.interpreter(context, simpleSelect);
    } else if (selectStatement instanceof ParenthesisSelect) {
      ParenthesisSelect parenthesisSelect = (ParenthesisSelect) selectStatement;
      throw RelationalExpressionInterpreterError.make(parenthesisSelect);
    } else if (selectStatement instanceof UnionSelect) {
      UnionSelect unionSelect = (UnionSelect) selectStatement;
      throw RelationalExpressionInterpreterError.make(unionSelect);
    } else if (selectStatement instanceof UnionParenthesisSelect) {
      UnionParenthesisSelect unionParenthesisSelect = (UnionParenthesisSelect) selectStatement;
      throw RelationalExpressionInterpreterError.make(unionParenthesisSelect);
    } else {
      throw RelationalExpressionInterpreterError.make(selectStatement);
    }
  }

  public RelationalOperation interpreter(InterpreterContext context, SimpleSelect simpleSelect) {
    RelationalOperation result = null;

    // SimpleSelect <
    Preconditions.checkArgument(simpleSelect != null);
    final QuerySpecification querySpecification = simpleSelect.querySpecification;
    final LockClauseEnum lockClause = simpleSelect.lockClause;

    /// QuerySpecification <
    final List<SelectSpecEnum> selectSpecs = querySpecification.selectSpecs;
    boolean all = false;
    boolean distinct = false;
    boolean distinctrow = false;
    for (SelectSpecEnum selectSpecEnum : selectSpecs) {
      switch (selectSpecEnum) {
      case ALL:
        all = true;
        break;
      case DISTINCT:
        distinct = true;
        break;
      case DISTINCTROW:
        distinctrow = true;
        break;
      default:
        break;
      }
    }
    //// SelectElements <
    List<String> projectSymbols = Lists.newArrayList(); // 投影字段
    final SelectElements selectElements = querySpecification.selectElements;
    if (Boolean.TRUE.equals(selectElements.star)) {
      context.addSymbol(selectElements.star()); // TODO * of what
      projectSymbols.add(selectElements.star());
    }

    final List<SelectElement> selectElementList = selectElements.selectElements;
    for (SelectElement selectElement : selectElementList) {
      if (selectElement instanceof SelectStarElement) {
        SelectStarElement selectStarElement = (SelectStarElement) selectElement;
        final FullId fullId = selectStarElement.fullId;
        String firstRawLiteral = fullId.firstRawLiteral();
        String secondRawLiteral = fullId.secondRawLiteral();
        if (secondRawLiteral == null) {
          context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.DATABASE_NAME, firstRawLiteral);
          context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.TABLE_NAME, secondRawLiteral);
        } else {
          context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.TABLE_NAME, firstRawLiteral);
        }
        projectSymbols.add(selectStarElement.literal());
      }

      else if (selectElement instanceof SelectColumnElement) {
        SelectColumnElement selectColumnElement = (SelectColumnElement) selectElement;
        final FullColumnName fullColumnName = selectColumnElement.fullColumnName;
        String column = fullColumnName.triple3();
        String database = fullColumnName.triple1();
        String table = fullColumnName.triple2();
        context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.ATTRIBUTE_NAME, column);
        if (database != null) {
          context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.DATABASE_NAME, database);
        }
        if (table != null) {
          context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.TABLE_NAME, table);
          context.addLink(RelationalSymbolTable.SymbolLinkTypeEnum.ATTRIBUTE_OF, column, table);
          if (database != null) {
            context.addLink(RelationalSymbolTable.SymbolLinkTypeEnum.TABLE_OF_DATABASE, table,
              database);
          }
        }
        final Uid uid = selectColumnElement.uid;
        if (uid != null) {
          context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.ATTRIBUTE_NAME, uid.rawLiteral());
          context.addLink(RelationalSymbolTable.SymbolLinkTypeEnum.ALIAS_OF, uid.rawLiteral(),
            column);
          projectSymbols.add(uid.rawLiteral());
        } else {
          projectSymbols.add(column);
        }
      }

      else if (selectElement instanceof SelectFunctionElement) {
        SelectFunctionElement selectFunctionElement = (SelectFunctionElement) selectElement;
        throw RelationalExpressionInterpreterError.make(selectFunctionElement);
      }

      else if (selectElement instanceof SelectExpressionElement) {
        SelectExpressionElement selectExpressionElement = (SelectExpressionElement) selectElement;
        throw RelationalExpressionInterpreterError.make(selectExpressionElement);
      }
    }
    //// SelectElements >

    //// SelectIntoExpression >
    final SelectIntoExpression selectIntoExpression = querySpecification.selectIntoExpression;
    if (selectIntoExpression != null) {
      throw RelationalExpressionInterpreterError.make(selectIntoExpression);
    }
    //// SelectIntoExpression <
    int columnSize = projectSymbols.size();
    RelationalRelation relation = RelationalModelFactory.makeRelation(
      RelationalUtils.temporaryRelationName(""), RelationalModelFactory.makeAttributes(columnSize));
    result = RelationalModelFactory.makeSelect(relation, null);

    //// FromClause >
    final FromClause fromClause = querySpecification.fromClause;
    final TableSources tableSources = fromClause.tableSources;
    this.interpreter(context, tableSources);
    final Expression whereExpr = fromClause.whereExpr;
    // FIXME(zhoujiagen) what's the type of attributes
    result = RelationalModelFactory.makeSelect(relation, whereExpr);

    final List<GroupByItem> groupByItems = fromClause.groupByItems;
    final Boolean withRollup = fromClause.withRollup;
    final Expression havingExpr = fromClause.havingExpr;
    //// FromClause <

    final OrderByClause orderByClause = querySpecification.orderByClause;
    final LimitClause limitClause = querySpecification.limitClause;
    /// QuerySpecification >

    // SimpleSelect >

    return result;
  }

  public void interpreter(InterpreterContext context, TableSources tableSources) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(tableSources != null);

    List<TableSource> tableSourceList = tableSources.tableSources;
    for (TableSource tableSource : tableSourceList) {
      this.interpreter(context, tableSource);
    }
  }

  public void interpreter(InterpreterContext context, TableSource tableSource) {
    Preconditions.checkArgument(context != null);
    Preconditions.checkArgument(tableSource != null);

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
      throw RelationalExpressionInterpreterError.make(tableSource);
    }

    // TableSourceItem >
    if (tableSourceItem instanceof AtomTableItem) {
      AtomTableItem atomTableItem = (AtomTableItem) tableSourceItem;
      final TableName tableName = atomTableItem.tableName;
      final FullId fullId = tableName.fullId;
      String firstRawLiteral = fullId.firstRawLiteral();
      String secondRawLiteral = fullId.secondRawLiteral();
      String tableNameText = null;
      if (secondRawLiteral != null) {
        context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.DATABASE_NAME, firstRawLiteral);
        tableNameText = secondRawLiteral;
        context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.TABLE_NAME, secondRawLiteral);
      } else {
        tableNameText = firstRawLiteral;
        context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.TABLE_NAME, firstRawLiteral);
      }

      final Uid alias = atomTableItem.alias;
      if (alias != null) {
        context.addLink(RelationalSymbolTable.SymbolLinkTypeEnum.ALIAS_OF, alias.rawLiteral(),
          tableNameText);
      }
    }

    else if (tableSourceItem instanceof SubqueryTableItem) {
      SubqueryTableItem subqueryTableItem = (SubqueryTableItem) tableSourceItem;
      final SelectStatement selectStatement = subqueryTableItem.selectStatement;
      final Uid alias = subqueryTableItem.alias;
      RelationalOperation relationalOperation = this.interpreter(context, selectStatement);
      // 临时关系
      RelationalRelation relation = relationalOperation.result(alias.rawLiteral());
      context.addSymbol(RelationalSymbolTable.SymbolTypeEnum.TABLE_NAME, alias.rawLiteral());
      context.addTemporaryRelation(alias.rawLiteral(), relation);
    }

    else if (tableSourceItem instanceof TableSourcesItem) {
      TableSourcesItem tableSourcesItem = (TableSourcesItem) tableSourceItem;
      final TableSources tableSources = tableSourcesItem.tableSources;
      this.interpreter(context, tableSources);
    }

    else {
      throw RelationalExpressionInterpreterError.make(tableSourceItem);
    }
    // TableSourceItem <

    if (CollectionUtils.isNotEmpty(joinParts)) {
      // JoinPart >
      for (JoinPart joinPart : joinParts) {
        if (joinPart instanceof InnerJoin) {
          InnerJoin innerJoin = (InnerJoin) joinPart;
          throw RelationalExpressionInterpreterError.make(joinPart);
        } else if (joinPart instanceof StraightJoin) {
          StraightJoin straightJoin = (StraightJoin) joinPart;
          throw RelationalExpressionInterpreterError.make(joinPart);
        } else if (joinPart instanceof OuterJoin) {
          OuterJoin outerJoin = (OuterJoin) joinPart;
          throw RelationalExpressionInterpreterError.make(joinPart);
        } else if (joinPart instanceof NaturalJoin) {
          NaturalJoin naturalJoin = (NaturalJoin) joinPart;
          throw RelationalExpressionInterpreterError.make(joinPart);
        } else {
          throw RelationalExpressionInterpreterError.make(joinPart);
        }
      }
      // JoinPart <
    }

  }
}
