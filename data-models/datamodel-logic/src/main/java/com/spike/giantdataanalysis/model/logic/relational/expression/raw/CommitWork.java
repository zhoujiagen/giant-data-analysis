package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 commitWork
    : COMMIT WORK?
      (AND nochain=NO? CHAIN)?
      (norelease=NO? RELEASE)?
    ;
 * </pre>
 */
public class CommitWork implements TransactionStatement {

  public final Boolean chain;
  public final Boolean release;

  CommitWork(Boolean chain, Boolean release) {
    this.chain = chain;
    this.release = release;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("COMMIT WORK ");
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
