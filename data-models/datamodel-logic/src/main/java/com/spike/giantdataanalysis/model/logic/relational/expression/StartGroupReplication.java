package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 startGroupReplication
    : START GROUP_REPLICATION
    ;
 * </pre>
 */
public class StartGroupReplication implements ReplicationStatement {
  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
