package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.ExpressionAtom.MysqlVariable;

/**
 * <pre>
selectStatement
  : querySpecification lockClause?                #simpleSelect
  | queryExpression lockClause?                   #parenthesisSelect
  | querySpecificationNointo unionStatement+
      (
        UNION unionType=(ALL | DISTINCT)?
        (querySpecification | queryExpression)
      )?
      orderByClause? limitClause? lockClause?     #unionSelect
  | queryExpressionNointo unionParenthesis+
      (
        UNION unionType=(ALL | DISTINCT)?
        queryExpression
      )?
      orderByClause? limitClause? lockClause?    #unionParenthesisSelect
  ;
 * </pre>
 */
public interface SelectStatement extends DmlStatement {

  public static class SimpleSelect implements SelectStatement {
    public final QuerySpecification querySpecification;
    public final LockClauseEnum lockClause;

    SimpleSelect(QuerySpecification querySpecification, LockClauseEnum lockClause) {
      Preconditions.checkArgument(querySpecification != null);

      this.querySpecification = querySpecification;
      this.lockClause = lockClause;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(querySpecification);
      if (lockClause != null) {
        builder.append(" ").append(lockClause);
      }
      return builder.toString();
    }
  }

  public static class ParenthesisSelect implements SelectStatement {
    public final QueryExpression queryExpression;
    public final LockClauseEnum lockClause;

    ParenthesisSelect(QueryExpression queryExpression, LockClauseEnum lockClause) {
      Preconditions.checkArgument(queryExpression != null);

      this.queryExpression = queryExpression;
      this.lockClause = lockClause;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ParenthesisSelect [queryExpression=");
      builder.append(queryExpression);
      builder.append(", lockClause=");
      builder.append(lockClause);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class UnionSelect implements SelectStatement {
    public final QuerySpecificationNointo querySpecificationNointo;
    public final List<UnionStatement> unionStatements;
    public final UnionTypeEnum unionType;
    public final QuerySpecification querySpecification;
    public final QueryExpression queryExpression;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;
    public final LockClauseEnum lockClause;

    UnionSelect(QuerySpecificationNointo querySpecificationNointo,
        List<UnionStatement> unionStatements, UnionTypeEnum unionType,
        QuerySpecification querySpecification, QueryExpression queryExpression,
        OrderByClause orderByClause, LimitClause limitClause, LockClauseEnum lockClause) {
      Preconditions.checkArgument(querySpecification != null);
      Preconditions.checkArgument(unionStatements != null && unionStatements.size() > 0);

      this.querySpecificationNointo = querySpecificationNointo;
      this.unionStatements = unionStatements;
      this.unionType = unionType;
      this.querySpecification = querySpecification;
      this.queryExpression = queryExpression;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
      this.lockClause = lockClause;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("UnionSelect [querySpecificationNointo=");
      builder.append(querySpecificationNointo);
      builder.append(", unionStatements=");
      builder.append(unionStatements);
      builder.append(", unionType=");
      builder.append(unionType);
      builder.append(", querySpecification=");
      builder.append(querySpecification);
      builder.append(", queryExpression=");
      builder.append(queryExpression);
      builder.append(", orderByClause=");
      builder.append(orderByClause);
      builder.append(", limitClause=");
      builder.append(limitClause);
      builder.append(", lockClause=");
      builder.append(lockClause);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class UnionParenthesisSelect implements SelectStatement {
    public final QueryExpressionNointo queryExpressionNointo;
    public final List<UnionParenthesis> unionParenthesisList;
    public final UnionTypeEnum unionType;
    public final QueryExpression queryExpression;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;
    public final LockClauseEnum lockClause;

    UnionParenthesisSelect(QueryExpressionNointo queryExpressionNointo,
        List<UnionParenthesis> unionParenthesisList, UnionTypeEnum unionType,
        QueryExpression queryExpression, OrderByClause orderByClause, LimitClause limitClause,
        LockClauseEnum lockClause) {
      Preconditions.checkArgument(queryExpressionNointo != null);
      Preconditions.checkArgument(unionParenthesisList != null && unionParenthesisList.size() > 0);

      this.queryExpressionNointo = queryExpressionNointo;
      this.unionParenthesisList = unionParenthesisList;
      this.unionType = unionType;
      this.queryExpression = queryExpression;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
      this.lockClause = lockClause;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("UnionParenthesisSelect [queryExpressionNointo=");
      builder.append(queryExpressionNointo);
      builder.append(", unionParenthesisList=");
      builder.append(unionParenthesisList);
      builder.append(", unionType=");
      builder.append(unionType);
      builder.append(", queryExpression=");
      builder.append(queryExpression);
      builder.append(", orderByClause=");
      builder.append(orderByClause);
      builder.append(", limitClause=");
      builder.append(limitClause);
      builder.append(", lockClause=");
      builder.append(lockClause);
      builder.append("]");
      return builder.toString();
    }

  }

  // ---------------------------------------------------------------------------
  // Helper Classes
  // ---------------------------------------------------------------------------
  public static enum UnionTypeEnum {
    ALL, DISTINCT
  }

  // unionStatement: UNION unionType=(ALL | DISTINCT)?
  // (querySpecificationNointo | queryExpressionNointo)
  public static class UnionStatement implements PrimitiveExpression {
    public final UnionTypeEnum unionType;
    public final QuerySpecificationNointo querySpecificationNointo;
    public final QueryExpressionNointo queryExpressionNointo;

    UnionStatement(UnionTypeEnum unionType, QuerySpecificationNointo querySpecificationNointo,
        QueryExpressionNointo queryExpressionNointo) {
      Preconditions.checkArgument(unionType != null);
      Preconditions.checkArgument(!(querySpecificationNointo == null//
          && queryExpressionNointo == null));

      this.unionType = unionType;
      this.querySpecificationNointo = querySpecificationNointo;
      this.queryExpressionNointo = queryExpressionNointo;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("UnionStatement [unionType=");
      builder.append(unionType);
      builder.append(", querySpecificationNointo=");
      builder.append(querySpecificationNointo);
      builder.append(", queryExpressionNointo=");
      builder.append(queryExpressionNointo);
      builder.append("]");
      return builder.toString();
    }

  }

  // unionParenthesis: UNION unionType=(ALL | DISTINCT)? queryExpressionNointo
  public static class UnionParenthesis implements PrimitiveExpression {
    public final UnionTypeEnum unionType;
    public final QueryExpressionNointo queryExpressionNointo;

    UnionParenthesis(UnionTypeEnum unionType, QueryExpressionNointo queryExpressionNointo) {
      Preconditions.checkArgument(queryExpressionNointo != null);

      this.unionType = unionType;
      this.queryExpressionNointo = queryExpressionNointo;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("UnionParenthesis [unionType=");
      builder.append(unionType);
      builder.append(", queryExpressionNointo=");
      builder.append(queryExpressionNointo);
      builder.append("]");
      return builder.toString();
    }

  }

  // querySpecificationNointo: SELECT selectSpec* selectElements
  // fromClause? orderByClause? limitClause?
  public static class QuerySpecificationNointo implements PrimitiveExpression {
    public final List<SelectSpecEnum> selectSpecs;
    public final SelectElements selectElements;
    public final FromClause fromClause;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;

    QuerySpecificationNointo(List<SelectSpecEnum> selectSpecs, SelectElements selectElements,
        FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
      Preconditions.checkArgument(selectElements != null);

      this.selectSpecs = selectSpecs;
      this.selectElements = selectElements;
      this.fromClause = fromClause;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("QuerySpecificationNointo [selectSpecs=");
      builder.append(selectSpecs);
      builder.append(", selectElements=");
      builder.append(selectElements);
      builder.append(", fromClause=");
      builder.append(fromClause);
      builder.append(", orderByClause=");
      builder.append(orderByClause);
      builder.append(", limitClause=");
      builder.append(limitClause);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  querySpecification
      : SELECT selectSpec* selectElements selectIntoExpression?
        fromClause? orderByClause? limitClause?
      | SELECT selectSpec* selectElements
      fromClause? orderByClause? limitClause? selectIntoExpression?
      ;
   * </pre>
   */
  public static class QuerySpecification implements PrimitiveExpression {
    public final List<SelectSpecEnum> selectSpecs;
    public final SelectElements selectElements;
    public final SelectIntoExpression selectIntoExpression;
    public final FromClause fromClause;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;

    QuerySpecification(List<SelectSpecEnum> selectSpecs, SelectElements selectElements,
        SelectIntoExpression selectIntoExpression, //
        FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
      Preconditions.checkArgument(selectElements != null);

      this.selectSpecs = selectSpecs;
      this.selectElements = selectElements;
      this.selectIntoExpression = selectIntoExpression;
      this.fromClause = fromClause;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SELECT").append(" ");
      if (CollectionUtils.isNotEmpty(selectSpecs)) {
        builder.append(Joiner.on(" ").join(selectSpecs));
      }

      builder.append(selectElements);

      if (selectIntoExpression != null) {
        builder.append(System.lineSeparator());
        builder.append(selectIntoExpression);
      }

      if (fromClause != null) {
        builder.append(System.lineSeparator());
        builder.append(fromClause);
      }

      if (orderByClause != null) {
        builder.append(System.lineSeparator());
        builder.append(orderByClause);
      }

      if (limitClause != null) {
        builder.append(System.lineSeparator());
        builder.append(limitClause);
      }
      return builder.toString();
    }

  }

  // queryExpressionNointo : '(' querySpecificationNointo ')'| '(' queryExpressionNointo ')'
  public static class QueryExpressionNointo implements PrimitiveExpression {
    public final QuerySpecificationNointo querySpecificationNointo;

    public QueryExpressionNointo(QuerySpecificationNointo querySpecificationNointo) {
      Preconditions.checkArgument(querySpecificationNointo != null);

      this.querySpecificationNointo = querySpecificationNointo;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("QueryExpressionNointo [querySpecificationNointo=");
      builder.append(querySpecificationNointo);
      builder.append("]");
      return builder.toString();
    }

  }

  public static enum SelectSpecEnum {
    ALL, DISTINCT, DISTINCTROW, // only one
    HIGH_PRIORITY, STRAIGHT_JOIN, SQL_SMALL_RESULT, SQL_BIG_RESULT, SQL_BUFFER_RESULT, //
    SQL_CACHE, SQL_NO_CACHE, // only one
    SQL_CALC_FOUND_ROWS;
  }

  /**
   * <pre>
  fromClause
    : FROM tableSources
      (WHERE whereExpr=expression)?
      (
        GROUP BY
        groupByItem (',' groupByItem)*
        (WITH ROLLUP)?
      )?
      (HAVING havingExpr=expression)?
    ;
   * </pre>
   */
  public static class FromClause implements PrimitiveExpression {
    public final TableSources tableSources;
    public final Expression whereExpr;
    public final List<GroupByItem> groupByItems;
    public final Boolean withRollup;
    public final Expression havingExpr;

    FromClause(TableSources tableSources, Expression whereExpr, List<GroupByItem> groupByItems,
        Boolean withRollup, Expression havingExpr) {
      Preconditions.checkArgument(tableSources != null);

      this.tableSources = tableSources;
      this.whereExpr = whereExpr;
      this.groupByItems = groupByItems;
      this.withRollup = withRollup;
      this.havingExpr = havingExpr;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("FROM ");
      builder.append(tableSources);
      if (whereExpr != null) {
        builder.append(System.lineSeparator());
        builder.append("WHERE ").append(whereExpr);
      }
      if (CollectionUtils.isNotEmpty(groupByItems)) {
        builder.append(System.lineSeparator());
        builder.append("GROUP BY ");
        builder.append(Joiner.on(", ").join(groupByItems));
      }
      if (Boolean.TRUE.equals(withRollup)) {
        builder.append(System.lineSeparator());
        builder.append(" WITH ROLLUP ");
      }
      if (havingExpr != null) {
        builder.append(System.lineSeparator());
        builder.append(havingExpr);
      }
      return builder.toString();
    }
  }

  // groupByItem : expression order=(ASC | DESC)?
  public static class GroupByItem implements PrimitiveExpression {
    public static enum OrderType {
      ASC, DESC
    }

    public final Expression expression;
    public final OrderType order;

    GroupByItem(Expression expression, OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("GroupByItem [expression=");
      builder.append(expression);
      builder.append(", order=");
      builder.append(order);
      builder.append("]");
      return builder.toString();
    }

  }

  // orderByClause: ORDER BY orderByExpression (',' orderByExpression)*
  public static class OrderByClause implements PrimitiveExpression {
    public final List<OrderByExpression> orderByExpressions;

    OrderByClause(List<OrderByExpression> orderByExpressions) {
      Preconditions.checkArgument(orderByExpressions != null && orderByExpressions.size() > 0);

      this.orderByExpressions = orderByExpressions;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("OrderByClause [orderByExpressions=");
      builder.append(orderByExpressions);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   limitClause
    : LIMIT
    (
      (offset=limitClauseAtom ',')? limit=limitClauseAtom
      | limit=limitClauseAtom OFFSET offset=limitClauseAtom
    )
    ;
   * </pre>
   */
  public static class LimitClause implements PrimitiveExpression {
    public final LimitClauseAtom limit;
    public final LimitClauseAtom offset;

    LimitClause(LimitClauseAtom limit, LimitClauseAtom offset) {
      Preconditions.checkArgument(limit != null);

      this.limit = limit;
      this.offset = offset;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LimitClause [limit=");
      builder.append(limit);
      builder.append(", offset=");
      builder.append(offset);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class LimitClauseAtom implements PrimitiveExpression {
    public final DecimalLiteral decimalLiteral;
    public final MysqlVariable mysqlVariable;

    LimitClauseAtom(DecimalLiteral decimalLiteral, MysqlVariable mysqlVariable) {
      Preconditions.checkArgument(!(decimalLiteral == null && mysqlVariable == null));

      this.decimalLiteral = decimalLiteral;
      this.mysqlVariable = mysqlVariable;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("LimitClauseAtom [decimalLiteral=");
      builder.append(decimalLiteral);
      builder.append(", mysqlVariable=");
      builder.append(mysqlVariable);
      builder.append("]");
      return builder.toString();
    }

  }

  // tableSources: tableSource (',' tableSource)*
  public static class TableSources implements PrimitiveExpression {
    public final List<TableSource> tableSources;

    TableSources(List<TableSource> tableSources) {
      Preconditions.checkArgument(tableSources != null && tableSources.size() > 0);

      this.tableSources = tableSources;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(Joiner.on(", ").join(tableSources));
      return builder.toString();
    }

  }

  /**
   * <pre>
   tableSource
    : tableSourceItem joinPart*           #tableSourceBase
    | '(' tableSourceItem joinPart* ')'   #tableSourceNested
    ;
   * </pre>
   */
  public static interface TableSource extends PrimitiveExpression {
  }

  public static class TableSourceBase implements TableSource {
    public final TableSourceItem tableSourceItem;
    public final List<JoinPart> joinParts;

    TableSourceBase(TableSourceItem tableSourceItem, List<JoinPart> joinParts) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.joinParts = joinParts;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(tableSourceItem);
      if (CollectionUtils.isNotEmpty(joinParts)) {
        builder.append(" ");
        builder.append(Joiner.on(" ").join(joinParts));
      }
      return builder.toString();
    }
  }

  public static class TableSourceNested implements TableSource {
    public final TableSourceItem tableSourceItem;
    public final List<JoinPart> joinParts;

    TableSourceNested(TableSourceItem tableSourceItem, List<JoinPart> joinParts) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.joinParts = joinParts;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("TableSourceNested [(");
      builder.append(tableSourceItem);
      if (CollectionUtils.isNotEmpty(joinParts)) {
        builder.append(" ");
        builder.append(Joiner.on(" ").join(joinParts));
      }
      builder.append(")]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   tableSourceItem
    : tableName
      (PARTITION '(' uidList ')' )? (AS? alias=uid)?
      (indexHint (',' indexHint)* )?                #atomTableItem
    | (
      selectStatement
      | '(' parenthesisSubquery=selectStatement ')'
      )
      AS? alias=uid                                #subqueryTableItem
    | '(' tableSources ')'                         #tableSourcesItem
    ;
   * </pre>
   */
  public static interface TableSourceItem extends PrimitiveExpression {
  }

  public static class AtomTableItem implements TableSourceItem {
    public final TableName tableName;
    public final UidList uidList;
    public final Uid alias;
    public final List<IndexHint> indexHints;

    AtomTableItem(TableName tableName, UidList uidList, Uid alias, List<IndexHint> indexHints) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.uidList = uidList;
      this.alias = alias;
      this.indexHints = indexHints;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(tableName);
      if (uidList != null) {
        builder.append("PARTITION (" + uidList + ")");
      }
      if (alias != null) {
        builder.append(alias);
      }
      if (CollectionUtils.isNotEmpty(indexHints)) {
        builder.append(Joiner.on(", ").join(indexHints));
      }
      return builder.toString();
    }

  }

  public static class SubqueryTableItem implements TableSourceItem {
    public final SelectStatement selectStatement;
    public final SelectStatement parenthesisSubquery;

    SubqueryTableItem(SelectStatement selectStatement, SelectStatement parenthesisSubquery) {
      Preconditions.checkArgument(selectStatement != null);
      Preconditions.checkArgument(parenthesisSubquery != null);

      this.selectStatement = selectStatement;
      this.parenthesisSubquery = parenthesisSubquery;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SubqueryTableItem [selectStatement=");
      builder.append(selectStatement);
      builder.append(", parenthesisSubquery=");
      builder.append(parenthesisSubquery);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class TableSourcesItem implements TableSourceItem {
    public final TableSources tableSources;

    TableSourcesItem(TableSources tableSources) {
      Preconditions.checkArgument(tableSources != null);

      this.tableSources = tableSources;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("TableSourcesItem [tableSources=");
      builder.append(tableSources);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  indexHint
    : indexHintAction=(USE | IGNORE | FORCE)
      keyFormat=(INDEX|KEY) ( FOR indexHintType)?
      '(' uidList ')'
    ;
   * </pre>
   */
  public static class IndexHint implements PrimitiveExpression {
    public static enum IndexHintAction {
      USE, IGNORE, FORCE;
    }

    public static enum KeyFormat {
      INDEX, KEY;
    }

    public final IndexHintAction indexHintAction;
    public final KeyFormat keyFormat;
    public final IndexHintType indexHintType;
    public final UidList uidList;

    IndexHint(IndexHintAction indexHintAction, KeyFormat keyFormat, IndexHintType indexHintType,
        UidList uidList) {
      Preconditions.checkArgument(indexHintAction != null);
      Preconditions.checkArgument(keyFormat != null);
      Preconditions.checkArgument(uidList != null);

      this.indexHintAction = indexHintAction;
      this.keyFormat = keyFormat;
      this.indexHintType = indexHintType;
      this.uidList = uidList;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("IndexHint [indexHintAction=");
      builder.append(indexHintAction);
      builder.append(", keyFormat=");
      builder.append(keyFormat);
      builder.append(", indexHintType=");
      builder.append(indexHintType);
      builder.append(", uidList=");
      builder.append(uidList);
      builder.append("]");
      return builder.toString();
    }

  }

  public static enum IndexHintType {
    JOIN, ORDER_BY, GROUP_BY;
  }

  // tableName : fullId
  public static class TableName implements PrimitiveExpression {
    public final FullId fullId;

    TableName(FullId fullId) {
      Preconditions.checkArgument(fullId != null);

      this.fullId = fullId;
    }

    @Override
    public String toString() {
      return fullId.toString();
    }

  }

  /**
   * <pre>
   joinPart
    : (INNER | CROSS)? JOIN tableSourceItem
      (
        ON expression
        | USING '(' uidList ')'
      )?                                                     #innerJoin
    | STRAIGHT_JOIN tableSourceItem (ON expression)?         #straightJoin
    | (LEFT | RIGHT) OUTER? JOIN tableSourceItem
        (
          ON expression
          | USING '(' uidList ')'
        )                                                    #outerJoin
    | NATURAL ((LEFT | RIGHT) OUTER?)? JOIN tableSourceItem  #naturalJoin
    ;
   * </pre>
   */
  public static interface JoinPart extends PrimitiveExpression {
  }

  public static class InnerJoin implements JoinPart {
    public final TableSourceItem tableSourceItem;
    public final Expression expression;
    public final UidList uidList;

    InnerJoin(TableSourceItem tableSourceItem, Expression expression, UidList uidList) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
      this.uidList = uidList;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("InnerJoin [tableSourceItem=");
      builder.append(tableSourceItem);
      builder.append(", expression=");
      builder.append(expression);
      builder.append(", uidList=");
      builder.append(uidList);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class StraightJoin implements JoinPart {
    public final TableSourceItem tableSourceItem;
    public final Expression expression;

    StraightJoin(TableSourceItem tableSourceItem, Expression expression) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("StraightJoin [tableSourceItem=");
      builder.append(tableSourceItem);
      builder.append(", expression=");
      builder.append(expression);
      builder.append("]");
      return builder.toString();
    }
  }

  public static class OuterJoin implements JoinPart {
    public final OuterJoinType type;
    public final TableSourceItem tableSourceItem;
    public final Expression expression;
    public final UidList uidList;

    OuterJoin(OuterJoinType type, TableSourceItem tableSourceItem, Expression expression,
        UidList uidList) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(tableSourceItem != null);
      Preconditions.checkArgument(!(expression == null && uidList == null));

      this.type = type;
      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
      this.uidList = uidList;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("OuterJoin [type=");
      builder.append(type);
      builder.append(", tableSourceItem=");
      builder.append(tableSourceItem);
      builder.append(", expression=");
      builder.append(expression);
      builder.append(", uidList=");
      builder.append(uidList);
      builder.append("]");
      return builder.toString();
    }

  }

  public static enum OuterJoinType {
    LEFT, RIGHT
  }

  public static class NaturalJoin implements JoinPart {
    public final OuterJoinType outerJoinType;
    public final TableSourceItem tableSourceItem;

    NaturalJoin(OuterJoinType outerJoinType, TableSourceItem tableSourceItem) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.outerJoinType = outerJoinType;
      this.tableSourceItem = tableSourceItem;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("NaturalJoin [outerJoinType=");
      builder.append(outerJoinType);
      builder.append(", tableSourceItem=");
      builder.append(tableSourceItem);
      builder.append("]");
      return builder.toString();
    }

  }

  // selectElements: (star='*' | selectElement ) (',' selectElement)*
  public static class SelectElements implements PrimitiveExpression {
    public final Boolean star;
    public final List<SelectElement> selectElements;

    public SelectElements(Boolean star, List<SelectElement> selectElements) {
      this.star = star;
      this.selectElements = selectElements;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      if (Boolean.TRUE.equals(star)) {
        builder.append("*");
      }
      if (CollectionUtils.isNotEmpty(selectElements)) {
        if (Boolean.TRUE.equals(star)) {
          builder.append(", ");
        }
        builder.append(Joiner.on(", ").join(selectElements));
      }
      return builder.toString();
    }

  }

  /**
   * <pre>
   selectElement
    : fullId '.' '*'                                 #selectStarElement
    | fullColumnName (AS? uid)?                      #selectColumnElement
    | functionCall (AS? uid)?                        #selectFunctionElement
    | (LOCAL_ID VAR_ASSIGN)? expression (AS? uid)?   #selectExpressionElement
   * </pre>
   */
  public static interface SelectElement extends PrimitiveExpression {
  }

  public static class SelectStarElement implements SelectElement {
    public final FullId fullId;

    SelectStarElement(FullId fullId) {
      Preconditions.checkArgument(fullId != null);
      this.fullId = fullId;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectStarElement [fullId=");
      builder.append(fullId);
      builder.append("]");
      return builder.toString();
    }
  }

  public static class SelectColumnElement implements SelectElement {
    public final FullColumnName fullColumnName;
    public final Uid uid;

    SelectColumnElement(FullColumnName fullColumnName, Uid uid) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
      this.uid = uid;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(fullColumnName);
      if (uid != null) {
        builder.append(", uid=");
      }
      return builder.toString();
    }

  }

  public static class SelectFunctionElement implements SelectElement {
    public final FunctionCall functionCall;
    public final Uid uid;

    SelectFunctionElement(FunctionCall functionCall, Uid uid) {
      Preconditions.checkArgument(functionCall != null);

      this.functionCall = functionCall;
      this.uid = uid;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectFunctionElement [functionCall=");
      builder.append(functionCall);
      builder.append(", uid=");
      builder.append(uid);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class SelectExpressionElement implements SelectElement {
    public final String localId;
    public final Expression expression;
    public final Uid uid;

    SelectExpressionElement(String localId, Expression expression, Uid uid) {
      Preconditions.checkArgument(expression != null);

      this.localId = localId;
      this.expression = expression;
      this.uid = uid;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectExpressionElement [localId=");
      builder.append(localId);
      builder.append(", expression=");
      builder.append(expression);
      builder.append(", uid=");
      builder.append(uid);
      builder.append("]");
      return builder.toString();
    }

  }

  public static enum LockClauseEnum {
    FOR_UPDATE, LOCK_IN_SHARE_MODE
  }

  // queryExpression: '(' querySpecification ')' | '(' queryExpression ')'
  public static class QueryExpression implements PrimitiveExpression {
    public final QuerySpecification querySpecification;
    public final QueryExpression queryExpression;

    QueryExpression(QuerySpecification querySpecification, QueryExpression queryExpression) {
      Preconditions.checkArgument(!(querySpecification == null && queryExpression == null));

      this.querySpecification = querySpecification;
      this.queryExpression = queryExpression;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("QueryExpression [querySpecification=");
      builder.append(querySpecification);
      builder.append(", queryExpression=");
      builder.append(queryExpression);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
   selectIntoExpression
    : INTO assignmentField (',' assignmentField )*    #selectIntoVariables
    | INTO DUMPFILE STRING_LITERAL                    #selectIntoDumpFile
    | (
        INTO OUTFILE filename=STRING_LITERAL
        (CHARACTER SET charset=charsetName)?
        (
          fieldsFormat=(FIELDS | COLUMNS)
          selectFieldsInto+
        )?
        (
          LINES selectLinesInto+
        )?
      )                                               #selectIntoTextFile
    ;
   * </pre>
   */
  public static interface SelectIntoExpression extends PrimitiveExpression {
  }

  public static class SelectIntoVariables implements SelectIntoExpression {
    public final List<AssignmentField> assignmentFields;

    SelectIntoVariables(List<AssignmentField> assignmentFields) {
      Preconditions.checkArgument(assignmentFields != null && assignmentFields.size() > 0);

      this.assignmentFields = assignmentFields;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectIntoVariables [assignmentFields=");
      builder.append(assignmentFields);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class AssignmentField implements PrimitiveExpression {
    public final Uid uid;
    public final String localId;

    AssignmentField(Uid uid, String localId) {
      Preconditions.checkArgument(!(uid == null && localId == null));
      this.uid = uid;
      this.localId = localId;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("AssignmentField [uid=");
      builder.append(uid);
      builder.append(", localId=");
      builder.append(localId);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class SelectIntoDumpFile implements SelectIntoExpression {
    public final String stringLiteral;

    SelectIntoDumpFile(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectIntoDumpFile [stringLiteral=");
      builder.append(stringLiteral);
      builder.append("]");
      return builder.toString();
    }

  }

  public static class SelectIntoTextFile implements SelectIntoExpression {
    public static enum TieldsFormatType {
      FIELDS, COLUMNS
    }

    public final String filename;
    public final CharsetName charsetName;
    public final TieldsFormatType fieldsFormat;
    public final List<SelectFieldsInto> selectFieldsIntos;
    public final List<SelectLinesInto> selectLinesInto;

    SelectIntoTextFile(String filename, //
        CharsetName charsetName, //
        TieldsFormatType fieldsFormat, List<SelectFieldsInto> selectFieldsIntos, //
        List<SelectLinesInto> selectLinesInto//
    ) {
      Preconditions.checkArgument(filename != null);

      this.filename = filename;
      this.charsetName = charsetName;
      this.fieldsFormat = fieldsFormat;
      this.selectFieldsIntos = selectFieldsIntos;
      this.selectLinesInto = selectLinesInto;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectIntoTextFile [filename=");
      builder.append(filename);
      builder.append(", charsetName=");
      builder.append(charsetName);
      builder.append(", fieldsFormat=");
      builder.append(fieldsFormat);
      builder.append(", selectFieldsIntos=");
      builder.append(selectFieldsIntos);
      builder.append(", selectLinesInto=");
      builder.append(selectLinesInto);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  selectFieldsInto
    : TERMINATED BY terminationField=STRING_LITERAL
    | OPTIONALLY? ENCLOSED BY enclosion=STRING_LITERAL
    | ESCAPED BY escaping=STRING_LITERAL
    ;
   * </pre>
   */
  public static class SelectFieldsInto implements PrimitiveExpression {
    public static enum Type {
      TERMINATED_BY, ENCLOSED_BY, ESCAPED_BY
    }

    public final SelectFieldsInto.Type type;
    public final Boolean optionally;
    public final String stringLiteral;

    SelectFieldsInto(SelectFieldsInto.Type type, Boolean optionally, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.optionally = optionally;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectFieldsInto [type=");
      builder.append(type);
      builder.append(", optionally=");
      builder.append(optionally);
      builder.append(", stringLiteral=");
      builder.append(stringLiteral);
      builder.append("]");
      return builder.toString();
    }

  }

  /**
   * <pre>
  selectLinesInto
    : STARTING BY starting=STRING_LITERAL
    | TERMINATED BY terminationLine=STRING_LITERAL
    ;
   * </pre>
   */
  public static class SelectLinesInto implements PrimitiveExpression {
    public static enum Type {
      STARTING_BY, TERMINATED_BY
    }

    public final SelectLinesInto.Type type;
    public final String stringLiteral;

    SelectLinesInto(SelectLinesInto.Type type, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SelectLinesInto [type=");
      builder.append(type);
      builder.append(", stringLiteral=");
      builder.append(stringLiteral);
      builder.append("]");
      return builder.toString();
    }
  }
}