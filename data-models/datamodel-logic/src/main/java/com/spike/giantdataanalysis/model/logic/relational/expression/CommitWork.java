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
}
