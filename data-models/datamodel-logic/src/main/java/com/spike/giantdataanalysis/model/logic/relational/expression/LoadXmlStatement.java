package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
loadXmlStatement
  : LOAD XML
    priority=(LOW_PRIORITY | CONCURRENT)?
    LOCAL? INFILE filename=STRING_LITERAL
    violation=(REPLACE | IGNORE)?
    INTO TABLE tableName
    (CHARACTER SET charset=charsetName)?
    (ROWS IDENTIFIED BY '<' tag=STRING_LITERAL '>')?
    ( IGNORE decimalLiteral linesFormat=(LINES | ROWS) )?
    ( '(' assignmentField (',' assignmentField)* ')' )?
    (SET updatedElement (',' updatedElement)*)?
  ;
 * </pre>
 */
public class LoadXmlStatement implements DmlStatement {

}