package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 alterInstance
    : ALTER INSTANCE ROTATE INNODB MASTER KEY
    ;
 * </pre>
 */
public class AlterInstance implements DdlStatement {
  @Override
  public String literal() {
    return "ALTER INSTANCE ROTATE INNODB MASTER KEY";
  }
}
