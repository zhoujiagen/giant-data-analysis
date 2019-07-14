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
}