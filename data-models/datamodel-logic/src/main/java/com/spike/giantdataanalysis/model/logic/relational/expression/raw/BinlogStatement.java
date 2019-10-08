package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;

/**
 * <pre>
 binlogStatement
    : BINLOG STRING_LITERAL
    ;
 * </pre>
 */
public class BinlogStatement implements AdministrationStatement {
  public final String binlog;

  BinlogStatement(String binlog) {
    Preconditions.checkArgument(binlog != null);

    this.binlog = binlog;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("BINLOG ").append(binlog);
    return sb.toString();
  }
}
