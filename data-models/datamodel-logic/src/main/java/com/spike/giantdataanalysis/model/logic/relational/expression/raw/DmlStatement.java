package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalAlgebraEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.ExpressionsWithDefaults;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;

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
    LOW_PRIORITY, CONCURRENT;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum ViolationEnum implements RelationalAlgebraEnum {
    REPLACE, IGNORE;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum FieldsFormatEnum implements RelationalAlgebraEnum {
    FIELDS, COLUMNS;
    @Override
    public String literal() {
      return name();
    }
  }

  public static enum LinesFormatEnum implements RelationalAlgebraEnum {
    LINES, ROWS;
    @Override
    public String literal() {
      return name();
    }
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      if (selectStatement != null) {
        sb.append(selectStatement.literal());
      } else {
        sb.append("VALUES ");
        List<String> literals = Lists.newArrayList();
        for (ExpressionsWithDefaults e : expressionsWithDefaults) {
          literals.add("(" + e.literal() + ")");
        }
        sb.append(Joiner.on(", ").join(literals));

      }
      return sb.toString();
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

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(fullColumnName.literal()).append(" = ");
      if (expression != null) {
        sb.append(expression.literal());
      } else {
        sb.append("DEFAULT");
      }
      return sb.toString();
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
    public String literal() {
      if (uid != null) {
        return uid.literal();
      } else {
        return localId;
      }
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
    FOR_UPDATE("FOR UPDATE"), LOCK_IN_SHARE_MODE("LOCK IN SHARE MODE");

    public String literal;

    LockClauseEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
    }
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("ORDER BY ");
      List<String> literals = Lists.newArrayList();
      for (OrderByExpression orderByExpression : orderByExpressions) {
        literals.add(orderByExpression.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
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
      ASC, DESC;
      @Override
      public String literal() {
        return name();
      }
    }

    public final Expression expression;
    public final OrderByExpression.OrderType order;

    OrderByExpression(Expression expression, OrderByExpression.OrderType order) {
      Preconditions.checkArgument(expression != null);

      this.expression = expression;
      this.order = order;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(expression.literal());
      if (order != null) {
        sb.append(" ").append(order.literal());
      }
      return sb.toString();
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      List<String> literals = Lists.newArrayList();
      for (TableSource tableSource : tableSources) {
        literals.add(tableSource.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(tableSourceItem.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(joinParts)) {
        List<String> literals = Lists.newArrayList();
        for (JoinPart joinPart : joinParts) {
          literals.add(joinPart.literal());
        }
        sb.append(Joiner.on(", ").join(literals)).append(" ");
      }
      return sb.toString();
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(").append(tableSourceItem.literal()).append(" ");
      if (CollectionUtils.isNotEmpty(joinParts)) {
        List<String> literals = Lists.newArrayList();
        for (JoinPart joinPart : joinParts) {
          literals.add(joinPart.literal());
        }
        sb.append(Joiner.on(", ").join(literals)).append(" ");
      }
      sb.append(")");
      return sb.toString();
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(tableName.literal()).append(" ");
      if (uidList != null) {
        sb.append("PARTITION (").append(uidList.literal()).append(") ");
      }
      if (alias != null) {
        sb.append("AS ").append(alias.literal()).append(" ");
      }
      List<String> literals = Lists.newArrayList();
      for (IndexHint indexHint : indexHints) {
        literals.add(indexHint.literal());
      }
      sb.append(Joiner.on(", ").join(literals));
      return sb.toString();
    }

  }

  public static class SubqueryTableItem implements TableSourceItem {
    public final SelectStatement selectStatement;
    public final Uid alias;

    SubqueryTableItem(SelectStatement selectStatement, Uid alias) {
      Preconditions.checkArgument(selectStatement != null);
      Preconditions.checkArgument(alias != null);

      this.selectStatement = selectStatement;
      this.alias = alias;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(").append(selectStatement.literal()).append(") AS ").append(alias.literal());
      return sb.toString();
    }

  }

  public static class TableSourcesItem implements TableSourceItem {
    public final TableSources tableSources;

    TableSourcesItem(TableSources tableSources) {
      Preconditions.checkArgument(tableSources != null);

      this.tableSources = tableSources;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("(").append(tableSources.literal()).append(")");
      return sb.toString();
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

      @Override
      public String literal() {
        return name();
      }
    }

    public static enum KeyFormat implements RelationalAlgebraEnum {
      INDEX, KEY;

      @Override
      public String literal() {
        return name();
      }
    }

    public final IndexHintAction indexHintAction;
    public final KeyFormat keyFormat;
    public final IndexHintTypeEnum indexHintType;
    public final UidList uidList;

    IndexHint(IndexHint.IndexHintAction indexHintAction, IndexHint.KeyFormat keyFormat,
        IndexHintTypeEnum indexHintType, UidList uidList) {
      Preconditions.checkArgument(indexHintAction != null);
      Preconditions.checkArgument(keyFormat != null);
      Preconditions.checkArgument(uidList != null);

      this.indexHintAction = indexHintAction;
      this.keyFormat = keyFormat;
      this.indexHintType = indexHintType;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(indexHintAction.literal()).append(" ").append(keyFormat.literal()).append(" ");
      if (indexHintType != null) {
        sb.append("FOR ").append(indexHintType.literal()).append(" ");
      }
      sb.append("(").append(uidList.literal()).append(")");
      return sb.toString();
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
    JOIN("JOIN"), ORDER_BY("ORDER BY"), GROUP_BY("GROUP BY");

    public String literal;

    IndexHintTypeEnum(String literal) {
      this.literal = literal;
    }

    @Override
    public String literal() {
      return literal;
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("INNER JOIN ").append(tableSourceItem.literal());
      if (expression != null) {
        sb.append(" ON ").append(expression.literal()).append(" ");
      }
      if (uidList != null) {
        sb.append(" USING ").append(uidList.literal());
      }
      return sb.toString();
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
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("STRAIGHT_JOIN ").append(tableSourceItem.literal());
      if (expression != null) {
        sb.append(" ON ").append(expression.literal());
      }
      return sb.toString();
    }
  }

  public static class OuterJoin implements JoinPart {
    public final DmlStatement.OuterJoinType type;
    public final TableSourceItem tableSourceItem;
    public final Expression expression;
    public final UidList uidList;

    OuterJoin(DmlStatement.OuterJoinType type, TableSourceItem tableSourceItem,
        Expression expression, UidList uidList) {
      Preconditions.checkArgument(type != null);
      Preconditions.checkArgument(tableSourceItem != null);
      Preconditions.checkArgument(!(expression == null && uidList == null));

      this.type = type;
      this.tableSourceItem = tableSourceItem;
      this.expression = expression;
      this.uidList = uidList;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append(type.literal()).append(" OUTER JOIN ").append(tableSourceItem.literal())
          .append(" ");
      if (expression != null) {
        sb.append("ON ").append(expression.literal());
      } else {
        sb.append("USING (").append(uidList.literal()).append(")");
      }
      return sb.toString();
    }

  }

  public static enum OuterJoinType implements RelationalAlgebraEnum {
    LEFT, RIGHT;

    @Override
    public String literal() {
      return name();
    }
  }

  public static class NaturalJoin implements JoinPart {
    public final DmlStatement.OuterJoinType outerJoinType;
    public final TableSourceItem tableSourceItem;

    NaturalJoin(OuterJoinType outerJoinType, TableSourceItem tableSourceItem) {
      Preconditions.checkArgument(tableSourceItem != null);

      this.outerJoinType = outerJoinType;
      this.tableSourceItem = tableSourceItem;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("NATURAL ");
      if (outerJoinType != null) {
        sb.append(outerJoinType.literal()).append(" OUTER ");
      }
      sb.append("JOIN ").append(tableSourceItem.literal());
      return sb.toString();
    }

  }
}