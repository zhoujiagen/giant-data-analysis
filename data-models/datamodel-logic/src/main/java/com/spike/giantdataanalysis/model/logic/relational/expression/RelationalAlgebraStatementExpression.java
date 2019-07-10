package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.RelationalAlgebraExpressionAtom.MysqlVariable;

/**
 * 关系代数语句表达式:
 * 
 * <pre>
sqlStatement
    : ddlStatement | dmlStatement | transactionStatement
    | replicationStatement | preparedStatement
    | administrationStatement | utilityStatement
    ;
 * </pre>
 */
public interface RelationalAlgebraStatementExpression {
  /**
   * <pre>
  dmlStatement
    : selectStatement | insertStatement | updateStatement
    | deleteStatement | replaceStatement | callStatement
    | loadDataStatement | loadXmlStatement | doStatement
    | handlerStatement
    ;
   * </pre>
   */
  public static interface DmlStatement extends RelationalAlgebraStatementExpression {
  }

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
  public static interface SelectStatement extends DmlStatement {
  }

  public static class SimpleSelect implements SelectStatement {
    final QuerySpecification querySpecification;
    final LockClauseEnum lockClause;

    SimpleSelect(QuerySpecification querySpecification, LockClauseEnum lockClause) {
      Preconditions.checkArgument(querySpecification != null);

      this.querySpecification = querySpecification;
      this.lockClause = lockClause;
    }
  }

  public static class ParenthesisSelect implements SelectStatement {
    final QueryExpression queryExpression;
    final LockClauseEnum lockClause;

    ParenthesisSelect(QueryExpression queryExpression, LockClauseEnum lockClause) {
      Preconditions.checkArgument(queryExpression != null);

      this.queryExpression = queryExpression;
      this.lockClause = lockClause;
    }
  }

  public static class UnionSelect implements SelectStatement {
    final QuerySpecificationNointo querySpecificationNointo;
    final List<UnionStatement> unionStatements;
    final UnionTypeEnum unionType;
    final QuerySpecification querySpecification;
    final QueryExpression queryExpression;
    final OrderByClause orderByClause;
    final LimitClause limitClause;
    final LockClauseEnum lockClause;

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

  }

  public static class UnionParenthesisSelect implements SelectStatement {
    final QueryExpressionNointo queryExpressionNointo;
    final List<UnionParenthesis> unionParenthesisList;
    final UnionTypeEnum unionType;
    final QueryExpression queryExpression;
    final OrderByClause orderByClause;
    final LimitClause limitClause;
    final LockClauseEnum lockClause;

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
  }

  // ---------------------------------------------------------------------------
  // Helper Classes
  // ---------------------------------------------------------------------------
  public static enum UnionTypeEnum {
    ALL, DISTINCT
  }

  // unionStatement: UNION unionType=(ALL | DISTINCT)?
  // (querySpecificationNointo | queryExpressionNointo)
  public static class UnionStatement implements RelationalAlgebraPrimitiveExpression {
    final UnionTypeEnum unionType;
    final QuerySpecificationNointo querySpecificationNointo;
    final QueryExpressionNointo queryExpressionNointo;

    UnionStatement(UnionTypeEnum unionType, QuerySpecificationNointo querySpecificationNointo,
        QueryExpressionNointo queryExpressionNointo) {
      Preconditions.checkArgument(unionType != null);
      Preconditions.checkArgument(!(querySpecificationNointo == null//
          && queryExpressionNointo == null));

      this.unionType = unionType;
      this.querySpecificationNointo = querySpecificationNointo;
      this.queryExpressionNointo = queryExpressionNointo;
    }
  }

  // unionParenthesis: UNION unionType=(ALL | DISTINCT)? queryExpressionNointo
  public static class UnionParenthesis implements RelationalAlgebraPrimitiveExpression {
    final UnionTypeEnum unionType;
    final QueryExpressionNointo queryExpressionNointo;

    UnionParenthesis(UnionTypeEnum unionType, QueryExpressionNointo queryExpressionNointo) {
      Preconditions.checkArgument(queryExpressionNointo != null);

      this.unionType = unionType;
      this.queryExpressionNointo = queryExpressionNointo;
    }
  }

  // querySpecificationNointo: SELECT selectSpec* selectElements
  // fromClause? orderByClause? limitClause?
  public static class QuerySpecificationNointo implements RelationalAlgebraPrimitiveExpression {
    final List<SelectSpecEnum> selectSpecs;
    final SelectElements selectElements;
    final FromClause fromClause;
    final OrderByClause orderByClause;
    final LimitClause limitClause;

    QuerySpecificationNointo(List<SelectSpecEnum> selectSpecs, SelectElements selectElements,
        FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
      Preconditions.checkArgument(selectElements != null);

      this.selectSpecs = selectSpecs;
      this.selectElements = selectElements;
      this.fromClause = fromClause;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
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
  public static class QuerySpecification implements RelationalAlgebraPrimitiveExpression {
    final SelectSpecEnum selectSpec;
    final SelectElements selectElements;
    final SelectIntoExpression selectIntoExpression;
    final Boolean selectIntoExpressionBeforeFrom; // selectIntoExpression的位置, 可能需要区分
    final FromClause fromClause;
    final OrderByClause orderByClause;
    final LimitClause limitClause;

    QuerySpecification(SelectSpecEnum selectSpec, SelectElements selectElements,
        SelectIntoExpression selectIntoExpression, //
        Boolean selectIntoExpressionBeforeFrom, //
        FromClause fromClause, OrderByClause orderByClause, LimitClause limitClause) {
      Preconditions.checkArgument(selectElements != null);

      this.selectSpec = selectSpec;
      this.selectElements = selectElements;
      this.selectIntoExpression = selectIntoExpression;
      this.selectIntoExpressionBeforeFrom = selectIntoExpressionBeforeFrom;
      this.fromClause = fromClause;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
    }
  }

  // queryExpressionNointo : '(' querySpecificationNointo ')'| '(' queryExpressionNointo ')'
  public static class QueryExpressionNointo implements RelationalAlgebraPrimitiveExpression {
    final QuerySpecificationNointo querySpecificationNointo;

    public QueryExpressionNointo(QuerySpecificationNointo querySpecificationNointo) {
      Preconditions.checkArgument(querySpecificationNointo != null);

      this.querySpecificationNointo = querySpecificationNointo;
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
  public static class FromClause implements RelationalAlgebraPrimitiveExpression {
    final TableSources tableSources;
    final RelationalAlgebraConditionalExpression whereExpr;
    final List<GroupByItem> groupByItem;
    final Boolean withRollup;
    final RelationalAlgebraConditionalExpression havingExpr;

    FromClause(TableSources tableSources, RelationalAlgebraConditionalExpression whereExpr,
        List<GroupByItem> groupByItem, Boolean withRollup,
        RelationalAlgebraConditionalExpression havingExpr) {
      Preconditions.checkArgument(tableSources != null);

      this.tableSources = tableSources;
      this.whereExpr = whereExpr;
      this.groupByItem = groupByItem;
      this.withRollup = withRollup;
      this.havingExpr = havingExpr;
    }
  }

  // groupByItem : expression order=(ASC | DESC)?
  public static class GroupByItem implements RelationalAlgebraPrimitiveExpression {
    public static enum OrderType {
      ASC, DESC
    }

    final RelationalAlgebraConditionalExpression expression;
    final OrderType order;

    GroupByItem(RelationalAlgebraConditionalExpression expression, OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }
  }

  // orderByClause: ORDER BY orderByExpression (',' orderByExpression)*
  public static class OrderByClause implements RelationalAlgebraPrimitiveExpression {
    final List<OrderByExpression> orderByExpressions;

    OrderByClause(List<OrderByExpression> orderByExpressions) {
      Preconditions.checkArgument(orderByExpressions != null && orderByExpressions.size() > 0);

      this.orderByExpressions = orderByExpressions;
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
  public static class LimitClause implements RelationalAlgebraPrimitiveExpression {
    final LimitClauseAtom limit;
    final LimitClauseAtom offset;

    LimitClause(LimitClauseAtom limit, LimitClauseAtom offset) {
      Preconditions.checkArgument(limit != null);

      this.limit = limit;
      this.offset = offset;
    }
  }

  public static class LimitClauseAtom implements RelationalAlgebraPrimitiveExpression {
    final DecimalLiteral decimalLiteral;
    final MysqlVariable mysqlVariable;

    LimitClauseAtom(DecimalLiteral decimalLiteral, MysqlVariable mysqlVariable) {
      Preconditions.checkArgument(!(decimalLiteral == null && mysqlVariable == null));

      this.decimalLiteral = decimalLiteral;
      this.mysqlVariable = mysqlVariable;
    }
  }

  // tableSources: tableSource (',' tableSource)*
  public static class TableSources implements RelationalAlgebraPrimitiveExpression {
    final List<TableSource> tableSources;

    TableSources(List<TableSource> tableSources) {
      Preconditions.checkArgument(tableSources != null && tableSources.size() > 0);

      this.tableSources = tableSources;
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
  public static interface TableSource extends RelationalAlgebraPrimitiveExpression {
  }

  public static class TableSourceBase implements TableSource {
    final TableSourceItem tableSourceItem;
    final List<JoinPart> joinPart;

    TableSourceBase(TableSourceItem tableSourceItem, List<JoinPart> joinPart) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.joinPart = joinPart;
    }
  }

  public static class TableSourceNested implements TableSource {
    final TableSourceItem tableSourceItem;
    final List<JoinPart> joinPart;

    TableSourceNested(TableSourceItem tableSourceItem, List<JoinPart> joinPart) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.joinPart = joinPart;
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
  public static interface TableSourceItem extends RelationalAlgebraPrimitiveExpression {
  }

  public static class AtomTableItem implements TableSourceItem {
    final TableName tableName;
    final UidList uidList;
    final Uid alias;
    final List<IndexHint> indexHint;

    AtomTableItem(TableName tableName, UidList uidList, Uid alias, List<IndexHint> indexHint) {
      Preconditions.checkArgument(tableName != null);

      this.tableName = tableName;
      this.uidList = uidList;
      this.alias = alias;
      this.indexHint = indexHint;
    }
  }

  public static class SubqueryTableItem implements TableSourceItem {
    final SelectStatement selectStatement;
    final SelectStatement parenthesisSubquery;

    SubqueryTableItem(SelectStatement selectStatement, SelectStatement parenthesisSubquery) {
      Preconditions.checkArgument(selectStatement != null);
      Preconditions.checkArgument(parenthesisSubquery != null);

      this.selectStatement = selectStatement;
      this.parenthesisSubquery = parenthesisSubquery;
    }
  }

  public static class TableSourcesItem implements TableSourceItem {
    final TableSources tableSources;

    TableSourcesItem(TableSources tableSources) {
      Preconditions.checkArgument(tableSources != null);

      this.tableSources = tableSources;
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
  public static class IndexHint implements RelationalAlgebraPrimitiveExpression {
    public static enum IndexHintAction {
      USE, IGNORE, FORCE;
    }

    public static enum KeyFormat {
      INDEX, KEY;
    }

    final IndexHintAction indexHintAction;
    final KeyFormat keyFormat;
    final IndexHintType indexHintType;
    final UidList uidList;

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

  }

  public static enum IndexHintType {
    JOIN, ORDER_BY, GROUP_BY;
  }

  public static class TableName implements RelationalAlgebraPrimitiveExpression {
    final FullId fullId;

    TableName(FullId fullId) {
      Preconditions.checkArgument(fullId != null);

      this.fullId = fullId;
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
  public static interface JoinPart extends RelationalAlgebraPrimitiveExpression {
  }

  public static class InnerJoin implements JoinPart {
    final TableSourceItem tableSourceItem;
    final RelationalAlgebraConditionalExpression expression;
    final UidList uidList;

    InnerJoin(TableSourceItem tableSourceItem, RelationalAlgebraConditionalExpression expression,
        UidList uidList) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
      this.uidList = uidList;
    }

  }

  public static class StraightJoin implements JoinPart {
    final TableSourceItem tableSourceItem;
    final RelationalAlgebraConditionalExpression expression;

    StraightJoin(TableSourceItem tableSourceItem,
        RelationalAlgebraConditionalExpression expression) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
    }
  }

  public static class OuterJoin implements JoinPart {
    final OuterJoinType type;
    final TableSourceItem tableSourceItem;
    final RelationalAlgebraConditionalExpression expression;
    final UidList uidList;

    OuterJoin(OuterJoinType type, TableSourceItem tableSourceItem,
        RelationalAlgebraConditionalExpression expression, UidList uidList) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(tableSourceItem != null);
      Preconditions.checkArgument(!(expression == null && uidList == null));

      this.type = type;
      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
      this.uidList = uidList;
    }
  }

  public static enum OuterJoinType {
    LEFT, RIGHT
  }

  public static class NaturalJoin implements JoinPart {
    final OuterJoinType outerJoinType;
    final TableSourceItem tableSourceItem;

    NaturalJoin(OuterJoinType outerJoinType, TableSourceItem tableSourceItem) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.outerJoinType = outerJoinType;
      this.tableSourceItem = tableSourceItem;
    }
  }

  // selectElements: (star='*' | selectElement ) (',' selectElement)*
  public static class SelectElements implements RelationalAlgebraPrimitiveExpression {
    final Boolean star;
    final SelectElement first;
    final List<SelectElement> left;

    public SelectElements(Boolean star, SelectElement first, List<SelectElement> left) {
      Preconditions.checkArgument(!(star == null && first == null));
      this.star = star;
      this.first = first;
      this.left = left;
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
  public static interface SelectElement extends RelationalAlgebraPrimitiveExpression {
  }

  public static class SelectStarElement implements SelectElement {
    final FullId fullId;

    SelectStarElement(FullId fullId) {
      Preconditions.checkArgument(fullId != null);
      this.fullId = fullId;
    }
  }

  public static class SelectColumnElement implements SelectElement {
    final FullColumnName fullColumnName;
    final Uid uid;

    SelectColumnElement(FullColumnName fullColumnName, Uid uid) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
      this.uid = uid;
    }
  }

  public static class SelectFunctionElement implements SelectElement {
    final FunctionCall functionCall;
    final Uid uid;

    SelectFunctionElement(FunctionCall functionCall, Uid uid) {
      Preconditions.checkArgument(functionCall != null);

      this.functionCall = functionCall;
      this.uid = uid;
    }
  }

  public static class SelectExpressionElement implements SelectElement {
    final String localId;
    final RelationalAlgebraConditionalExpression expression;
    final Uid uid;

    SelectExpressionElement(String localId, RelationalAlgebraConditionalExpression expression,
        Uid uid) {
      Preconditions.checkArgument(expression != null);

      this.localId = localId;
      this.expression = expression;
      this.uid = uid;
    }
  }

  public static enum LockClauseEnum {
    FOR_UPDATE, LOCK_IN_SHARE_MODE
  }

  // queryExpression: '(' querySpecification ')' | '(' queryExpression ')'
  public static class QueryExpression implements RelationalAlgebraPrimitiveExpression {

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
  public static interface SelectIntoExpression extends RelationalAlgebraPrimitiveExpression {
  }

  public static class SelectIntoVariables implements SelectIntoExpression {
    final List<AssignmentField> assignmentFields;

    SelectIntoVariables(List<AssignmentField> assignmentFields) {
      Preconditions.checkArgument(assignmentFields != null && assignmentFields.size() > 0);

      this.assignmentFields = assignmentFields;
    }
  }

  public static class AssignmentField implements RelationalAlgebraPrimitiveExpression {
    final Uid uid;
    final String localId;

    AssignmentField(Uid uid, String localId) {
      Preconditions.checkArgument(!(uid == null && localId == null));
      this.uid = uid;
      this.localId = localId;
    }
  }

  public static class SelectIntoDumpFile implements SelectIntoExpression {
    final String stringLiteral;

    SelectIntoDumpFile(String stringLiteral) {
      Preconditions.checkArgument(stringLiteral != null);

      this.stringLiteral = stringLiteral;
    }
  }

  public static class SelectIntoTextFile implements SelectIntoExpression {
    public static enum TieldsFormatType {
      FIELDS, COLUMNS
    }

    final String filename;
    final CharsetName charsetName;
    final TieldsFormatType fieldsFormat;
    final List<SelectFieldsInto> selectFieldsIntos;
    final List<SelectLinesInto> selectLinesInto;

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
  public static class SelectFieldsInto implements RelationalAlgebraPrimitiveExpression {
    public static enum Type {
      TERMINATED_BY, ENCLOSED_BY, ESCAPED_BY
    }

    final Type type;
    final Boolean optionally;
    final String stringLiteral;

    SelectFieldsInto(Type type, Boolean optionally, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.optionally = optionally;
      this.stringLiteral = stringLiteral;
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
  public static class SelectLinesInto implements RelationalAlgebraPrimitiveExpression {
    public static enum Type {
      STARTING_BY, TERMINATED_BY
    }

    final Type type;
    final String stringLiteral;

    SelectLinesInto(Type type, String stringLiteral) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(stringLiteral != null);

      this.type = type;
      this.stringLiteral = stringLiteral;
    }
  }

}
