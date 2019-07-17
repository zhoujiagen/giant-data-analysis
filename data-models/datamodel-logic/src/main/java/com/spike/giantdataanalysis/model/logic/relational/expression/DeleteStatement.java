package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

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
    public final List<TableName> tableNames;
    public final TableSources tableSources;
    public final Expression where;

    MultipleDeleteStatement(Boolean lowPriority, Boolean quick, Boolean ignore,
        List<TableName> tableNames, TableSources tableSources, Expression where) {
      Preconditions.checkArgument(tableNames != null && tableNames.size() > 0);
      Preconditions.checkArgument(tableSources != null);

      this.lowPriority = lowPriority;
      this.quick = quick;
      this.ignore = ignore;
      this.tableNames = tableNames;
      this.tableSources = tableSources;
      this.where = where;
    }

  }
}