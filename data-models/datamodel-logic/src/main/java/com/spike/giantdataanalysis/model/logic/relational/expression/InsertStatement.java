package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
insertStatement
  : INSERT
    priority=(LOW_PRIORITY | DELAYED | HIGH_PRIORITY)?
    IGNORE? INTO? tableName
    (PARTITION '(' partitions=uidList ')' )?
    (
      ('(' columns=uidList ')')? insertStatementValue
      | SET
          setFirst=updatedElement
          (',' setElements+=updatedElement)*
    )
    (
      ON DUPLICATE KEY UPDATE
      duplicatedFirst=updatedElement
      (',' duplicatedElements+=updatedElement)*
    )?
  ;
 * </pre>
 */
public class InsertStatement implements DmlStatement {

}