package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

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
    sb.append("ROLLBACK WORK ");
    if (Boolean.TRUE.equals(chain)) {
      sb.append("AND CHAIN ");
    } else if (Boolean.FALSE.equals(chain)) {
      sb.append("AND NO CHAIN ");
    }
    if (Boolean.TRUE.equals(release)) {
      sb.append("RELEASE");
    } else if (Boolean.FALSE.equals(release)) {
      sb.append("NO RELEASE");
    }
    return sb.toString();
  }
}
