package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 resetStatement
    : RESET QUERY CACHE
    ;
 * </pre>
 */
public class ResetStatement implements AdministrationStatement {
  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
