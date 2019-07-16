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
}
