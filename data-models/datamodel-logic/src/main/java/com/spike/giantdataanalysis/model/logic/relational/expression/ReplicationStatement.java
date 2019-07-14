package com.spike.giantdataanalysis.model.logic.relational.expression;

/**
 * <pre>
replicationStatement
  : changeMaster | changeReplicationFilter | purgeBinaryLogs
  | resetMaster | resetSlave | startSlave | stopSlave
  | startGroupReplication | stopGroupReplication
  | xaStartTransaction | xaEndTransaction | xaPrepareStatement
  | xaCommitWork | xaRollbackWork | xaRecoverWork
  ;
 * </pre>
 */
public interface ReplicationStatement extends SqlStatement {
}