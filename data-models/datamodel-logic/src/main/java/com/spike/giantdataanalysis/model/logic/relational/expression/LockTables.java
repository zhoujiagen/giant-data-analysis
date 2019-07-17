package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
 lockTables
    : LOCK TABLES lockTableElement (',' lockTableElement)*
    ;
 * </pre>
 */
public class LockTables implements TransactionStatement {
  public final List<LockTableElement> lockTableElements;

  LockTables(List<LockTableElement> lockTableElements) {
    Preconditions.checkArgument(lockTableElements != null && lockTableElements.size() > 0);

    this.lockTableElements = lockTableElements;
  }

}
