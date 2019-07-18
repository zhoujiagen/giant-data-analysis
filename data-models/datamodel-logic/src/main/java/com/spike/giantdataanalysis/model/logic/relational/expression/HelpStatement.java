package com.spike.giantdataanalysis.model.logic.relational.expression;

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
    return sb.toString();
  }
}
