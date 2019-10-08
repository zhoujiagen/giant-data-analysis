package com.spike.giantdataanalysis.model.logic.relational.expression.raw;

/**
 * <pre>
 startGroupReplication
    : START GROUP_REPLICATION
    ;
 * </pre>
 */
public class StartGroupReplication implements ReplicationStatement {

  StartGroupReplication() {
  }

  @Override
  public String literal() {
    return "START GROUP_REPLICATION";
  }
}
