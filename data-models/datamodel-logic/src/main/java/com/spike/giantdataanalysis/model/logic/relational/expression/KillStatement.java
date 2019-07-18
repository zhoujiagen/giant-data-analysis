package com.spike.giantdataanalysis.model.logic.relational.expression;

import java.util.List;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.model.logic.relational.expression.Literals.DecimalLiteral;

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
    return sb.toString();
  }
}
