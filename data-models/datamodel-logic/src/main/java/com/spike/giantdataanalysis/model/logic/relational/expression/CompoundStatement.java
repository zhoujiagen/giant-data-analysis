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
public interface CompoundStatement extends RelationalAlgebraExpression {

  /**
   * <pre>
   routineBody
    : blockStatement | sqlStatement
    ;
   * </pre>
   */
  public static class RoutineBody implements PrimitiveExpression {

  }

  /**
   * <pre>
  declareVariable
    : DECLARE uidList dataType (DEFAULT defaultValue)?
    ;
   * </pre>
   */
  public static class DeclareVariable implements PrimitiveExpression {

  }

  /**
   * <pre>
  declareCondition
    : DECLARE uid CONDITION FOR
      ( decimalLiteral | SQLSTATE VALUE? STRING_LITERAL)
    ;
   * </pre>
   */
  public static class DeclareCondition implements PrimitiveExpression {

  }

  /**
   * <pre>
  declareCursor
    : DECLARE uid CURSOR FOR selectStatement
    ;
   * </pre>
   */
  public static class DeclareCursor implements PrimitiveExpression {

  }

  /**
   * <pre>
  declareHandler
    : DECLARE handlerAction=(CONTINUE | EXIT | UNDO)
      HANDLER FOR
      handlerConditionValue (',' handlerConditionValue)*
      routineBody
    ;
   * </pre>
   */
  public static class DeclareHandler implements PrimitiveExpression {

  }

  /**
   * <pre>
  handlerConditionValue
    : decimalLiteral                                                #handlerConditionCode
    | SQLSTATE VALUE? STRING_LITERAL                                #handlerConditionState
    | uid                                                           #handlerConditionName
    | SQLWARNING                                                    #handlerConditionWarning
    | NOT FOUND                                                     #handlerConditionNotfound
    | SQLEXCEPTION                                                  #handlerConditionException
    ;
   * </pre>
   */
  public static class HandlerConditionValue implements PrimitiveExpression {

  }

  /**
   * <pre>
  procedureSqlStatement
    : (compoundStatement | sqlStatement) SEMI
    ;
   * </pre>
   */
  public static class ProcedureSqlStatement implements PrimitiveExpression {

  }

  /**
   * <pre>
  caseAlternative
    : WHEN (constant | expression)
      THEN procedureSqlStatement+
    ;
   * </pre>
   */
  public static class CaseAlternative implements PrimitiveExpression {

  }

  /**
   * <pre>
  elifAlternative
    : ELSEIF expression
      THEN procedureSqlStatement+
    ;
   * </pre>
   */
  public static class ElifAlternative implements PrimitiveExpression {

  }
}
