package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;

/**
 * <pre>
 killStatement
    : KILL connectionFormat=(CONNECTION | QUERY)?
      decimalLiteral+
    ;
 * </pre>
 */
public class KillStatement implements AdministrationStatement {

  public final ConnectionFormatEnum connectionFormat;
  public final List<DecimalLiteral> decimalLiterals;

  KillStatement(ConnectionFormatEnum connectionFormat, List<DecimalLiteral> decimalLiterals) {
    Preconditions.checkArgument(decimalLiterals != null && decimalLiterals.size() > 0);

    this.connectionFormat = connectionFormat;
    this.decimalLiterals = decimalLiterals;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("KILL ");
    if (connectionFormat != null) {
      sb.append(connectionFormat.literal()).append(" ");
    }
    List<String> literals = Lists.newArrayList();
    for (DecimalLiteral decimalLiteral : decimalLiterals) {
      literals.add(decimalLiteral.literal());
    }
    sb.append(Joiner.on(" ").join(literals));
    return sb.toString();
  }
}
