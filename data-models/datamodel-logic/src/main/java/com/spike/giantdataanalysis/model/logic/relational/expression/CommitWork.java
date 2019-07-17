package com.spike.giantdataanalysis.model.logic.relational.expression;

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

}
