package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 shutdownStatement
    : SHUTDOWN
    ;
 * </pre>
 */
public class ShutdownStatement implements AdministrationStatement {
  @Override
  public String literal() {
    return "SHUTDOWN";
  }
}
