package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
 stopGroupReplication
    : STOP GROUP_REPLICATION
    ;
 * </pre>
 */
public class StopGroupReplication implements ReplicationStatement {
  @Override
  public String literal() {
    StringBuilder sb = new StringBuilder();
    return sb.toString();
  }
}
