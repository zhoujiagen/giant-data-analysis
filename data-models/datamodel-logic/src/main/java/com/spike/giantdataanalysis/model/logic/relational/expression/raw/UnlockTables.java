package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 unlockTables
    : UNLOCK TABLES
    ;
 * </pre>
 */
public class UnlockTables implements TransactionStatement {
  UnlockTables() {
  }

  @Override
  public String literal() {
    return "UNLOCK TABLES";
  }
}
