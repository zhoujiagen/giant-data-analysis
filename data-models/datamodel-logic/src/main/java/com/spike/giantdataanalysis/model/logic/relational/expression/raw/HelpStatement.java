package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

import com.google.common.base.Preconditions;

/**
 * <pre>
 helpStatement
    : HELP STRING_LITERAL
    ;
 * </pre>
 */
public class HelpStatement implements UtilityStatement {
  public final String help;

  HelpStatement(String help) {
    Preconditions.checkArgument(help != null);

    this.help = help;
  }

  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    sb.append("HELP ").append(help);
    return sb.toString();
  }
}
