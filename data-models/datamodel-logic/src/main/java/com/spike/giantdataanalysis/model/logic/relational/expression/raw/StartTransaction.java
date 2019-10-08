package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

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
    sb.append("START TRANSACTION ");
    List<String> literals = Lists.newArrayList();
    for (TransactionModeEnum transactionMode : transactionModes) {
      literals.add(transactionMode.literal());
    }
    sb.append(Joiner.on(", ").join(literals));

    return sb.toString();
  }
}
