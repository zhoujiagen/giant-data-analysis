package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.ExpressionsWithDefaults;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;

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
public interface DmlStatement extends SqlStatement {

  public static enum PriorityEnum implements RelationalAlgebraEnum {
    LOW_PRIORITY, CONCURRENT
  }

  public static enum ViolationEnum implements RelationalAlgebraEnum {
    REPLACE, IGNORE
  }

  public static enum FieldsFormatEnum implements RelationalAlgebraEnum {
    FIELDS, COLUMNS
  }

  public static enum LinesFormatEnum implements RelationalAlgebraEnum {
    LINES, ROWS
  }

  /**
   * <pre>
   insertStatementValue
    : selectStatement
    | insertFormat=(VALUES | VALUE)
      '(' expressionsWithDefaults ')'
        (',' '(' expressionsWithDefaults ')')*
    ;
   * </pre>
   */
  public static class InsertStatementValue implements PrimitiveExpression {
    public final SelectStatement selectStatement;
    public final List<ExpressionsWithDefaults> expressionsWithDefaults;

    InsertStatementValue(SelectStatement selectStatement,
        List<ExpressionsWithDefaults> expressionsWithDefaults) {
      Preconditions.checkArgument(!(selectStatement == null
          && (expressionsWithDefaults == null || expressionsWithDefaults.size() == 0)));

      this.selectStatement = selectStatement;
      this.expressionsWithDefaults = expressionsWithDefaults;
    }

  }

  /**
   * <pre>
   updatedElement
    : fullColumnName '=' (expression | DEFAULT)
    ;
   * </pre>
   */
  public static class UpdatedElement implements PrimitiveExpression {
    public final FullColumnName fullColumnName;
    public final Expression expression;

    UpdatedElement(FullColumnName fullColumnName, Expression expression) {
      Preconditions.checkArgument(fullColumnName != null);

      this.fullColumnName = fullColumnName;
      this.expression = expression;
    }

  }

  /**
   * <pre>
   assignmentField
    : uid | LOCAL_ID
    ;
   * </pre>
   */
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

  /**
   * <pre>
   lockClause
    : FOR UPDATE | LOCK IN SHARE MODE
    ;
   * </pre>
   */
  public static enum LockClauseEnum implements RelationalAlgebraEnum {
    FOR_UPDATE, LOCK_IN_SHARE_MODE
  }

  /**
   * <pre>
   orderByClause
    : ORDER BY orderByExpression (',' orderByExpression)*
    ;
   * </pre>
   */
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
   orderByExpression
    : expression order=(ASC | DESC)?
    ;
   * </pre>
   */
  public static class OrderByExpression implements PrimitiveExpression {
    public static enum OrderType implements RelationalAlgebraEnum {
      ASC, DESC
    }

    public final Expression expression;
    public final OrderByExpression.OrderType order;

    OrderByExpression(Expression expression, OrderByExpression.OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("OrderByExpression [expression=");
      builder.append(expression);
      builder.append(", order=");
      builder.append(order);
      builder.append("]");
      return builder.toString();
    }
  }

  /**
   * <pre>
   tableSources
    : tableSource (',' tableSource)*
    ;
   * </pre>
   */
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
    public static enum IndexHintAction implements RelationalAlgebraEnum {
      USE, IGNORE, FORCE;
    }

    public static enum KeyFormat implements RelationalAlgebraEnum {
      INDEX, KEY;
    }

    public final IndexHintAction indexHintAction;
    public final KeyFormat keyFormat;
    public final IndexHintTypeEnum indexHintType;
    public final UidList uidList;

    IndexHint(IndexHintAction indexHintAction, KeyFormat keyFormat, IndexHintTypeEnum indexHintType,
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

  /**
   * <pre>
   indexHintType
    : JOIN | ORDER BY | GROUP BY
    ;
   * </pre>
   */
  public static enum IndexHintTypeEnum implements RelationalAlgebraEnum {
    JOIN, ORDER_BY, GROUP_BY;
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

  public static enum OuterJoinType implements RelationalAlgebraEnum {
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
}