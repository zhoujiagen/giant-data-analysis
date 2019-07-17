package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.SelectStatement.LimitClause;

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

  }
}