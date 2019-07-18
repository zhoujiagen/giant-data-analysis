package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * <pre>
 flushStatement
    : FLUSH flushFormat=(NO_WRITE_TO_BINLOG | LOCAL)?
      flushOption (',' flushOption)*
    ;
 * </pre>
 */
public class FlushStatement implements AdministrationStatement {
  public final FlushFormatEnum flushFormat;
  public final List<FlushOption> flushOptions;

  FlushStatement(FlushFormatEnum flushFormat, List<FlushOption> flushOptions) {
    Preconditions.checkArgument(flushOptions != null && flushOptions.size() > 0);

    this.flushFormat = flushFormat;
    this.flushOptions = flushOptions;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
