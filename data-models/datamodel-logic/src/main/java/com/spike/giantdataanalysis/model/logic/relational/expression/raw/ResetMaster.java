package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 resetMaster
    : RESET MASTER
    ;
 * </pre>
 */
public class ResetMaster implements ReplicationStatement {
  ResetMaster() {
  }

  @Override
  public String literal() {
    return "RESET MASTER";
  }
}
