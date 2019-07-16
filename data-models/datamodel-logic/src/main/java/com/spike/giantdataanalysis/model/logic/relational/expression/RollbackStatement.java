package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 rollbackStatement
    : ROLLBACK WORK? TO SAVEPOINT? uid
    ;
 * </pre>
 */
public class RollbackStatement implements TransactionStatement {
}
