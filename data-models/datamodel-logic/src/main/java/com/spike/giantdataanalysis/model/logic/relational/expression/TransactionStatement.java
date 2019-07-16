package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
transactionStatement
  : startTransaction
  | beginWork | commitWork | rollbackWork
  | savepointStatement | rollbackStatement
  | releaseStatement | lockTables | unlockTables
  ;
 * </pre>
 */
public interface TransactionStatement extends SqlStatement {

  /**
   * <pre>
   setAutocommitStatement
    : SET AUTOCOMMIT '=' autocommitValue=('0' | '1')
    ;
   * </pre>
   */
  public static class SetAutocommitStatement implements SetStatement {

  }

  /**
   * <pre>
   setTransactionStatement
    : SET transactionContext=(GLOBAL | SESSION)? TRANSACTION
      transactionOption (',' transactionOption)*
    ;
   * </pre>
   */
  public static class SetTransactionStatement implements SetStatement {

  }

  /**
   * <pre>
   transactionMode
    : WITH CONSISTENT SNAPSHOT
    | READ WRITE
    | READ ONLY
    ;
   * </pre>
   */
  public static class TransactionMode implements PrimitiveExpression {

  }

  /**
   * <pre>
   lockTableElement
    : tableName (AS? uid)? lockAction
    ;
   * </pre>
   */
  public static class LockTableElement implements PrimitiveExpression {

  }

  /**
   * <pre>
   lockAction
    : READ LOCAL? | LOW_PRIORITY? WRITE
    ;
   * </pre>
   */
  public static class LockAction implements PrimitiveExpression {

  }

  /**
   * <pre>
   transactionOption
    : ISOLATION LEVEL transactionLevel
    | READ WRITE
    | READ ONLY
    ;
   * </pre>
   */
  public static class TransactionOption implements PrimitiveExpression {

  }

  /**
   * <pre>
   transactionLevel
    : REPEATABLE READ
    | READ COMMITTED
    | READ UNCOMMITTED
    | SERIALIZABLE
    ;
   * </pre>
   */
  public static class TransactionLevel implements PrimitiveExpression {

  }
}