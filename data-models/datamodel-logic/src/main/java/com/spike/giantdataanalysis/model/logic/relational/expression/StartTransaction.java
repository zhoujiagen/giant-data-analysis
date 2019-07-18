package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

/**
 * <pre>
 startTransaction
    : START TRANSACTION (transactionMode (',' transactionMode)* )?
    ;
 * </pre>
 */
public class StartTransaction implements TransactionStatement {

  public final List<TransactionModeEnum> transactionModes;

  StartTransaction(List<TransactionModeEnum> transactionModes) {
    this.transactionModes = transactionModes;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
