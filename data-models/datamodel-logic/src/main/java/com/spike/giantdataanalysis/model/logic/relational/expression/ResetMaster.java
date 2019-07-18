package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 resetMaster
    : RESET MASTER
    ;
 * </pre>
 */
public class ResetMaster implements ReplicationStatement {
  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
