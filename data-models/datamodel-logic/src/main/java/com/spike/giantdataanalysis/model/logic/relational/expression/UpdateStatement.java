package com.spike.giantdataanalysis.model.logic.relational.expression;

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

  }
}