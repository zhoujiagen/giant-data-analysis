package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
compoundStatement
  : blockStatement
  | caseStatement | ifStatement | leaveStatement
  | loopStatement | repeatStatement | whileStatement
  | iterateStatement | returnStatement | cursorStatement
  ;
 * </pre>
 */
public interface UtilityStatement extends SqlStatement {
}