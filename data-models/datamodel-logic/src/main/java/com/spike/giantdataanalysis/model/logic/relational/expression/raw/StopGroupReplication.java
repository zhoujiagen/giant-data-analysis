package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 stopGroupReplication
    : STOP GROUP_REPLICATION
    ;
 * </pre>
 */
public class StopGroupReplication implements ReplicationStatement {
  StopGroupReplication() {
  }

  @Override
  public String literal() {
    return "STOP GROUP_REPLICATION";
  }
}
