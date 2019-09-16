package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 shutdownStatement
    : SHUTDOWN
    ;
 * </pre>
 */
public class ShutdownStatement implements AdministrationStatement {
  ShutdownStatement() {
  }

  @Override
  public String literal() {
    return "SHUTDOWN";
  }
}
