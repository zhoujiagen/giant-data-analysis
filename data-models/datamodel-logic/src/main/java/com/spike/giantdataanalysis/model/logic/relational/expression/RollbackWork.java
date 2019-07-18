package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 rollbackWork
    : ROLLBACK WORK?
      (AND nochain=NO? CHAIN)?
      (norelease=NO? RELEASE)?
    ;
 * </pre>
 */
public class RollbackWork implements TransactionStatement {
  public final Boolean chain;
  public final Boolean release;

  RollbackWork(Boolean chain, Boolean release) {
    this.chain = chain;
    this.release = release;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
