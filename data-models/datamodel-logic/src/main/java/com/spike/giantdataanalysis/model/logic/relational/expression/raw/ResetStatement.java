package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 resetStatement
    : RESET QUERY CACHE
    ;
 * </pre>
 */
public class ResetStatement implements AdministrationStatement {
  ResetStatement() {
  }

  @Override
  public String literal() {
    return "RESET QUERY CACHE";
  }
}
