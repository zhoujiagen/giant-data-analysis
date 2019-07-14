package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
loadDataStatement
  : LOAD DATA
    priority=(LOW_PRIORITY | CONCURRENT)?
    LOCAL? INFILE filename=STRING_LITERAL
    violation=(REPLACE | IGNORE)?
    INTO TABLE tableName
    (PARTITION '(' uidList ')' )?
    (CHARACTER SET charset=charsetName)?
    (
      fieldsFormat=(FIELDS | COLUMNS)
      selectFieldsInto+
    )?
    (
      LINES
        selectLinesInto+
    )?
    (
      IGNORE decimalLiteral linesFormat=(LINES | ROWS)
    )?
    ( '(' assignmentField (',' assignmentField)* ')' )?
    (SET updatedElement (',' updatedElement)*)?
  ;
 * </pre>
 */
public class LoadDataStatement implements DmlStatement {

}