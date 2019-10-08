package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

/**
 * <pre>
deleteStatement
  : singleDeleteStatement | multipleDeleteStatement
  ;
 * </pre>
 */
public interface DeleteStatement extends DmlStatement {

  /**
   * <pre>
   singleDeleteStatement
    : DELETE priority=LOW_PRIORITY? QUICK? IGNORE?
    FROM tableName
      (PARTITION '(' uidList ')' )?
      (WHERE expression)?
      orderByClause? (LIMIT decimalLiteral)?
    ;
   * </pre>
   */
  public static class SingleDeleteStatement implements DeleteStatement {
    public final Boolean lowPriority;
    public final Boolean quick;
    public final Boolean ignore;
    public final TableName tableName;
    public final UidList uidList;
    public final Expression where;
    public final OrderByClause orderByClause;
    public final DecimalLiteral limit;

    SingleDeleteStatement(Boolean lowPriority, Boolean quick, Boolean ignore, TableName tableName,
        UidList uidList, Expression where, OrderByClause orderByClause, DecimalLiteral limit) {
      Preconditions.checkArgument(tableName != null);

      this.lowPriority = lowPriority;
      this.quick = quick;
      this.ignore = ignore;
      this.tableName = tableName;
      this.uidList = uidList;
      this.where = where;
      this.orderByClause = orderByClause;
      this.limit = limit;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DELETE ");
      if (Boolean.TRUE.equals(lowPriority)) {
        sb.append("LOW_PRIORITY ");
      }
      if (Boolean.TRUE.equals(quick)) {
        sb.append("QUICK ");
      }
      if (Boolean.TRUE.equals(ignore)) {
        sb.append("IGNORE ");
      }
      sb.append("FROM ").append(tableName.literal()).append(" ");
      if (uidList != null) {
        sb.append("PARTITION (").append(uidList.literal()).append(") ");
      }
      if (where != null) {
        sb.append("WHERE ").append(where.literal()).append(" ");
      }
      if (orderByClause != null) {
        sb.append(orderByClause.literal()).append(" ");
      }
      if (limit != null) {
        sb.append("LIMIT ").append(limit.literal());
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   multipleDeleteStatement
    : DELETE priority=LOW_PRIORITY? QUICK? IGNORE?
      (
        tableName ('.' '*')? ( ',' tableName ('.' '*')? )*
            FROM tableSources
        | FROM
            tableName ('.' '*')? ( ',' tableName ('.' '*')? )*
            USING tableSources
      )
      (WHERE expression)?
    ;
   * </pre>
   */
  public static class MultipleDeleteStatement implements DeleteStatement {
    public final Boolean lowPriority;
    public final Boolean quick;
    public final Boolean ignore;
    public final boolean using;
    public final List<TableName> tableNames;
    public final TableSources tableSources;
    public final Expression where;

    MultipleDeleteStatement(Boolean lowPriority, Boolean quick, Boolean ignore, boolean using,
        List<TableName> tableNames, TableSources tableSources, Expression where) {
      Preconditions.checkArgument(tableNames != null && tableNames.size() > 0);
      Preconditions.checkArgument(tableSources != null);

      this.lowPriority = lowPriority;
      this.quick = quick;
      this.ignore = ignore;
      this.using = using;
      this.tableNames = tableNames;
      this.tableSources = tableSources;
      this.where = where;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("DELETE ");
      if (Boolean.TRUE.equals(lowPriority)) {
        sb.append("LOW_PRIORITY ");
      }
      if (Boolean.TRUE.equals(quick)) {
        sb.append("QUICK ");
      }
      if (Boolean.TRUE.equals(ignore)) {
        sb.append("IGNORE ");
      }
      if (!using) {
        List<String> literals = Lists.newArrayList();
        for (TableName tableName : tableNames) {
          literals.add(tableName.literal());
        }
        sb.append(Joiner.on(", ").join(literals)).append(" ");
        sb.append("FROM ").append(tableSources.literal()).append(" ");
      } else {
        sb.append("FROM ");
        List<String> literals = Lists.newArrayList();
        for (TableName tableName : tableNames) {
          literals.add(tableName.literal());
        }
        sb.append(Joiner.on(", ").join(literals)).append(" ");
        sb.append("USING ").append(tableSources.literal()).append(" ");
      }
      if (where != null) {
        sb.append(where.literal());
      }
      return sb.toString();
    }

  }
}