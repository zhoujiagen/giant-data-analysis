package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.LimitClause;

/**
 * <pre>
updateStatement
  : singleUpdateStatement | multipleUpdateStatement
  ;
 * </pre>
 */
public interface UpdateStatement extends DmlStatement {

  /**
   * <pre>
   singleUpdateStatement
    : UPDATE priority=LOW_PRIORITY? IGNORE? tableName (AS? uid)?
      SET updatedElement (',' updatedElement)*
      (WHERE expression)? orderByClause? limitClause?
    ;
   * </pre>
   */
  public static class SingleUpdateStatement implements UpdateStatement {
    public final Boolean lowPriority;
    public final Boolean ignore;
    public final TableName tableName;
    public final Uid uid;
    public final List<UpdatedElement> updatedElements;
    public final Expression where;
    public final OrderByClause orderByClause;
    public final LimitClause limitClause;

    SingleUpdateStatement(Boolean lowPriority, Boolean ignore, TableName tableName, Uid uid,
        List<UpdatedElement> updatedElements, Expression where, OrderByClause orderByClause,
        LimitClause limitClause) {
      Preconditions.checkArgument(tableName != null);
      Preconditions.checkArgument(updatedElements != null && updatedElements.size() > 0);

      this.lowPriority = lowPriority;
      this.ignore = ignore;
      this.tableName = tableName;
      this.uid = uid;
      this.updatedElements = updatedElements;
      this.where = where;
      this.orderByClause = orderByClause;
      this.limitClause = limitClause;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("UPDATE ");
      if (Boolean.TRUE.equals(lowPriority)) {
        sb.append("LOW_PRIORITY ");
      }
      if (Boolean.TRUE.equals(ignore)) {
        sb.append("IGNORE ");
      }
      sb.append(tableName.literal()).append(" ");
      if (uid != null) {
        sb.append("AS ").append(uid.literal()).append(" ");
      }
      sb.append("SET ");
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : updatedElements) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals)).append(" ");
      if (where != null) {
        sb.append("WHERE ").append(where.literal()).append(" ");
      }
      if (orderByClause != null) {
        sb.append(orderByClause.literal()).append(" ");
      }
      if (limitClause != null) {
        sb.append(limitClause.literal()).append(" ");
      }
      return sb.toString();
    }

  }

  /**
   * <pre>
   multipleUpdateStatement
    : UPDATE priority=LOW_PRIORITY? IGNORE? tableSources
      SET updatedElement (',' updatedElement)*
      (WHERE expression)?
    ;
   * </pre>
   */
  public static class MultipleUpdateStatement implements UpdateStatement {
    public final Boolean lowPriority;
    public final Boolean ignore;
    public final TableSources tableSources;
    public final List<UpdatedElement> updatedElements;
    public final Expression where;

    MultipleUpdateStatement(Boolean lowPriority, Boolean ignore, TableSources tableSources,
        List<UpdatedElement> updatedElements, Expression where) {
      Preconditions.checkArgument(tableSources != null);
      Preconditions.checkArgument(updatedElements != null && updatedElements.size() > 0);

      this.lowPriority = lowPriority;
      this.ignore = ignore;
      this.tableSources = tableSources;
      this.updatedElements = updatedElements;
      this.where = where;
    }

    @Override
    public String literal() {
      StringBuilder sb = new StringBuilder();
      sb.append("UPDATE ");
      if (Boolean.TRUE.equals(lowPriority)) {
        sb.append("LOW_PRIORITY ");
      }
      if (Boolean.TRUE.equals(ignore)) {
        sb.append("IGNORE ");
      }
      sb.append(tableSources.literal()).append(" ");
      sb.append("SET ");
      List<String> literals = Lists.newArrayList();
      for (UpdatedElement updatedElement : updatedElements) {
        literals.add(updatedElement.literal());
      }
      sb.append(Joiner.on(", ").join(literals)).append(" ");
      if (where != null) {
        sb.append("WHERE ").append(where.literal()).append(" ");
      }
      return sb.toString();
    }

  }
}