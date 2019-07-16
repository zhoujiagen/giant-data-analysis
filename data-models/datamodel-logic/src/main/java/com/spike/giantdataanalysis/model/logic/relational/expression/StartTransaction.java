package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 startTransaction
    : START TRANSACTION (transactionMode (',' transactionMode)* )?
    ;
 * </pre>
 */
public class StartTransaction implements TransactionStatement {
}
