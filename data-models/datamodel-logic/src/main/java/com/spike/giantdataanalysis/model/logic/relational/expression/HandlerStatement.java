package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
handlerStatement
  : handlerOpenStatement
  | handlerReadIndexStatement
  | handlerReadStatement
  | handlerCloseStatement
  ;
 * </pre>
 */
public interface HandlerStatement extends DmlStatement {

  /**
   * <pre>
   handlerOpenStatement
    : HANDLER tableName OPEN (AS? uid)?
    ;
   * </pre>
   */
  public static class HandlerOpenStatement implements HandlerStatement {

  }

  /**
   * <pre>
   handlerReadIndexStatement
    : HANDLER tableName READ index=uid
      (
        comparisonOperator '(' constants ')'
        | moveOrder=(FIRST | NEXT | PREV | LAST)
      )
      (WHERE expression)? (LIMIT decimalLiteral)?
    ;
   * </pre>
   */
  public static class HandlerReadIndexStatement implements HandlerStatement {

  }

  /**
   * <pre>
   handlerReadStatement
    : HANDLER tableName READ moveOrder=(FIRST | NEXT)
      (WHERE expression)? (LIMIT decimalLiteral)?
    ;
   * </pre>
   */
  public static class HandlerReadStatement implements HandlerStatement {

  }

  /**
   * <pre>
   handlerCloseStatement
    : HANDLER tableName CLOSE
    ;
   * </pre>
   */
  public static class HandlerCloseStatement implements HandlerStatement {

  }
}