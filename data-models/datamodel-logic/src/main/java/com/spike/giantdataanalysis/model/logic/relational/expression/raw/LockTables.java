package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("LOCK TABLES ");
    List<String> literals = Lists.newArrayList();
    for (LockTableElement lockTableElement : lockTableElements) {
      literals.add(lockTableElement.literal());
    }
    sb.append(Joiner.on(", ").join(literals));

    return sb.toString();
  }
}
