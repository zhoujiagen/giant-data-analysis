package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
    sb.append("FLUSH ");
    if (flushFormat != null) {
      sb.append(flushFormat.literal()).append(" ");
    }
    List<String> literals = Lists.newArrayList();
    for (FlushOption flushOption : flushOptions) {
      literals.add(flushOption.literal());
    }
    sb.append(Joiner.on(", ").join(literals));
    return sb.toString();
  }
}
