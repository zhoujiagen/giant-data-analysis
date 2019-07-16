package com.spike.giantdataanalysis.model.logic.relational.expression;

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

  }
}