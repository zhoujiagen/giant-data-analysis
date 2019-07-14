package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
replaceStatement
  : REPLACE priority=(LOW_PRIORITY | DELAYED)?
    INTO? tableName
    (PARTITION '(' partitions=uidList ')' )?
    (
      ('(' columns=uidList ')')? insertStatementValue
      | SET
        setFirst=updatedElement
        (',' setElements+=updatedElement)*
    )
  ;
 * </pre>
 */
public class ReplaceStatement implements DmlStatement {

}