package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 unlockTables
    : UNLOCK TABLES
    ;
 * </pre>
 */
public class UnlockTables implements TransactionStatement {
  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
